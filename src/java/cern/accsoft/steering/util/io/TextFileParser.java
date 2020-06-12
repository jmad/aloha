package cern.accsoft.steering.util.io;

import java.io.File;
import java.util.List;


/**
 * A simple parser, that opens an ASCII file and returns all the lines in a list.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public interface TextFileParser {

    /**
     * parses the file and returns all the lines in an ArrayList
     * 
     * @param file the file to parse
     * @return a list containing strings which represent the lines in the file.
     * @throws TextFileParserException if the parsing is not possible
     */
    public abstract List<String> parse(File file) throws TextFileParserException;

}