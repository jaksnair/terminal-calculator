package com.app.pack.processor;

import com.app.pack.constants.TerminalCalculatorConstants;
import com.app.pack.exception.TerminalCalculatorException;
import com.app.pack.exception.TerminalCalculatorExpressionFormatErrorCode;
import com.app.pack.exception.TerminalCalculatorExpressionFormatException;
import com.app.pack.exception.TerminalCalculatorInvalidArgumentException;
import com.app.pack.log.TerminalCalculatorLogManager;
import com.app.pack.constants.Operations;
import com.app.pack.exception.TerminalCalculatorOperationException;
import com.app.pack.exception.TerminalCalculatorResultThresholdException;
import com.app.pack.util.TerminalCalculatorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Created by jayakrishnansomasekharannair on 6/26/18.
 */


/**
 *
 * Class deals with the terminal-calulator processing of the input expression.
 * 1. It evaluates expressions in a very simple integer expression language.
 * 2. Takes an input on the command line, computes the result, and prints it to the console. 
 * 3. Expression details :
 *      - Numbers : integers between Integer.MIN_VALUE and Integer.MAX_VALUE.
 *      - Variables: strings of characters, where each character is one of a-z, A-Z.
 *      - Arithmetic functions: add, sub, mult, div, each taking two arbitrary expressions as arguments. 
 *      - A “let” operator for assigning values to variables:
 *                  let(<variable name>, <value expression>, <expression where variable is used>)
 *                  - <expression where variable is used> : either another let expression or normal expression,
 *                  please visit TestTerminalCalculatorProcessor.java for sample test cases
 *
 */
public class TerminalCalculatorProcessor {

    /**
     * LOGGER instance
     */
    private static Logger LOGGER = LogManager.getLogger(TerminalCalculatorProcessor.class.getName());

    /**
     * Storage to hold the variable and its value used in let expressions.
     */
    private static Map<String,String> VARIABLE_VALUE_MAP = new HashMap<String,String>();

    /**
     * INSTANCE of TerminalCalculatorProcessor
     */
    private static TerminalCalculatorProcessor INSTANCE = new TerminalCalculatorProcessor();

    /**
     * private default constructor
     */
    private TerminalCalculatorProcessor(){

    }

    /**
     * Method will provide instance of TerminalCalculatorProcessor.
     * It sets the desired log level supplied as command line argument.
     *
     * @return INSTANCE
     */
    public static TerminalCalculatorProcessor getInstance() {
        TerminalCalculatorLogManager.setLogLevel();
        return INSTANCE;
    }

    /**
     *
     *  Method evaluates expression
     *  - validates input expression
     *  - evaluates expression and stores it to result
     *  - validates expression result for threshold violation.
     *
     * @param inputExpression
     * @return result
     * @throws TerminalCalculatorException
     */
    public int evaluate(final String inputExpression) throws TerminalCalculatorException{

        long result = 0L;

        try {

            LOGGER.info("Received Expression is  : "+ inputExpression);

            validateExpression(inputExpression);
            result = evaluateExpression(inputExpression);
            validateExpressionResult(result);

        } catch (TerminalCalculatorInvalidArgumentException | TerminalCalculatorExpressionFormatException
                |TerminalCalculatorOperationException | TerminalCalculatorResultThresholdException exception) {

            LOGGER.error(TerminalCalculatorConstants.EXCEPTION_OCCURRED_MESSAGE + exception.getMessage(), exception);
            TerminalCalculatorUtils.handleException(exception);
        }

        LOGGER.info("Evaluated Result  : "+ result);

        return (int)result;
    }


    /**
     *
     * inputExpression is validated whether.
     * - it is not null or empty,
     * - it has balanced parenthesis and the delimiters are well formed
     *
     * @param inputExpression
     * @throws TerminalCalculatorInvalidArgumentException
     * @throws TerminalCalculatorExpressionFormatException
     */
    private void validateExpression(final String inputExpression) throws TerminalCalculatorInvalidArgumentException,
            TerminalCalculatorExpressionFormatException {

        LOGGER.info("Validating Expression  : " + inputExpression);

        if(TerminalCalculatorUtils.checkNullOrEmpty(inputExpression)){
            LOGGER.fatal("Expression is either empty or null ");
            throw new TerminalCalculatorInvalidArgumentException(TerminalCalculatorConstants.EMPTY_INPUT_FOUND_MESSAGE);
        }

        checkIfExpressionHasBalancedParenthesisAndIsWellFormed(inputExpression);

        LOGGER.info("Expression passed validation ");

    }

