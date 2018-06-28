package com.app.pack;

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
public class TestTerminalCalculatorProcessor {

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
                "Found empty input - ExceptionCategory : TerminalCalculatorInvalidArgumentException");

    }

    @Test
    public void testEvaluateWhenInputExpressionIsEmpty() {

        final String inputExpression = "";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                "Found empty input - ExceptionCategory : TerminalCalculatorInvalidArgumentException");

    }

    @Test
    public void testEvaluateWhenInputExpressionIsNotHavingBalancedParenthesis() {

        final String inputExpression = "add(1, 2";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                "parenthesis count does not match - ExceptionCategory : TerminalCalculatorExpressionFormatException");

    }


    @Test
    public void testEvaluateWhenInputExpressionIsNotHavingExpectedDelimiterFormat() {

        final String inputExpression = "add(1,2)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                "delimiters pattern is not as expected - ExceptionCategory : TerminalCalculatorExpressionFormatException");

    }

    @Test
    public void testEvaluateWhenInputExpressionHavingInvalidOperation() {

        final String inputExpression = "adds(1, 2)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                "Operation not permitted : adds - ExceptionCategory : TerminalCalculatorOperationException");

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
                "result exceeded maximum threshold, " + Integer.MAX_VALUE +
                        " - ExceptionCategory : TerminalCalculatorResultThresholdException");

    }


    @Test
    public void testEvaluateExpressionResultExceedsMinimumThreshold() throws TerminalCalculatorException {

        final String inputExpression = "mult("+Integer.MIN_VALUE+", 2)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                "result lesser than minimum threshold, " + Integer.MIN_VALUE +
                        " - ExceptionCategory : TerminalCalculatorResultThresholdException");

    }


    @Test
    public void testEvaluateExpressionDivisionByZeroCase() throws TerminalCalculatorException {

        final String inputExpression = "div(1, 0)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                "result exceeded maximum threshold, " + Integer.MAX_VALUE +
                        " - ExceptionCategory : TerminalCalculatorInvalidArgumentException");

    }
}
