package com.app.pack.util;

import com.app.pack.constants.TerminalCalculatorConstants;
import com.app.pack.exception.TerminalCalculatorException;
import com.app.pack.exception.TerminalCalculatorExpressionFormatException;
import com.app.pack.exception.TerminalCalculatorInvalidArgumentException;
import com.app.pack.exception.TerminalCalculatorOperationException;
import com.app.pack.exception.TerminalCalculatorResultThresholdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jayakrishnansomasekharannair on 6/26/18.
 */
public class TerminalCalculatorUtils implements TerminalCalculatorConstants {

    private static Logger LOGGER = LogManager.getLogger(TerminalCalculatorUtils.class.getName());

    public static boolean checkNullOrEmpty(String string) {
        return (string == null || string.trim().equals(""));
    }

    public static boolean isANumber(String string){
        return string.matches("\\d+");
    }


    public static void handleException(Exception exception) throws TerminalCalculatorException {

        LOGGER.info("handleException in action");

        StringBuilder errorMessage = new StringBuilder(EXCEPTION_OCCURRED_MESSAGE);

        errorMessage.append(exception.getMessage());
        if(exception instanceof TerminalCalculatorInvalidArgumentException) {
            errorMessage.append(EXCEPTION_CATEGORY_MESSAGE).append("TerminalCalculatorInvalidArgumentException");
        } else if (exception instanceof TerminalCalculatorResultThresholdException) {
            errorMessage.append(EXCEPTION_CATEGORY_MESSAGE).append("TerminalCalculatorResultThresholdException");
        } else if (exception instanceof TerminalCalculatorOperationException) {
            errorMessage.append(EXCEPTION_CATEGORY_MESSAGE).append("TerminalCalculatorOperationException");
        } else if (exception instanceof TerminalCalculatorExpressionFormatException) {
            errorMessage.append(EXCEPTION_CATEGORY_MESSAGE).append("TerminalCalculatorExpressionFormatException");
        } else {
            errorMessage.append(exception.getMessage());
        }

        LOGGER.warn("Throwing TerminalCalculatorException");

        throw new TerminalCalculatorException(errorMessage.toString());

    }
}
