package com.app.pack;

import com.app.pack.constants.TerminalCalculatorConstants;
import com.app.pack.exception.TerminalCalculatorException;
import com.app.pack.processor.TerminalCalculatorProcessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by jayakrishnansomasekharannair on 6/27/18.
 */
public class TestTerminalCalculatorProcessor implements TerminalCalculatorConstants {

    private static TerminalCalculatorProcessor terminalCalculatorProcessor = null;

    @BeforeAll
    public static void setup() {
        terminalCalculatorProcessor = TerminalCalculatorProcessor.getInstance();
    }

    @BeforeEach
    public void setupBeforeEach() {

    }

    @Test
    public void testEvaluateWhenInputExpressionIsNull() {

        final String inputExpression = null;
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EMPTY_INPUT_FOUND_MESSAGE+EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorInvalidArgumentException");

    }

    @Test
    public void testEvaluateWhenInputExpressionIsEmpty() {

        final String inputExpression = "";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EMPTY_INPUT_FOUND_MESSAGE+EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorInvalidArgumentException");

    }

    @Test
    public void testEvaluateWhenInputExpressionIsNotHavingBalancedParenthesis() {

        final String inputExpression = "add(1, 2";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                PARENTHESIS_COUNT_DOES_NOT_MATCH_MESSAGE+EXCEPTION_CATEGORY_MESSAGE+
                        "TerminalCalculatorExpressionFormatException");

    }


    @Test
    public void testEvaluateWhenInputExpressionIsNotHavingExpectedDelimiterFormat() {

        final String inputExpression = "add(1,2)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                DELIMITER_PATTERN_IS_NOT_AS_EXPECTED_MESSAGE+EXCEPTION_CATEGORY_MESSAGE+
                        "TerminalCalculatorExpressionFormatException");

    }

    @Test
    public void testEvaluateWhenInputExpressionHavingInvalidOperation() {

        final String inputExpression = "adds(1, 2)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                OPERATION_NOT_PERMITTED_MESSAGE+"adds"+EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorOperationException");

    }


    @Test
    public void testEvaluateValidAddExpression() throws TerminalCalculatorException {

        final String inputExpression = "add(1, 2)";
        assertEquals(3, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    @Test
    public void testEvaluateValidSubExpression() throws TerminalCalculatorException {

        final String inputExpression = "sub(10, 2)";
        assertEquals(8, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    @Test
    public void testEvaluateValidMultiplicationExpression() throws TerminalCalculatorException {

        final String inputExpression = "mult(1, 2)";
        assertEquals(2, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    @Test
    public void testEvaluateValidDivisionExpression() throws TerminalCalculatorException {

        final String inputExpression = "div(2, 1)";
        assertEquals(2, terminalCalculatorProcessor.evaluate(inputExpression));

    }


    @Test
    public void testEvaluateValidAddAndMultExpression() throws TerminalCalculatorException {

        final String inputExpression = "add(1, mult(2, 3))";
        assertEquals(7, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    @Test
    public void testEvaluateValidAddMultDivExpression() throws TerminalCalculatorException {

        final String inputExpression = "mult(add(2, 2), div(9, 3))";
        assertEquals(12, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    @Test
    public void testEvaluateValidLetExpression() throws TerminalCalculatorException {

        final String inputExpression = "let(a, 5, add(a, a))";
        assertEquals(10, terminalCalculatorProcessor.evaluate(inputExpression));

    }


    @Test
    public void testEvaluateValidLetMultAndAddExpression() throws TerminalCalculatorException {

        final String inputExpression = "let(a, 5, let(b, mult(a, 10), add(b, a)))";
        assertEquals(55, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    @Test
    public void testEvaluateValidComplexLetExpression() throws TerminalCalculatorException {

        final String inputExpression = "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))";
        assertEquals(40, terminalCalculatorProcessor.evaluate(inputExpression));

    }


    @Test
    public void testEvaluateExpressionResultExceedsMaximumThreshold() throws TerminalCalculatorException {

        final String inputExpression = "mult("+Integer.MAX_VALUE+", 2)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE + Integer.MAX_VALUE +
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorResultThresholdException");

    }


    @Test
    public void testEvaluateExpressionResultExceedsMinimumThreshold() throws TerminalCalculatorException {

        final String inputExpression = "mult("+Integer.MIN_VALUE+", 2)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                RESULT_LESSER_THAN_MINIMUM_THRESHOLD_MESSAGE + Integer.MIN_VALUE +
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorResultThresholdException");

    }


    @Test
    public void testEvaluateExpressionDivisionByZeroCase() throws TerminalCalculatorException {

        final String inputExpression = "div(1, 0)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                DIVISION_BY_ZERO_ENCOUNTERED_MESSAGE + Integer.MAX_VALUE +
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorInvalidArgumentException");

    }
}
