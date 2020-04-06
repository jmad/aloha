/*
 * $Id: AlohaCommandLineParser.java,v 1.3 2009-03-16 16:38:11 kfuchsbe Exp $
 *
 * $Date: 2009-03-16 16:38:11 $
 * $Revision: 1.3 $
 * $Author: kfuchsbe $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.aloha.app;

import cern.accsoft.steering.jmad.util.JMadPreferences;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class defines which commandline-arguments we want to find and stores
 * them in the preferences-object.
 *
 * @author kfuchsbe
 */
public class AlohaCommandLineParser {

    /* the logger */
    private final static Logger LOGGER = LoggerFactory.getLogger(AlohaCommandLineParser.class);

    /*
     * names of possible options
     */
    private final static String OPTION_INPUT_PATH = "input-path";
    private final static String OPTION_OUTPUT_PATH = "output-path";
    private final static String OPTION_MEASUREMENT_NUMBER = "measurement-number";
    private final static String OPTION_SELF_TEST = "self-test";

    /**
     * parses the arguments and passes them to the preferences.
     *
     * @param args
     */
    public final static void parse(String[] args, Preferences preferences,
                                   JMadPreferences jmadPreferences) {
        if ((preferences == null) || (jmadPreferences == null)) {
            return;
        }

        // create the command line parser
        CommandLineParser parser = new PosixParser();

        // create the Options
        Options options = new Options();

        OptionBuilder.withLongOpt(OPTION_INPUT_PATH);
        OptionBuilder
                .withDescription("set the path to the default input data folder");
        OptionBuilder.withValueSeparator('=');
        OptionBuilder.hasArg();
        options.addOption(OptionBuilder.create());

        OptionBuilder.withLongOpt(OPTION_OUTPUT_PATH);
        OptionBuilder
                .withDescription("set the path to the default outpur folder");
        OptionBuilder.withValueSeparator('=');
        OptionBuilder.hasArg();
        options.addOption(OptionBuilder.create());

        OptionBuilder.withLongOpt(OPTION_MEASUREMENT_NUMBER);
        OptionBuilder.withDescription("the measurement number to load");
        OptionBuilder.withValueSeparator('=');
        OptionBuilder.hasArg();
        options.addOption(OptionBuilder.create());

        OptionBuilder.withLongOpt(OPTION_SELF_TEST);
        OptionBuilder.withDescription("run selftest on startup");
        options.addOption(OptionBuilder.create());

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            // read the options if available
            if (line.hasOption(OPTION_INPUT_PATH)) {
                String inputPath = line.getOptionValue(OPTION_INPUT_PATH);
                preferences.setInputPath(inputPath);
                LOGGER.info("Set input path from commandline: '" + inputPath
                        + "'");
            }

            if (line.hasOption(OPTION_OUTPUT_PATH)) {
                String outputPath = line.getOptionValue(OPTION_OUTPUT_PATH);
                jmadPreferences.setOutputPath(outputPath);
                LOGGER.info("Set output path from commandline: '" + outputPath
                        + "'");
            }

            if (line.hasOption(OPTION_MEASUREMENT_NUMBER)) {
                String strValue = line
                        .getOptionValue(OPTION_MEASUREMENT_NUMBER);
                try {
                    Integer measurementNumber = Integer.parseInt(strValue);
                    preferences.setMeasurementNumber(measurementNumber);
                    LOGGER.info("Set measurement number from commandline: '"
                            + measurementNumber + "'");
                } catch (NumberFormatException e) {
                    LOGGER.warn("Could not parse value '" + strValue
                            + "' for option '" + OPTION_MEASUREMENT_NUMBER
                            + "' to integer.");
                }
            }

            if (line.hasOption(OPTION_SELF_TEST)) {
                preferences.setSelfTestEnabled(true);
            }
        } catch (ParseException exp) {
            LOGGER.warn("Unexpected exception while parsing commandline.", exp);
        }

    }
}
