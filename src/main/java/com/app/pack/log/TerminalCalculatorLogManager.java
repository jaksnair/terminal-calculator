package com.app.pack.log;

import com.app.pack.processor.TerminalCalculatorProcessor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Created by jayakrishnansomasekharannair on 6/27/18.
 */
public class TerminalCalculatorLogManager {

    private static Logger LOGGER = LogManager.getLogger(TerminalCalculatorLogManager.class.getName());

    public static void setLogLevel() {

        LOGGER.info("TerminalCalculatorLogManager : setLogLevel");

        String logLevel = LogLevel.WARN.name();
        if (Boolean.getBoolean("log4j.debug")) {
            logLevel = LogLevel.DEBUG.name();
        } else if (Boolean.getBoolean("log4j.info")) {
            logLevel = LogLevel.INFO.name();
        } else if (Boolean.getBoolean("log4j.error")){
            logLevel = LogLevel.ERROR.name();
        } else if (Boolean.getBoolean("log4j.trace")){
            logLevel = LogLevel.TRACE.name();
        } else if (Boolean.getBoolean("log4j.fatal")){
            logLevel = LogLevel.FATAL.name();
        } else if (Boolean.getBoolean("log4j.all")){
            logLevel = LogLevel.ALL.name();
        } else if (Boolean.getBoolean("log4j.off")){
            logLevel = LogLevel.OFF.name();
        }
        Level level = Level.getLevel(logLevel);

        LOGGER.warn("LogLevel set is : "+ logLevel);

        Configurator.setRootLevel(level);
    }
}
