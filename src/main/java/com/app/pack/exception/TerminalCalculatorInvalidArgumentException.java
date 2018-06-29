package com.app.pack.exception;

/**
 * Created by jayakrishnansomasekharannair on 6/26/18.
 */

/**
 * TerminalCalculatorInvalidArgumentException is thrown when the expression is not considered as valid after processing.
 */
public class TerminalCalculatorInvalidArgumentException extends Exception {

    public TerminalCalculatorInvalidArgumentException(String errorMessage) {
        super(errorMessage);
    }

}
