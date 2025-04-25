/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.javadoc;

import java.io.Serializable;

import org.junit.jupiter.api.Test;

import javadng.JavadocTestSupport;
import javadng.javadoc.ParameterizableInfo;

public class ParameterizableInfoTest extends JavadocTestSupport {

    @Test
    public <A extends Comparable> void bounded() {
        assert checkTypeParmeterName(currentMethod(), "A");
        assert checkTypeParameter(currentMethod(), "<code>A</code><code class='extends'><code>java.lang.Comparable</code></code>");
    }

    @Test
    public <A extends Comparable<A>> void boundedVariable() {
        assert checkTypeParmeterName(currentMethod(), "A");
        assert checkTypeParameter(currentMethod(), "<code>A</code><code class='extends'><code>java.lang.Comparable<code class='parameters'><code>A</code></code></code></code>");
    }

    @Test
    public <A extends Comparable & Serializable> void intersection() {
        assert checkTypeParmeterName(currentMethod(), "A");
        assert checkTypeParameter(currentMethod(), "<code>A</code><code class='extends'><code>java.lang.Comparable</code> &amp; <code>java.io.Serializable</code></code>");
    }

    @Test
    public <A> void simple() {
        assert checkTypeParmeterName(currentMethod(), "A");
        assert checkTypeParameter(currentMethod(), "<code>A</code>");
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