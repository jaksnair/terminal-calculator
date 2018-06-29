package com.app.pack.exception;

/**
 * Created by jayakrishnansomasekharannair on 6/27/18.
 */

/**
 * This holds the TerminalCalculatorExpressionFormatException error codes.
 * 2 categories as of now,
 *      - Delimiters are not well formed
 *      - Parenthesis are not balanced.
 *
 */
public enum TerminalCalculatorExpressionFormatErrorCode {

    MALFORMED_DELIMITERS,
    UNBALANCED_PARENTHESIS

}
