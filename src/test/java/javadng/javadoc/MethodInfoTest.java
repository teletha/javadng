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
import java.util.List;

import org.junit.jupiter.api.Test;

import javadng.javadoc.MethodInfo;
import javadng.page.JavadocTestSupport;

public class MethodInfoTest extends JavadocTestSupport {

    @Test
    protected void returnType() {
        assert checkReturnType(currentMethod(), "<code>void</code>");
    }

    @Test
    void returnPrimitive() {
        assert checkReturnType(method("integer"), "<code>int</code>");
    }

    protected int integer() {
        return 1;
    }

    @Test
    void returnString() {
        assert checkReturnType(method("string"), "<code>java.lang.String</code>");
    }

    protected String string() {
        return "";
    }

    @Test
    void returnGenerics() {
        assert checkReturnType(method("generics"), "<code>T</code>");
    }

    protected <T> T generics() {
        return null;
    }

    @Test
    void returnBounded() {
        assert checkReturnType(method("bounded"), "<code>T</code>");
    }

    protected <T extends Serializable> T bounded() {
        return null;
    }

    @Test
    void returnParameterized() {
        assert checkReturnType(method("parameterized"), "<code>java.util.List<code class='parameters'><code>java.lang.String</code></code></code>");
    }

    protected List<String> parameterized() {
        return null;
    }

    /**
     * Shortcut method.
     * 
     * @param info
     * @param expected
     * @return
     */
    private boolean checkReturnType(MethodInfo info, String expected) {
        assert sameXML(info.createReturnType(), expected);
        return true;
    }
}