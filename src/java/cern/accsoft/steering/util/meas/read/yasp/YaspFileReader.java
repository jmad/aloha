package cern.accsoft.steering.util.meas.read.yasp;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import cern.accsoft.steering.util.acc.BeamNumber;
import cern.accsoft.steering.util.io.TextFileParser;
import cern.accsoft.steering.util.io.TextFileParserException;
import cern.accsoft.steering.util.io.impl.TextFileParserImpl;
import cern.accsoft.steering.util.meas.data.Plane;
import cern.accsoft.steering.util.meas.data.yasp.CorrectorValue;
import cern.accsoft.steering.util.meas.data.yasp.MonitorValue;
import cern.accsoft.steering.util.meas.data.yasp.ReadingData;
import cern.accsoft.steering.util.meas.data.yasp.ReadingDataImpl;
import cern.accsoft.steering.util.meas.data.yasp.YaspHeaderData;
import cern.accsoft.steering.util.meas.data.yasp.YaspHeaderImpl;
import cern.accsoft.steering.util.meas.read.ReaderException;
import cern.accsoft.steering.util.meas.read.ReadingDataReader;
import cern.accsoft.steering.util.meas.read.filter.ReadSelectionFilter;
import org.apache.log4j.Logger;

/**
 * this is the base class for readers, which read yasp-output files.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class YaspFileReader implements ReadingDataReader {
    private final static Logger LOGGER = Logger.getLogger(YaspFileReader.class);

    /** The text-parser, which we use when looping through the lines */
    private TextFileParser textFileParser = new TextFileParserImpl();

    /**
     * this enum describes, which readmode we are actually in, when parsing through the lines of the file.
     * 
     * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
     */
    private enum ReadMode {
        CORRECTOR,
        MONITOR
    }

    @Override
    public ReadingData read(File file) throws ReaderException {
        return this.read(file, null);
    }

    @Override
    public ReadingData read(File file, ReadSelectionFilter selection) throws ReaderException {
        ReadingDataImpl readingData = new ReadingDataImpl();
        this.read(file, selection, readingData);
        return readingData;
    }

    /**
     * reads the data from a file using the given selection filter and puts the data into the giving reading data. This
     * method might be called by subclasses, which want to to setup the object beforehand.
     * 
     * @param file the file from which to read the data
     * @param filter the filter to use for loading the data
     * @param readingData the data object into which to put the data
     * @throws YaspReaderException if reading returns errors
     */
    protected final void read(File file, ReadSelectionFilter filter, ReadingDataImpl readingData)
            throws YaspReaderException {
        List<String> lines;
        try {
            lines = textFileParser.parse(file);
        } catch (TextFileParserException e) {
            throw new YaspReaderException("Error while parsing file '" + file + "'", e);
        }

        ReadMode readMode = null;
        YaspHeaderImpl yaspHeader = new YaspHeaderImpl();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("@")) {
                this.parseHeaderLine(yaspHeader, line);
            } else if (line.startsWith("*")) {
                // ignore (column titles)
            } else if (line.startsWith("#")) {
                // switch between monitors and corrector magnets:
                if (line.startsWith("# MONITOR")) {
                    readMode = ReadMode.MONITOR;
                    LOGGER.debug("Switched to MONITOR mode for file '" + file.getAbsolutePath() + "'");
                } else if (line.startsWith("# CORRECTOR")) {
                    readMode = ReadMode.CORRECTOR;
                    LOGGER.debug("Switched to CORRECTOR mode for file '" + file.getAbsolutePath() + "'");
                } else {
                    throw new InconsistentYaspFileException("Unkown tag '" + line + "' in file '" + file + "'");
                }
            } else if (line.trim().length() != 0) {
                if (readMode == ReadMode.MONITOR) {
                    MonitorValue monitorValue = parseMonitorLine(line);
                    if (isOfInterest(filter, monitorValue)) {
                        readingData.add(monitorValue);
                    }
                } else if (readMode == ReadMode.CORRECTOR) {
                    CorrectorValue correctorValue = parseCorrectorLine(line);
                    if (isOfInterest(filter, correctorValue)) {
                        readingData.add(correctorValue);
                    }
                } else {
                    throw new InconsistentYaspFileException(
                            "Neither found tags '# MONITOR' nor '# CORRECTOR' in file '" + file + "'");
                }
            }
        }

        /* set the parsed yasp header data */
        readingData.setHeaderData(yaspHeader);
    }

    /**
     * parses one line of the header information and puts it into the given yasp-header.
     * 
     * @param yaspHeader the header in into which to store the paresd values
     * @param line the line to pars
     * @throws InconsistentYaspFileException if the line can not be parsed correctly.
     */
    private void parseHeaderLine(YaspHeaderImpl yaspHeader, String line) throws InconsistentYaspFileException {
        if (line == null) {
            throw new NullPointerException("Line to be parsed must not be null!");
        }

        StringTokenizer tokenizer = tokenizerFor(line);

        /* remove the @ at the beginning */
        tokenizer.nextToken();

        YaspHeaderData data = YaspHeaderData.fromString(tokenizer.nextToken());
        if (data != YaspHeaderData.UNKNOWN) {
            checkConsistency(line);
            /* remove the format specifier */
            tokenizer.nextToken();
            yaspHeader.addEntry(data, tokenizer.nextToken());
        }
    }

    private void checkConsistency(String line) throws InconsistentYaspFileException {
        int tokenCount = tokenizerFor(line).countTokens();
        if (tokenCount < 4) {
            throw new InconsistentYaspFileException("Header line '" + line + "' must have at least 4 fields but has "
                    + tokenCount + ".");
        }
    }

    private StringTokenizer tokenizerFor(String line) {
        return new StringTokenizer(line);
    }

    /**
     * parses a line and puts the values into a corrector-object.
     * 
     * @param line the line to parse
     * @return the corrector.
     * @throws InconsistentYaspFileException
     */
    protected CorrectorValue parseCorrectorLine(String line) throws InconsistentYaspFileException {
        if (line == null)
            throw new NullPointerException("Line to be parsed must not be null!");

        StringTokenizer tokenizer = new StringTokenizer(line, " \t");
        int countTokens = tokenizer.countTokens();
        if ((countTokens != 5) && (countTokens != 6)) {
            throw new InconsistentYaspFileException("Corrector line '" + line + "' must have 5 or 6 fields but has "
                    + countTokens + ".");
        }

        CorrectorValue corrector = new CorrectorValue();
        try {
            corrector.setName(tokenizer.nextToken());
            corrector.setPlane(Plane.fromTag(tokenizer.nextToken()));
            corrector.setBeamNumber(BeamNumber.fromInt(new Integer(tokenizer.nextToken())));
            corrector.strengthName = tokenizer.nextToken();
            corrector.kick = new Double(tokenizer.nextToken());
            if (countTokens > 5) {
                corrector.rtKick = new Double(tokenizer.nextToken());
            }
        } catch (RuntimeException e) {
            throw new InconsistentYaspFileException("Error while processing Corrector - line '" + line + "'", e);
        }

        return corrector;
    }

    /**
     * parses a line and puts the values into a monitor-object.
     * 
     * @param line the line to parse
     * @return the monitor.
     * @throws InconsistentYaspFileException
     */
    protected MonitorValue parseMonitorLine(String line) throws InconsistentYaspFileException {
        if (line == null) {
            throw new NullPointerException("Line to be parsed must not be null!");
        }

        StringTokenizer tokenizer = new StringTokenizer(line, " \t");
        int countTokens = tokenizer.countTokens();
        if (countTokens != 9) {
            throw new InconsistentYaspFileException("Monitor line '" + line + "' must have 9 fields, but has "
                    + countTokens + ".");
        }

        // * NAME PLANE BEAM POS RMS SUM HW-STATUS STATUS STATUS-TAG
        MonitorValue monitor = new MonitorValue();
        try {
            monitor.setName(tokenizer.nextToken());
            monitor.setPlane(Plane.fromTag(tokenizer.nextToken()));
            monitor.setBeamNumber(BeamNumber.fromInt(new Integer(tokenizer.nextToken())));
            monitor.setBeamPosition(new Double(tokenizer.nextToken()));
            monitor.rms = new Double(tokenizer.nextToken());
            monitor.sum = new Double(tokenizer.nextToken());
            monitor.hwStatus = new Integer(tokenizer.nextToken());
            monitor.status = new Integer(tokenizer.nextToken());
            // we do not process status tag at the moment.
        } catch (RuntimeException e) {
            throw new InconsistentYaspFileException("Error while processing Monitor - line '" + line + "'", e);
        }
        return monitor;
    }

    /**
     * decides if we will use the given monitor
     * 
     * @param filter the filter to query, if the value shall be loaded
     * @param monitorValue the monitor value to check
     * @return <code>true</code>, if the data for the monitor shall be loaded, <code>false</code> otherwise
     */
    private boolean isOfInterest(ReadSelectionFilter filter, MonitorValue monitorValue) {
        if (filter != null) {
            return filter.isOfInterest(monitorValue);
        } else {
            /* by default we read all elements */
            return true;
        }
    }

    /**
     * decides if we will use the given corrector
     * 
     * @param filter the filter to query, if the value shall be loaded.
     * @param correctorValue the corrector value to check
     * @return <code>true</code> if the corrector value shall be loade, <code>false</code> otherwise
     */
    private boolean isOfInterest(ReadSelectionFilter filter, CorrectorValue correctorValue) {
        if (filter != null) {
            return filter.isOfInterest(correctorValue);
        } else {
            /* by default we read all elements */
            return true;
        }
    }

}