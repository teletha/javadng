/*
 * Copyright (C) 2020 stoneforge Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package stoneforge.javadoc.analyze;

import java.io.Serializable;

import org.junit.jupiter.api.Test;

import stoneforge.javadoc.JavadocTestSupport;

public class ParameterizableInfoTest extends JavadocTestSupport {

    @Test
    public <A extends Comparable> void bounded() {
        assert checkTypeParmeterName(currentMethod(), "A");
        assert checkTypeParameter(currentMethod(), "<i>A</i><i class='extends'><i>java.lang.Comparable</i></i>");
    }

    @Test
    public <A extends Comparable<A>> void boundedVariable() {
        assert checkTypeParmeterName(currentMethod(), "A");
        assert checkTypeParameter(currentMethod(), "<i>A</i><i class='extends'><i>java.lang.Comparable<i class='parameters'><i>A</i></i></i></i>");
    }

    @Test
    public <A extends Comparable & Serializable> void intersection() {
        assert checkTypeParmeterName(currentMethod(), "A");
        assert checkTypeParameter(currentMethod(), "<i>A</i><i class='extends'><i>java.lang.Comparable</i> &amp; <i>java.io.Serializable</i></i>");
    }

    @Test
    public <A> void simple() {
        assert checkTypeParmeterName(currentMethod(), "A");
        assert checkTypeParameter(currentMethod(), "<i>A</i>");
    }

    /**
     * Shortcut method.
     * 
     * @param info
     * @param expected
     * @return
     */
    private boolean checkTypeParmeterName(ParameterizableInfo info, String... expected) {
        for (int i = 0; i < expected.length; i++) {
            assert info.createTypeVariableName(i).text().equals(expected[i]);
        }
        return true;
    }

    /**
     * Shortcut method.
     * 
     * @param info
     * @param expected
     * @return
     */
    private boolean checkTypeParameter(ParameterizableInfo info, String... expected) {
        for (int i = 0; i < expected.length; i++) {
            assert sameXML(info.createTypeVariable(i), expected[i]);
        }
        return true;
    }
}