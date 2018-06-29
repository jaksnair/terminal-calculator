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

/**
 * Test class to unit test all methods of TerminalCalculatorProcessor.
 */

public class TestTerminalCalculatorProcessor implements TerminalCalculatorConstants {

    private static TerminalCalculatorProcessor terminalCalculatorProcessor = null;

    /**
     * Before executing all test cases, create the TerminalCalculatorProcessor instance.
     */
    @BeforeAll
    public static void setup() {
        terminalCalculatorProcessor = TerminalCalculatorProcessor.getInstance();
    }

    /**
     * Before Each test cases, nothing specific to be executed.
     */
    @BeforeEach
    public void setupBeforeEach() {

    }

    /**
     * CASE : To evaluate a null expression
     */
    @Test
    public void testEvaluateWhenInputExpressionIsNull() {

        final String inputExpression = null;
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EMPTY_INPUT_FOUND_MESSAGE+EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorInvalidArgumentException");

    }

    /**
     * CASE : To evaluate a empty expression
     */
    @Test
    public void testEvaluateWhenInputExpressionIsEmpty() {

        final String inputExpression = "";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                EMPTY_INPUT_FOUND_MESSAGE+EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorInvalidArgumentException");

    }

    /**
     * CASE : To evaluate a an expression with unbalanced parenthesis
     */
    @Test
    public void testEvaluateWhenInputExpressionIsNotHavingBalancedParenthesis() {

        final String inputExpression = "add(1, 2";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                PARENTHESIS_COUNT_DOES_NOT_MATCH_MESSAGE+EXCEPTION_CATEGORY_MESSAGE+
                        "TerminalCalculatorExpressionFormatException");

    }

    /**
     * CASE : To evaluate a an expression with unexpected delimiter format
     */
    @Test
    public void testEvaluateWhenInputExpressionIsNotHavingExpectedDelimiterFormat() {

        final String inputExpression = "add(1,2)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                DELIMITER_PATTERN_IS_NOT_AS_EXPECTED_MESSAGE+EXCEPTION_CATEGORY_MESSAGE+
                        "TerminalCalculatorExpressionFormatException");

    }

    /**
     * CASE : To evaluate a an expression with an invalid operation
     */
    @Test
    public void testEvaluateWhenInputExpressionHavingInvalidOperation() {

        final String inputExpression = "adds(1, 2)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                OPERATION_NOT_PERMITTED_MESSAGE+"adds"+EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorOperationException");

    }

    /**
     * CASE : To evaluate a valid add expression
     *
     *  @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateValidAddExpression() throws TerminalCalculatorException {

        final String inputExpression = "add(1, 2)";
        assertEquals(3, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    /**
     * CASE : To evaluate a valid sub expression
     */
    @Test
    public void testEvaluateValidSubExpression() throws TerminalCalculatorException {

        final String inputExpression = "sub(10, 2)";
        assertEquals(8, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    /**
     * CASE : To evaluate a valid multiplication expression
     *
     *  @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateValidMultiplicationExpression() throws TerminalCalculatorException {

        final String inputExpression = "mult(1, 2)";
        assertEquals(2, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    /**
     * CASE : To evaluate a valid division expression
     *
     *  @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateValidDivisionExpression() throws TerminalCalculatorException {

        final String inputExpression = "div(2, 1)";
        assertEquals(2, terminalCalculatorProcessor.evaluate(inputExpression));

    }


    /**
     * CASE : To evaluate a valid add and mult expression
     *
     *  @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateValidAddAndMultExpression() throws TerminalCalculatorException {

        final String inputExpression = "add(1, mult(2, 3))";
        assertEquals(7, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    /**
     * CASE : To evaluate a valid add+mult+div expression
     *
     *  @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateValidAddMultDivExpression() throws TerminalCalculatorException {

        final String inputExpression = "mult(add(2, 2), div(9, 3))";
        assertEquals(12, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    /**
     * CASE : To evaluate a valid let expression
     *
     *  @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateValidLetExpression() throws TerminalCalculatorException {

        final String inputExpression = "let(a, 5, add(a, a))";
        assertEquals(10, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    /**
     * CASE : To evaluate a valid let+mult+add expression
     *
     *  @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateValidLetMultAndAddExpression() throws TerminalCalculatorException {

        final String inputExpression = "let(a, 5, let(b, mult(a, 10), add(b, a)))";
        assertEquals(55, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    /**
     * CASE : To evaluate a complex let expression
     *
     *  @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateValidComplexLetExpression() throws TerminalCalculatorException {

        final String inputExpression = "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))";
        assertEquals(40, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    /**
     * CASE : To evaluate a complex let expression with variables as "aA"(2 chars)
     *
     *  @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateValidComplexLetVariableFormatExpression() throws TerminalCalculatorException {

        final String inputExpression = "let(aA, 5, add(aA, aA))";
        assertEquals(10, terminalCalculatorProcessor.evaluate(inputExpression));

    }

    /**
     *
     * CASE : To evaluate a let expression with unexpected variable "a1"(alphabet and a numeric)
     *
     * @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateUnexpectedLetVariableFormatExpression() throws TerminalCalculatorException {

        final String inputExpression = "let(a1, 5, add(a, a))";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                UNEXPECTED_LET_VARIABLE_FORMAT_MESSAGE + "a1" +
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorInvalidArgumentException");

    }

    /**
     * CASE : To evaluate a let expression with a different variable in use in the expression.
     *
     * @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateUnexpectedLetVariableFoundInExpression() throws TerminalCalculatorException {

        final String inputExpression = "let(a, 5, add(b, b))";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                UNEXPECTED_LET_VARIABLE_FOUND_MESSAGE + "b" +
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorInvalidArgumentException");

    }

    /**
     * CASE : To evaluate a let expression whose result exceeds maximum threshold value.
     *
     * @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateExpressionResultExceedsMaximumThreshold() throws TerminalCalculatorException {

        final String inputExpression = "mult("+Integer.MAX_VALUE+", 2)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE + Integer.MAX_VALUE +
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorResultThresholdException");

    }

    /**
     *
     * CASE : To evaluate a let expression whose result is less than minimum threshold value.
     *
     * @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateExpressionResultExceedsMinimumThreshold() throws TerminalCalculatorException {

        final String inputExpression = "mult("+Integer.MIN_VALUE+", 2)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                RESULT_LESSER_THAN_MINIMUM_THRESHOLD_MESSAGE + Integer.MIN_VALUE +
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorResultThresholdException");

    }

    /**
     * CASE : To evaluate a let expression in which division by zero is encountered.
     *
     * @throws TerminalCalculatorException
     */
    @Test
    public void testEvaluateExpressionDivisionByZeroCase() throws TerminalCalculatorException {

        final String inputExpression = "div(1, 0)";
        Executable closureContainingCodeToTest = ()-> terminalCalculatorProcessor.evaluate(inputExpression);
        assertThrows(TerminalCalculatorException.class,closureContainingCodeToTest,
                DIVISION_BY_ZERO_ENCOUNTERED_MESSAGE + Integer.MAX_VALUE +
                        EXCEPTION_CATEGORY_MESSAGE+"TerminalCalculatorInvalidArgumentException");

    }
}
