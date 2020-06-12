package cern.accsoft.steering.util.io.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import cern.accsoft.steering.util.io.TextFileParser;
import cern.accsoft.steering.util.io.TextFileParserException;

// jwenning : removed as external lib not longer available
// import org.apache.tools.bzip2.CBZip2InputStream;

/**
 * Simple parser, that opens a TextFile and returns the (trimmed) lines as List.
 *
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class TextFileParserImpl implements TextFileParser {
    public static final String EXTENSION_GZIP = ".gz";

    @Override
    public List<String> parse(File file) throws TextFileParserException {
        if (file == null) {
            throw new TextFileParserException("Failure: file must not be null!");
        }

        List<String> lines = new ArrayList<>();
        BufferedReader bufferedReader = openReader(file);

        try {
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }

                line = line.trim();
                if (line.length() > 0) { // ignore empty lines
                    lines.add(line.trim());
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new TextFileParserException("IO-failure!", e);
        }
        return lines;
    }

    /**
     * opens an valid reader, depending on the ending of the fileName.
     *
     * @param file the file to open
     * @return the reader if successful
     * @throws TextFileParserException if the reader cannot be opened
     */
    private BufferedReader openReader(File file) throws TextFileParserException {
        BufferedReader reader;
        try {
            if (file.getCanonicalPath().endsWith(EXTENSION_GZIP)) {
                reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
            } else {
                reader = new BufferedReader(new FileReader(file));
            }
        } catch (Exception e) {
            throw new TextFileParserException(
                    "Error while opening inputstream for filename '" + file.getAbsolutePath() + "'.", e);
        }
        return reader;
    }

}