    /**
     * Method evaluates expression recursively.
     * - It treats Let expression different from expression with other operations.
     * - If it is Let expression, method resolves the variables and then does the operation involved.
     *
     * @param inputExpression
     * @return
     * @throws TerminalCalculatorOperationException
     * @throws TerminalCalculatorInvalidArgumentException
     */
    private long evaluateExpression(final String inputExpression) throws TerminalCalculatorOperationException,
            TerminalCalculatorInvalidArgumentException {

        long result = 0;

        String operation = identifyOperation(inputExpression);

        LOGGER.debug("Evaluating expression : " + inputExpression);
        LOGGER.debug("Operation requested is : " + operation);

        if(!validOperation(operation)){
            LOGGER.error("Invalid Operation : " + operation);
            LOGGER.warn("Suspending Operation as either wrong operation or expression format found.");
            throw new TerminalCalculatorOperationException(TerminalCalculatorConstants.OPERATION_NOT_PERMITTED_MESSAGE + operation);
        }
        int offset = operation.length()+1;

        if(!operation.equals(Operations.LET.getName())) {

            int delimiterIndex = findIndexOfDelimiter(inputExpression.substring(offset));
            delimiterIndex = delimiterIndex + offset;
            String leftOperand = inputExpression.substring(offset, delimiterIndex);
            leftOperand = substituteIfItsAVariableFromLetExpression(leftOperand);
            String rightOperand = inputExpression.substring(delimiterIndex + 2, inputExpression.length() - 1);
            rightOperand = substituteIfItsAVariableFromLetExpression(rightOperand);


            if (!checkIfOperandIsANumber(leftOperand)) {
                leftOperand = String.valueOf(evaluateExpression(leftOperand));
            }

            if (!checkIfOperandIsANumber(rightOperand)) {
                rightOperand = String.valueOf(evaluateExpression(rightOperand));
            }

            result = performOperation(leftOperand, rightOperand, operation);

        } else {

            String variable = inputExpression.substring(offset, inputExpression.indexOf(TerminalCalculatorConstants.TERMINAL_CALCULATOR_DELIMITER_COMMA));
            offset = inputExpression.indexOf(TerminalCalculatorConstants.TERMINAL_CALCULATOR_DELIMITER_COMMA)+1;

            int delimiterIndex = findIndexOfDelimiter(inputExpression.substring(offset));
            delimiterIndex = delimiterIndex + offset;

            String valueExpression = inputExpression.substring(offset+1, delimiterIndex);

            if (checkIfOperandIsANumber(valueExpression)) {

                LOGGER.debug("Variable  : " + variable + " Value : " + valueExpression);

                validateVariableAndLoadVariableValueMap(variable,valueExpression);
            } else {

                LOGGER.debug("Need to resolve value from : " + valueExpression);

                validateVariableAndLoadVariableValueMap(variable,String.valueOf(evaluateExpression(valueExpression)));
            }

            String expressionInWhichVariableIsUsed = inputExpression.substring(delimiterIndex + 2, inputExpression.length() - 1);

            result = evaluateExpression(expressionInWhichVariableIsUsed);

        }

        return result;
    }


