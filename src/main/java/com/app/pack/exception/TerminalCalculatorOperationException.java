package com.app.pack.exception;

/**
 * Created by jayakrishnansomasekharannair on 6/26/18.
 */

/**
 * TerminalCalculatorOperationException is thrown when an inalid operation is encountered.
 */
public class TerminalCalculatorOperationException extends Exception {

    public TerminalCalculatorOperationException(String errorMessage) {
        super(errorMessage);
    }

}
