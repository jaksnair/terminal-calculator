package com.app.pack.constants;

/**
 * Created by jayakrishnansomasekharannair on 6/26/18.
 */

/**
 * Application constants (class helper constants and expression constants) are present in this interface.
 */

public abstract class TerminalCalculatorConstants {

    public static final String TERMINAL_CALCULATOR_ADD_OPERATION = "add";

    public static final String TERMINAL_CALCULATOR_SUB_OPERATION = "sub";

    public static final String TERMINAL_CALCULATOR_MULT_OPERATION = "mult";

    public static final String TERMINAL_CALCULATOR_DIV_OPERATION = "div";

    public static final char TERMINAL_CALCULATOR_LEFT_PARENTHESIS = '(';

    public static final char TERMINAL_CALCULATOR_RIGHT_PARENTHESIS = ')';

    public static final char TERMINAL_CALCULATOR_DELIMITER_COMMA = ',';

    public static final char TERMINAL_CALCULATOR_DELIMITER_SPACE = ' ';

    public static final String EXCEPTION_OCCURRED_MESSAGE = "Exception Occurred : ";

    public static final String EXCEPTION_CATEGORY_MESSAGE = " - ExceptionCategory : ";

    public static final String EMPTY_INPUT_FOUND_MESSAGE = "Found empty input. ";

    public static final String OPERATION_NOT_PERMITTED_MESSAGE = "Operation not permitted :";

    public static final String RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE = "Result exceeded maximum threshold. ";

    public static final String RESULT_LESSER_THAN_MINIMUM_THRESHOLD_MESSAGE = "Result lesser than minimum threshold. ";

    public static final String PARENTHESIS_COUNT_DOES_NOT_MATCH_MESSAGE = "Parenthesis count does not match. ";

    public static final String DELIMITER_PATTERN_IS_NOT_AS_EXPECTED_MESSAGE = "Delimiter pattern is not as expected. ";

    public static final String DIVISION_BY_ZERO_ENCOUNTERED_MESSAGE = "Division by Zero Encountered, check the arguments in Division Expression. ";

    public static final String UNEXPECTED_LET_VARIABLE_FORMAT_MESSAGE = "Unexpected variable format in LET expression : ";

    public static final String UNEXPECTED_LET_VARIABLE_FOUND_MESSAGE = "Unexpected variable found in LET expression : ";
}
