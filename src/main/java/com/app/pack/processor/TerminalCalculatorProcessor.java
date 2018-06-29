package com.app.pack.processor;

import com.app.pack.constants.Operations;
import com.app.pack.exception.TerminalCalculatorExpressionFormatErrorCode;
import com.app.pack.exception.TerminalCalculatorExpressionFormatException;
import com.app.pack.exception.TerminalCalculatorOperationException;
import com.app.pack.log.TerminalCalculatorLogManager;
import com.app.pack.constants.TerminalCalculatorConstants;
import com.app.pack.util.TerminalCalculatorUtils;
import com.app.pack.exception.TerminalCalculatorException;
import com.app.pack.exception.TerminalCalculatorInvalidArgumentException;
import com.app.pack.exception.TerminalCalculatorResultThresholdException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jayakrishnansomasekharannair on 6/26/18.
 */

public class TerminalCalculatorProcessor implements TerminalCalculatorConstants {

    private static Logger LOGGER = LogManager.getLogger(TerminalCalculatorProcessor.class.getName());

    private static Map<String,String> VARIABLE_VALUE_MAP = new HashMap<String,String>();

    private static TerminalCalculatorProcessor INSTANCE = new TerminalCalculatorProcessor();

    private TerminalCalculatorProcessor(){

    }

    public static TerminalCalculatorProcessor getInstance() {
        TerminalCalculatorLogManager.setLogLevel();
        return INSTANCE;
    }

    public int evaluate(final String inputExpression) throws TerminalCalculatorException{

        LOGGER.traceEntry(this.getClass().getName()+" : evaluate");

        long result = 0L;

        try {

            LOGGER.info("Received Expression is  : "+ inputExpression);

            validateExpression(inputExpression);
            result = evaluateExpression(inputExpression);
            validateExpressionResult(result);

        } catch (TerminalCalculatorInvalidArgumentException | TerminalCalculatorExpressionFormatException
                |TerminalCalculatorOperationException | TerminalCalculatorResultThresholdException exception) {

            LOGGER.error("Exception occurred " + exception.getMessage());
            TerminalCalculatorUtils.handleException(exception);
        }

        LOGGER.traceExit(this.getClass().getName()+" : evaluate");

        LOGGER.info("Evaluated Result  : "+ result);

        return (int)result;
    }

    private void validateExpression(final String inputExpression) throws TerminalCalculatorInvalidArgumentException,
            TerminalCalculatorExpressionFormatException {

        LOGGER.info("Validating Expression  : " + inputExpression);

        if(TerminalCalculatorUtils.checkNullOrEmpty(inputExpression)){
            LOGGER.fatal("Expression is either empty or null ");
            throw new TerminalCalculatorInvalidArgumentException(EMPTY_INPUT_FOUND_MESSAGE);
        }

        checkIfExpressionHasBalancedParenthesisAndIsWellFormed(inputExpression);

        LOGGER.info("Expression passed validation ");

    }

