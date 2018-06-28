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

        if(LOGGER.isTraceEnabled()){
            LOGGER.traceEntry(this.getClass().getName()+" : evaluate");
        }

        long result = 0L;

        try {

            LOGGER.info("Received Expression is  : "+ inputExpression);

            validateExpression(inputExpression);
            result = evaluateExpression(inputExpression);
            validateExpressionResult(result);

        } catch (TerminalCalculatorInvalidArgumentException | TerminalCalculatorExpressionFormatException
                |TerminalCalculatorOperationException | TerminalCalculatorResultThresholdException exception) {

            LOGGER.error("Exception occured " + exception.getMessage());
            TerminalCalculatorUtils.handleException(exception);
        }


        if(LOGGER.isTraceEnabled()){
            LOGGER.traceExit(this.getClass().getName()+" : evaluate");
        }

        LOGGER.info("Evaluated Result  : "+ result);

        return (int)result;
    }

    private void validateExpression(final String inputExpression) throws TerminalCalculatorInvalidArgumentException,
            TerminalCalculatorExpressionFormatException {

        LOGGER.info("Validating Expression  : " + inputExpression);

        if(TerminalCalculatorUtils.checkNullOrEmpty(inputExpression)){
            LOGGER.fatal("Expression is either empty or null ");
            throw new TerminalCalculatorInvalidArgumentException("Found empty input");
        }

        checkIfExpressionHasBalancedParenthesisAndIsWellFormed(inputExpression);

        LOGGER.info("Expression passed validation ");

    }

    private long evaluateExpression(final String inputExpression) throws TerminalCalculatorOperationException,
            TerminalCalculatorInvalidArgumentException {

        long result = 0;

        if(LOGGER.isTraceEnabled()){
            LOGGER.traceEntry(this.getClass().getName()+" : evaluateExpression");
        }

        String operation = identifyOperation(inputExpression);

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("Evaluating expression : " + inputExpression);
            LOGGER.debug("Operation requested is : " + operation);
        }


        if(!validOperation(operation)){
            LOGGER.warn("Operation requested will not be performed.");
            throw new TerminalCalculatorOperationException("Operation not permitted : " + operation);
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
                if(LOGGER.isTraceEnabled()){
                    LOGGER.debug("Variable  : " + variable + " Value : " + valueExpression);
                }
                loadVariableValueMap(variable,valueExpression);
            } else {
                if(LOGGER.isTraceEnabled()){
                    LOGGER.debug("Need to resolve value from : " + valueExpression);
                }
                loadVariableValueMap(variable,String.valueOf(evaluateExpression(valueExpression)));
            }

            String expressionInWhichVariableIsUsed = inputExpression.substring(delimiterIndex + 2, inputExpression.length() - 1);

            result = evaluateExpression(expressionInWhichVariableIsUsed);

        }

        if(LOGGER.isTraceEnabled()){
            LOGGER.traceExit(this.getClass().getName()+" : evaluateExpression");
        }


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
                    LOGGER.warn("Delimiters in expression are not well formed ");
                    throw new TerminalCalculatorExpressionFormatException("delimiters pattern is not as expected",
                            TerminalCalculatorExpressionFormatErrorCode.MALFORMED_DELIMITERS);
                }
            }

            index++;
        }

        if(parenthesisCount!=0){
            LOGGER.warn("Parenthesis in expression are not balanced ");
            throw new TerminalCalculatorExpressionFormatException("parenthesis count does not match",
                    TerminalCalculatorExpressionFormatErrorCode.UNBALANCED_PARENTHESIS);
        }

    }

    private String identifyOperation(final String expression) {

        if(LOGGER.isTraceEnabled()){
            LOGGER.traceEntry(this.getClass().getName()+" : identifyOperation");
        }


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

        if(LOGGER.isTraceEnabled()){
            LOGGER.traceExit(this.getClass().getName()+" : findIndexOfDelimiter");
        }

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

        if(LOGGER.isTraceEnabled()){
            LOGGER.traceEntry(this.getClass().getName()+" : checkIfExpressionHasBalancedParenthesisAndIsWellFormed");
        }

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
                    throw new TerminalCalculatorInvalidArgumentException(
                            "Division by Zero Encountered, check the arguments in Division Expression");
                }
                result = Long.parseLong(leftOperand.trim())/Long.parseLong(rightOperand.trim());
                break;
            default :
                throw new TerminalCalculatorOperationException("Operation not permitted : " + operation);
        }

        if(LOGGER.isTraceEnabled()){
            LOGGER.traceExit(this.getClass().getName()+" : checkIfExpressionHasBalancedParenthesisAndIsWellFormed");
        }

        return result;

    }

    private void validateExpressionResult (final long result) throws TerminalCalculatorResultThresholdException{
        if(result > Integer.MAX_VALUE) {
            throw new TerminalCalculatorResultThresholdException(" result exceeded maximum threshold, "
                    + Integer.MAX_VALUE);
        } else if (result < Integer.MIN_VALUE) {
            throw new TerminalCalculatorResultThresholdException(" result lesser than minimum threshold, "
                    + Integer.MIN_VALUE);
        }
    }

    private String substituteIfItsAVariableFromLetExpression( String variable){

        if(LOGGER.isTraceEnabled()){
            LOGGER.traceEntry(this.getClass().getName()+" : substituteIfItsAVariableFromLetExpression");
        }

        Set<String> variablesAvailable  = VARIABLE_VALUE_MAP.keySet();
        if(variablesAvailable.contains(variable)){
            variable = VARIABLE_VALUE_MAP.get(variable);
        }

        if(LOGGER.isTraceEnabled()){
            LOGGER.traceExit(this.getClass().getName()+" : substituteIfItsAVariableFromLetExpression");
        }

        return variable;

    }

    private void loadVariableValueMap(final String variable, final String value) {
        VARIABLE_VALUE_MAP.put(variable,value);
    }
}


