package com.app.pack;

import com.app.pack.constants.TerminalCalculatorConstants;
import com.app.pack.exception.TerminalCalculatorException;
import com.app.pack.exception.TerminalCalculatorExpressionFormatErrorCode;
import com.app.pack.exception.TerminalCalculatorInvalidArgumentException;
import com.app.pack.exception.TerminalCalculatorExpressionFormatException;
import com.app.pack.exception.TerminalCalculatorOperationException;
import com.app.pack.exception.TerminalCalculatorResultThresholdException;
import com.app.pack.util.TerminalCalculatorUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by jayakrishnansomasekharannair on 6/27/18.
 */

public class TestTerminalCalculatorUtils implements TerminalCalculatorConstants {

    @Test
    public void testCheckNullOrEmptyTrue() {
        String input = null;
        assertTrue(TerminalCalculatorUtils.checkNullOrEmpty(input));
    }

    @Test
    public void testCheckNullOrEmptyFalse() {
        String input = "notNull";
        assertFalse(TerminalCalculatorUtils.checkNullOrEmpty(input));
    }

    @Test
    public void testIsANumberTrue() {
        String input = "123";
        assertTrue(TerminalCalculatorUtils.isANumber(input));
    }

    @Test
    public void testIsANumberFalse() {
        String input = "let(a, 5, add(a, 5))";
        assertFalse(TerminalCalculatorUtils.isANumber(input));
    }


    @Test
    public void testHandleExceptionTerminalCalculatorInvalidArgumentException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new TerminalCalculatorInvalidArgumentException(EMPTY_INPUT_FOUND_MESSAGE));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EXCEPTION_OCCURRED_MESSAGE+EMPTY_INPUT_FOUND_MESSAGE+EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorInvalidArgumentException");
    }

    @Test
    public void testHandleExceptionTerminalCalculatorResultThresholdException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new TerminalCalculatorResultThresholdException(RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE+Integer.MAX_VALUE));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EXCEPTION_OCCURRED_MESSAGE + RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE + Integer.MAX_VALUE+
                        EXCEPTION_CATEGORY_MESSAGE + "TerminalCalculatorResultThresholdException");

    }

    @Test
    public void testHandleExceptionTerminalCalculatorInvalidOperationException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new TerminalCalculatorOperationException(OPERATION_NOT_PERMITTED_MESSAGE+"adds"));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EXCEPTION_OCCURRED_MESSAGE+OPERATION_NOT_PERMITTED_MESSAGE+"adds"+
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorOperationException");
    }


    @Test
    public void testHandleExceptionTerminalCalculatorUnbalancedExpressionException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new TerminalCalculatorExpressionFormatException(PARENTHESIS_COUNT_DOES_NOT_MATCH_MESSAGE,
                        TerminalCalculatorExpressionFormatErrorCode.UNBALANCED_PARENTHESIS));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EXCEPTION_OCCURRED_MESSAGE+PARENTHESIS_COUNT_DOES_NOT_MATCH_MESSAGE+
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorExpressionFormatException");
    }

    @Test
    public void testHandleExceptionNullPointerExpressionException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new NullPointerException("null"));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EXCEPTION_OCCURRED_MESSAGE+"Division by Zero Encountered, check the arguments in Division Expression" +
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorExpressionFormatException");
    }

}
