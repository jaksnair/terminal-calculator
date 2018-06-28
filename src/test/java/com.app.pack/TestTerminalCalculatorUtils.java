package com.app.pack;

import com.app.pack.exception.TerminalCalculatorException;
import com.app.pack.exception.TerminalCalculatorExpressionFormatErrorCode;
import com.app.pack.exception.TerminalCalculatorInvalidArgumentException;
import com.app.pack.exception.TerminalCalculatorExpressionFormatException;
import com.app.pack.exception.TerminalCalculatorOperationException;
import com.app.pack.exception.TerminalCalculatorResultThresholdException;
import com.app.pack.util.TerminalCalculatorUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by jayakrishnansomasekharannair on 6/27/18.
 */

public class TestTerminalCalculatorUtils {

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
                new TerminalCalculatorInvalidArgumentException("Found empty input"));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                "Found empty input - ExceptionCategory : TerminalCalculatorInvalidArgumentException");
    }

    @Test
    public void testHandleExceptionTerminalCalculatorResultThresholdException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new TerminalCalculatorResultThresholdException(" result exceeded max threshold, "+Integer.MAX_VALUE));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                " result exceeded max threshold, "+Integer.MAX_VALUE+" - ExceptionCategory : TerminalCalculatorResultThresholdException");
    }
    @Test
    public void testHandleExceptionTerminalCalculatorInvalidOperationException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new TerminalCalculatorOperationException("Operation not permitted : adds"));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                "Operation not permitted : adds - ExceptionCategory : TerminalCalculatorOperationException");
    }
    @Test
    public void testHandleExceptionTerminalCalculatorUnbalancedExpressionException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new TerminalCalculatorExpressionFormatException("parenthesis count does not match",
                        TerminalCalculatorExpressionFormatErrorCode.UNBALANCED_PARENTHESIS));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                "parenthesis count does not match - ExceptionCategory : TerminalCalculatorExpressionFormatException");
    }

    @Test
    public void testHandleExceptionNullPointerExpressionException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new NullPointerException("null"));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                "Division by Zero Encountered, check the arguments in Division Expression - " +
                        "ExceptionCategory : TerminalCalculatorExpressionFormatException");
    }

}
