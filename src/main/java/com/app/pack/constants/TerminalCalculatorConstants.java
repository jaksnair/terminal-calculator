package com.app.pack.constants;

/**
 * Created by jayakrishnansomasekharannair on 6/26/18.
 */

/**
 * Application constants (class helper constants and expression constants) are present in this interface.
 */

public interface TerminalCalculatorConstants {

    String TERMINAL_CALCULATOR_ADD_OPERATION = "add";

    String TERMINAL_CALCULATOR_SUB_OPERATION = "sub";

    String TERMINAL_CALCULATOR_MULT_OPERATION = "mult";

    String TERMINAL_CALCULATOR_DIV_OPERATION = "div";

    char TERMINAL_CALCULATOR_LEFT_PARENTHESIS = '(';

    char TERMINAL_CALCULATOR_RIGHT_PARENTHESIS = ')';

    char TERMINAL_CALCULATOR_DELIMITER_COMMA = ',';

    char TERMINAL_CALCULATOR_DELIMITER_SPACE = ' ';

    String EXCEPTION_OCCURRED_MESSAGE = "Exception Occurred : ";

    String EXCEPTION_CATEGORY_MESSAGE = " - ExceptionCategory : ";

    String EMPTY_INPUT_FOUND_MESSAGE = "Found empty input. ";

    String OPERATION_NOT_PERMITTED_MESSAGE = "Operation not permitted :";

    String RESULT_EXCEEDED_MAXIMUM_THRESHOLD_MESSAGE = "Result exceeded maximum threshold. ";

    String RESULT_LESSER_THAN_MINIMUM_THRESHOLD_MESSAGE = "Result lesser than minimum threshold. ";

    String PARENTHESIS_COUNT_DOES_NOT_MATCH_MESSAGE = "Parenthesis count does not match. ";

    String DELIMITER_PATTERN_IS_NOT_AS_EXPECTED_MESSAGE = "Delimiter pattern is not as expected. ";

    String DIVISION_BY_ZERO_ENCOUNTERED_MESSAGE = "Division by Zero Encountered, check the arguments in Division Expression. ";

    String UNEXPECTED_LET_VARIABLE_FORMAT_MESSAGE = "Unexpected variable format in LET expression : ";

    String UNEXPECTED_LET_VARIABLE_FOUND_MESSAGE = "Unexpected variable found in LET expression : ";
}