    /**
     * Method ensures if the expression has balanced parenthesis & the delimiters are well formed (space follows comma).
     * @param expression
     * @throws TerminalCalculatorExpressionFormatException
     */
    private void checkIfExpressionHasBalancedParenthesisAndIsWellFormed(final String expression)
            throws TerminalCalculatorExpressionFormatException {

        int parenthesisCount = 0;
        int index = 0;

        while(index < expression.length()) {
            if (expression.charAt(index) == TerminalCalculatorConstants.TERMINAL_CALCULATOR_LEFT_PARENTHESIS) {
                parenthesisCount++;
            }
            if (expression.charAt(index) == TerminalCalculatorConstants.TERMINAL_CALCULATOR_RIGHT_PARENTHESIS) {
                parenthesisCount--;
            }

            if (index < expression.length()-1 && expression.charAt(index) == TerminalCalculatorConstants.TERMINAL_CALCULATOR_DELIMITER_COMMA){
                if(expression.charAt(index+1) != TerminalCalculatorConstants.TERMINAL_CALCULATOR_DELIMITER_SPACE) {
                    LOGGER.fatal("Delimiters in expression are not well formed ");
                    throw new TerminalCalculatorExpressionFormatException(TerminalCalculatorConstants.DELIMITER_PATTERN_IS_NOT_AS_EXPECTED_MESSAGE,
                            TerminalCalculatorExpressionFormatErrorCode.MALFORMED_DELIMITERS);
                }
            }

            index++;
        }

        if(parenthesisCount!=0){
            LOGGER.fatal("Parenthesis in expression are not balanced ");
            throw new TerminalCalculatorExpressionFormatException(TerminalCalculatorConstants.PARENTHESIS_COUNT_DOES_NOT_MATCH_MESSAGE,
                    TerminalCalculatorExpressionFormatErrorCode.UNBALANCED_PARENTHESIS);
        }

    }

    /**
     * Method identifies the operation involved.
     * @param expression
     * @return
     */
    private String identifyOperation(final String expression) {

        StringBuilder operation = new StringBuilder();
        int index=0;
        final int indexOfLeftParenthesis = expression.indexOf(TerminalCalculatorConstants.TERMINAL_CALCULATOR_LEFT_PARENTHESIS);
        while(index <  indexOfLeftParenthesis){
            operation.append(expression.charAt(index++));
        }

        return operation.toString();
    }

    /**
     * Method finds the index of delimiter in the expression.
     * @param expression
     * @return
     */
    private int findIndexOfDelimiter(final String expression) {

        int index=0, delimiterIndex=0;
        boolean parenthesisAreBalanced =  true;
        while(index < expression.length()){
            if(expression.charAt(index)== TerminalCalculatorConstants.TERMINAL_CALCULATOR_LEFT_PARENTHESIS){
                parenthesisAreBalanced = false;
            } else if(expression.charAt(index)== TerminalCalculatorConstants.TERMINAL_CALCULATOR_RIGHT_PARENTHESIS){
                parenthesisAreBalanced = true;
            } else if(parenthesisAreBalanced && expression.charAt(index)== TerminalCalculatorConstants.TERMINAL_CALCULATOR_DELIMITER_COMMA){
                delimiterIndex = index;
                break;
            }
            index++;
        }

        return delimiterIndex;
    }

    /**
     * It ensures whether the input is a number or not.
     *
     * @param operandNotSureIfExpression
     * @return
     */
    private boolean checkIfOperandIsANumber (final String operandNotSureIfExpression) {

        return TerminalCalculatorUtils.isANumber(operandNotSureIfExpression);

    }

    /**
     *
     * Compares the given operation against the available inputs.
     *
     * @param operation
     * @return
     */
    private boolean validOperation (final String operation) {
        return Arrays.stream(Operations.values())
                .filter(op -> op.getName().equals(operation)).findFirst().isPresent();
    }

