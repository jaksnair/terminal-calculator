package com.app.pack.exception;

/**
 * Created by jayakrishnansomasekharannair on 6/26/18.
 */

/**
 * TerminalCalculatorExpressionFormatException is thrown when the expression does not meet the expected format.
 */
public class TerminalCalculatorExpressionFormatException extends Exception {

    private TerminalCalculatorExpressionFormatErrorCode errorCode;

    public TerminalCalculatorExpressionFormatException(String errorMessage,
            TerminalCalculatorExpressionFormatErrorCode errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }


}
