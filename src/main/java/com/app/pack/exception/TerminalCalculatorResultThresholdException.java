package com.app.pack.exception;

/**
 * Created by jayakrishnansomasekharannair on 6/26/18.
 */

/**
 * TerminalCalculatorResultThresholdException is thrown when the result exceeds the desired threshold.
 */
public class TerminalCalculatorResultThresholdException extends Exception {

    public TerminalCalculatorResultThresholdException(String errorMessage) {
        super(errorMessage);
    }

}
