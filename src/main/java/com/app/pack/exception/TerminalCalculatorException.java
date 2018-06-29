package com.app.pack.exception;

/**
 * Created by jayakrishnansomasekharannair on 6/26/18.
 */

/**
 * Exception class : TerminalCalculatorException
 * - final expression thrown out of terminal-calculator program.
 * - All expressions will be casted to this expression in TerminalCalculatorUtils.handleException().
 */
public class TerminalCalculatorException extends Exception {

    public TerminalCalculatorException(String errorMessage)
    {
        super(errorMessage);
    }

}