    private long evaluateExpression(final String inputExpression) throws TerminalCalculatorOperationException,
            TerminalCalculatorInvalidArgumentException {

        long result = 0;

        LOGGER.traceEntry(this.getClass().getName()+" : evaluateExpression");

        String operation = identifyOperation(inputExpression);

        LOGGER.debug("Evaluating expression : " + inputExpression);
        LOGGER.debug("Operation requested is : " + operation);

        if(!validOperation(operation)){
            LOGGER.error("Invalid Operation : " + operation);
            LOGGER.warn("Suspending Operation.");
            throw new TerminalCalculatorOperationException(OPERATION_NOT_PERMITTED_MESSAGE + operation);
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

            String variable = inputExpression.substring(offset, inputExpression.indexOf(TERMINAL_CALCULATOR_DELIMITER_COMMA));
            offset = inputExpression.indexOf(TERMINAL_CALCULATOR_DELIMITER_COMMA)+1;

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

        LOGGER.traceExit(this.getClass().getName()+" : evaluateExpression");

        return result;
    }

    
    private void checkIfExpressionHasBalancedParenthesisAndIsWellFormed(final String expression)
            throws TerminalCalculatorExpressionFormatException {

        int parenthesisCount = 0;
        int index = 0;

        while(index < expression.length()) {
            if (expression.charAt(index) == TERMINAL_CALCULATOR_LEFT_PARENTHESIS) {
                parenthesisCount++;
            }
            if (expression.charAt(index) == TERMINAL_CALCULATOR_RIGHT_PARENTHESIS) {
                parenthesisCount--;
            }

            if (index < expression.length()-1 && expression.charAt(index) == TERMINAL_CALCULATOR_DELIMITER_COMMA){
                if(expression.charAt(index+1) != TERMINAL_CALCULATOR_DELIMITER_SPACE) {
                    LOGGER.fatal("Delimiters in expression are not well formed ");
                    throw new TerminalCalculatorExpressionFormatException(DELIMITER_PATTERN_IS_NOT_AS_EXPECTED_MESSAGE,
                            TerminalCalculatorExpressionFormatErrorCode.MALFORMED_DELIMITERS);
                }
            }

            index++;
        }

        if(parenthesisCount!=0){
            LOGGER.fatal("Parenthesis in expression are not balanced ");
            throw new TerminalCalculatorExpressionFormatException(PARENTHESIS_COUNT_DOES_NOT_MATCH_MESSAGE,
                    TerminalCalculatorExpressionFormatErrorCode.UNBALANCED_PARENTHESIS);
        }

    }

    private String identifyOperation(final String expression) {

        LOGGER.traceEntry(this.getClass().getName()+" : identifyOperation");

        StringBuilder operation = new StringBuilder();
        int index=0;
        final int indexOfLeftParenthesis = expression.indexOf(TERMINAL_CALCULATOR_LEFT_PARENTHESIS);
        while(index <  indexOfLeftParenthesis){
            operation.append(expression.charAt(index++));
        }

        if(LOGGER.isTraceEnabled()){
            LOGGER.traceExit(this.getClass().getName()+" : identifyOperation");
        }

        return operation.toString();
    }

    private int findIndexOfDelimiter(final String expression) {

        if(LOGGER.isTraceEnabled()){
            LOGGER.traceEntry(this.getClass().getName()+" : findIndexOfDelimiter");
        }

        int index=0, delimiterIndex=0;
        boolean parenthesisAreBalanced =  true;
        while(index < expression.length()){
            if(expression.charAt(index)== TERMINAL_CALCULATOR_LEFT_PARENTHESIS){
                parenthesisAreBalanced = false;
            } else if(expression.charAt(index)== TERMINAL_CALCULATOR_RIGHT_PARENTHESIS){
                parenthesisAreBalanced = true;
            } else if(parenthesisAreBalanced && expression.charAt(index)==TERMINAL_CALCULATOR_DELIMITER_COMMA){
                delimiterIndex = index;
                break;
            }
            index++;
        }

        LOGGER.traceExit(this.getClass().getName()+" : findIndexOfDelimiter");

        return delimiterIndex;
    }

    private boolean checkIfOperandIsANumber (final String operandNotSureIfExpression) {

        return TerminalCalculatorUtils.isANumber(operandNotSureIfExpression);

    }

    private boolean validOperation (final String operation) {
        return Arrays.stream(Operations.values())
                .filter(op -> op.getName().equals(operation)).findFirst().isPresent();
    }

    private long performOperation( final String leftOperand, final  String rightOperand, final String operation)
            throws TerminalCalculatorOperationException, TerminalCalculatorInvalidArgumentException {

        long result = 0L;

        LOGGER.traceEntry(this.getClass().getName()+" : checkIfExpressionHasBalancedParenthesisAndIsWellFormed");

        switch(operation) {
            case TERMINAL_CALCULATOR_ADD_OPERATION :
                result = Long.parseLong(leftOperand.trim())+Long.parseLong(rightOperand.trim());
                break;
            case TERMINAL_CALCULATOR_SUB_OPERATION :
                result = Long.parseLong(leftOperand.trim())-Long.parseLong(rightOperand.trim());
                break;
            case TERMINAL_CALCULATOR_MULT_OPERATION :
                result = Long.parseLong(leftOperand.trim())*Long.parseLong(rightOperand.trim());
                break;
            case TERMINAL_CALCULATOR_DIV_OPERATION :
                if(Long.parseLong(rightOperand.trim())==0){
                    LOGGER.error(" Right operand obtained is zero, cannot continue to divide ");
                    throw new TerminalCalculatorInvalidArgumentException(DIVISION_BY_ZERO_ENCOUNTERED_MESSAGE);
                }
                result = Long.parseLong(leftOperand.trim())/Long.parseLong(rightOperand.trim());
                break;
            default :
                throw new TerminalCalculatorOperationException("Operation not permitted : " + operation);
        }

        LOGGER.traceExit(this.getClass().getName()+" : checkIfExpressionHasBalancedParenthesisAndIsWellFormed");

        return result;

    }

    private void validateExpressionResult (final long result) throws TerminalCalculatorResultThresholdException {

        if(result > Integer.MAX_VALUE) {
            LOGGER.warn(RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE+ "Result obtained : " + result);
            throw new TerminalCalculatorResultThresholdException(RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE
                    + Integer.MAX_VALUE);
        } else if (result < Integer.MIN_VALUE) {
            LOGGER.warn(RESULT_LESSER_THAN_MINIMUM_THRESHOLD_MESSAGE+ "Result obtained : " + result);
            throw new TerminalCalculatorResultThresholdException(RESULT_LESSER_THAN_MINIMUM_THRESHOLD_MESSAGE
                    + Integer.MIN_VALUE);
        }
    }

    private String substituteIfItsAVariableFromLetExpression( String variable)
            throws TerminalCalculatorInvalidArgumentException {


        LOGGER.traceEntry(this.getClass().getName()+" : substituteIfItsAVariableFromLetExpression");

        Set<String> variablesAvailable  = VARIABLE_VALUE_MAP.keySet();
        if(variablesAvailable.contains(variable)){
            variable = VARIABLE_VALUE_MAP.get(variable);
        } else {

            try{
                checkIfExpressionHasBalancedParenthesisAndIsWellFormed(variable);
            } catch (TerminalCalculatorExpressionFormatException e) {
                LOGGER.error(UNEXPECTED_LET_VARIABLE_FOUND_MESSAGE+ variable);
                throw new TerminalCalculatorInvalidArgumentException(UNEXPECTED_LET_VARIABLE_FOUND_MESSAGE);
            }

        }

        LOGGER.traceExit(this.getClass().getName()+" : substituteIfItsAVariableFromLetExpression");

        return variable;

    }

    private void validateVariableAndLoadVariableValueMap(final String variable, final String value)
            throws TerminalCalculatorInvalidArgumentException {

        if(variable.matches("[a-zA-Z]+")) {
            VARIABLE_VALUE_MAP.put(variable, value);
        } else {
            throw new TerminalCalculatorInvalidArgumentException(UNEXPECTED_LET_VARIABLE_FORMAT_MESSAGE);
        }

    }
}