    /**
     * Method performs the given operation with the operands and returns the result.
     *
     * @param leftOperand
     * @param rightOperand
     * @param operation
     * @return
     * @throws TerminalCalculatorOperationException
     * @throws TerminalCalculatorInvalidArgumentException
     */
    private long performOperation( final String leftOperand, final  String rightOperand, final String operation)
            throws TerminalCalculatorOperationException, TerminalCalculatorInvalidArgumentException {

        long result = 0L;

        switch(operation) {
            case TerminalCalculatorConstants.TERMINAL_CALCULATOR_ADD_OPERATION :
                result = Long.parseLong(leftOperand.trim())+Long.parseLong(rightOperand.trim());
                break;
            case TerminalCalculatorConstants.TERMINAL_CALCULATOR_SUB_OPERATION :
                result = Long.parseLong(leftOperand.trim())-Long.parseLong(rightOperand.trim());
                break;
            case TerminalCalculatorConstants.TERMINAL_CALCULATOR_MULT_OPERATION :
                result = Long.parseLong(leftOperand.trim())*Long.parseLong(rightOperand.trim());
                break;
            case TerminalCalculatorConstants.TERMINAL_CALCULATOR_DIV_OPERATION :
                if(Long.parseLong(rightOperand.trim())==0){
                    LOGGER.error(" Right operand obtained is zero, cannot continue to divide ");
                    throw new TerminalCalculatorInvalidArgumentException(TerminalCalculatorConstants.DIVISION_BY_ZERO_ENCOUNTERED_MESSAGE);
                }
                result = Long.parseLong(leftOperand.trim())/Long.parseLong(rightOperand.trim());
                break;
            default :
                throw new TerminalCalculatorOperationException("Operation not permitted : " + operation);
        }

        return result;

    }


    /**
     * Compares result against the threshold value
     *
     * Integer.MIN_VALUE < result < Integer.MAX_VALUE
     *
     * @param result
     * @throws TerminalCalculatorResultThresholdException
     */
    private void validateExpressionResult (final long result) throws TerminalCalculatorResultThresholdException {

        if(result > Integer.MAX_VALUE) {
            LOGGER.warn(TerminalCalculatorConstants.RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE+ "Result obtained : " + result);
            throw new TerminalCalculatorResultThresholdException(TerminalCalculatorConstants.RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE
                    + Integer.MAX_VALUE);
        } else if (result < Integer.MIN_VALUE) {
            LOGGER.warn(TerminalCalculatorConstants.RESULT_LESSER_THAN_MINIMUM_THRESHOLD_MESSAGE+ "Result obtained : " + result);
            throw new TerminalCalculatorResultThresholdException(TerminalCalculatorConstants.RESULT_LESSER_THAN_MINIMUM_THRESHOLD_MESSAGE
                    + Integer.MIN_VALUE);
        }
    }

    /**
     *
     * Variable of a Let expression is substituted with the value stored.
     * It also ensures to throw expression if the variable is not a valid one.
     *
     * @param variable
     * @return
     * @throws TerminalCalculatorInvalidArgumentException
     */
    private String substituteIfItsAVariableFromLetExpression( String variable)
            throws TerminalCalculatorInvalidArgumentException {

        Set<String> variablesAvailable  = VARIABLE_VALUE_MAP.keySet();
        if(variablesAvailable.contains(variable)) {
            variable = VARIABLE_VALUE_MAP.get(variable);
        } else if (validateVariable(variable)) {
                LOGGER.error(TerminalCalculatorConstants.UNEXPECTED_LET_VARIABLE_FOUND_MESSAGE + variable + " . Available variables are  : "
                        + variablesAvailable.stream().collect(Collectors.joining(TerminalCalculatorConstants.TERMINAL_CALCULATOR_DELIMITER_COMMA+"")));
                throw new TerminalCalculatorInvalidArgumentException(TerminalCalculatorConstants.UNEXPECTED_LET_VARIABLE_FOUND_MESSAGE);
        }


        return variable;

    }

    /**
     *
     * It validates the variable and loads variable and value to the storage.
     *
     * @param variable
     * @param value
     * @throws TerminalCalculatorInvalidArgumentException
     */
    private void validateVariableAndLoadVariableValueMap(final String variable, final String value)
            throws TerminalCalculatorInvalidArgumentException {

        if(validateVariable(variable)) {
            VARIABLE_VALUE_MAP.put(variable, value);
        } else {
            throw new TerminalCalculatorInvalidArgumentException(TerminalCalculatorConstants.UNEXPECTED_LET_VARIABLE_FORMAT_MESSAGE + variable);
        }

    }

    /**
     * Validate the given variable
     * - strings of characters, where each character is one of a-z, A-Z
     * @param variable
     * @return
     */
    private boolean validateVariable(final String variable) {
        return variable.matches("[a-zA-Z]+");
    }
}


