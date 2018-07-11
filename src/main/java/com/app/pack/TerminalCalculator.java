package com.app.pack;

import com.app.pack.exception.TerminalCalculatorException;
import com.app.pack.processor.TerminalCalculatorProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jayakrishnansomasekharannair on 6/26/18.
 */
public class TerminalCalculator {

    /**
     * LOGGER instance
     */
    private static Logger LOGGER = LogManager.getLogger(TerminalCalculator.class.getName());

    /**
     * Main method of the terminal calculator application.
     * @param args
     */

    public static void main(String[] args) {

        LOGGER.info("Terminal Calculator Launched");

        final String inputExpression = args[0];
        TerminalCalculatorProcessor terminalCalculator = TerminalCalculatorProcessor.getInstance();
        try {
            System.out.println(terminalCalculator.evaluate(inputExpression));
        } catch (TerminalCalculatorException e) {
            System.out.println(e.getMessage()+"\r check log file for more details");
            LOGGER.error("Exception from  TerminalCalculatorProcessor : " + e.getMessage());
        }

        LOGGER.info("Terminal Calculator Exit");

    }


}
