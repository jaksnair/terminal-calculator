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

import static com.app.pack.constants.TerminalCalculatorConstants.EMPTY_INPUT_FOUND_MESSAGE;
import static com.app.pack.constants.TerminalCalculatorConstants.EXCEPTION_CATEGORY_MESSAGE;
import static com.app.pack.constants.TerminalCalculatorConstants.EXCEPTION_OCCURRED_MESSAGE;
import static com.app.pack.constants.TerminalCalculatorConstants.OPERATION_NOT_PERMITTED_MESSAGE;
import static com.app.pack.constants.TerminalCalculatorConstants.PARENTHESIS_COUNT_DOES_NOT_MATCH_MESSAGE;
import static com.app.pack.constants.TerminalCalculatorConstants.RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by jayakrishnansomasekharannair on 6/27/18.
 */

/**
 * Test class to unit test all methods of TerminalCalculatorUtils.
 */
public class TestTerminalCalculatorUtils  {

    /**
     * CASE : To test checkNullOrEmpty behaves correct in true case
     */
    @Test
    public void testCheckNullOrEmptyTrue() {
        String input = null;
        assertTrue(TerminalCalculatorUtils.checkNullOrEmpty(input));
    }

    /**
     * CASE : To test checkNullOrEmpty behaves correct in false case
     */
    @Test
    public void testCheckNullOrEmptyFalse() {
        String input = "notNull";
        assertFalse(TerminalCalculatorUtils.checkNullOrEmpty(input));
    }

    /**
     * CASE : To test isANumber behaves correct in true case
     */
    @Test
    public void testIsANumberTrue() {
        String input = "123";
        assertTrue(TerminalCalculatorUtils.isANumber(input));
    }

    /**
     * CASE : To test isANumber behaves correct in false case
     */
    @Test
    public void testIsANumberFalse() {
        String input = "let(a, 5, add(a, 5))";
        assertFalse(TerminalCalculatorUtils.isANumber(input));
    }


    /**
     * CASE : handleException test when casts TerminalCalculatorInvalidArgumentException
     */
    @Test
    public void testHandleExceptionTerminalCalculatorInvalidArgumentException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new TerminalCalculatorInvalidArgumentException(EMPTY_INPUT_FOUND_MESSAGE));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EXCEPTION_OCCURRED_MESSAGE+EMPTY_INPUT_FOUND_MESSAGE+EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorInvalidArgumentException");
    }

    /**
     * CASE : handleException test when casts  TerminalCalculatorResultThresholdException
     */
    @Test
    public void testHandleExceptionTerminalCalculatorResultThresholdException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new TerminalCalculatorResultThresholdException(RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE+Integer.MAX_VALUE));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EXCEPTION_OCCURRED_MESSAGE + RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE + Integer.MAX_VALUE+
                        EXCEPTION_CATEGORY_MESSAGE + "TerminalCalculatorResultThresholdException");

    }

    /**
     * CASE : handleException test when casts  TerminalCalculatorOperationException
     */
    @Test
    public void testHandleExceptionTerminalCalculatorInvalidOperationException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new TerminalCalculatorOperationException(OPERATION_NOT_PERMITTED_MESSAGE+"adds"));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EXCEPTION_OCCURRED_MESSAGE+OPERATION_NOT_PERMITTED_MESSAGE+"adds"+
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorOperationException");
    }


    /**
     * CASE : handleException test when casts  TerminalCalculatorExpressionFormatException
     */
    @Test
    public void testHandleExceptionTerminalCalculatorUnbalancedExpressionException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new TerminalCalculatorExpressionFormatException(PARENTHESIS_COUNT_DOES_NOT_MATCH_MESSAGE,
                        TerminalCalculatorExpressionFormatErrorCode.UNBALANCED_PARENTHESIS));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EXCEPTION_OCCURRED_MESSAGE+PARENTHESIS_COUNT_DOES_NOT_MATCH_MESSAGE+
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorExpressionFormatException");
    }

    /**
     * CASE : handleException test when casts NullPointerException
     */
    @Test
    public void testHandleExceptionNullPointerExpressionException() {

        Executable closureContainingCodeToTest = ()-> TerminalCalculatorUtils.handleException(
                new NullPointerException("null"));
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EXCEPTION_OCCURRED_MESSAGE+"Division by Zero Encountered, check the arguments in Division Expression" +
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorExpressionFormatException");
    }

}
