####################################
#   terminal-calculator - READ ME  #
####################################

 - terminal-calculator is a executable command-line calculator that evaluates expressions in a very simple integer expression language.
 - Log level controlling via command line arguments possible.
 - CI integration setup available.

######################
# REPOSITORY DETAILS #
######################

1. Code will be available at repository : https://github.com/jaksnair/terminal-calculator

2. Branches are,
                - master : default branch
                - r<<version>>-dev : dev branch
                - r<<version>>-dev-travis-ci-test : dev branch with travis-ci integrated.

3. Git hub apps or CIs integrated
                - Travis-ci : https://travis-ci.org/jaksnair/terminal-calculator

#####################
#       ASSEMBLY    #
#####################

1. Please assemble to build the jar using command: mvn clean compile assembly:single

######################
# FUNCTIONALITY NOTE #
######################

1. It evaluates expressions in a very simple integer expression language.
2. Takes an input on the command line, computes the result, and prints it to the console. 
3. Expression details :
    - Numbers : integers between Integer.MIN_VALUE and Integer.MAX_VALUE.
    - Variables: strings of characters, where each character is one of a-z, A-Z.
    - Arithmetic functions: add, sub, mult, div, each taking two arbitrary expressions as arguments. 
    - A “let” operator for assigning values to variables:
      	let(<variable name>, <value expression>, <expression where variable is used>)
4. Logging verbosity control :
    - levels of verbosity: INFO, ERROR, DEBUG, FATAL, WARN, TRACE, ALL
5. CI setup available.


############
#   USAGE  #
############

1. The jar takes an input on the command line, computes the result, and prints it to the console. 
    For example: java -jar terminalcalculator.jar "add(2, 2)"

2. Logger level could be controlled via the command line.
    For example: java -jar -Dlog4j.debug=true terminalcalculator.jar "add(2, 2)"


