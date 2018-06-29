package com.app.pack.constants;

/**
 * Created by jayakrishnansomasekharannair on 6/27/18.
 */

/**
 * Allowed operations.
 */
public enum Operations {

    ADD("add"),
    SUB("sub"),
    MULT("mult"),
    DIV("div"),
    LET("let");

    String name;

    Operations(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
