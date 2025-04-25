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

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import javadng.JavadocTestSupport;

public class CanonicalizeParamTest extends JavadocTestSupport {

    private static final String BASE = "api/" + CanonicalizeParamTest.class.getName() + ".html";

    /**
     * {@link #param(String)}
     */
    @Test
    public void param() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#param(java.lang.String)' aria-label='javadng.javadoc.CanonicalizeParamTest'>#param(String)</a></code></span>");
    }

    native void param(String param);

    /**
     * {@link #param(int, long, float, double, boolean, char, short, byte)}
     */
    @Test
    public void primitives() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#param(int,long,float,double,boolean,char,short,byte)' aria-label='javadng.javadoc.CanonicalizeParamTest'>#param(int, long, float, double, boolean, char, short, byte)</a></code></span>");
    }

    native void param(int i, long l, float f, double d, boolean b, char c, short s, byte by);

    /**
     * {@link #array(String[])}
     */
    @Test
    public void array() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#array(java.lang.String[])' aria-label='javadng.javadoc.CanonicalizeParamTest'>#array(String[])</a></code></span>");
    }

    native void array(String[] param);

    /**
     * {@link #arrayMulti(String[][][])}
     */
    @Test
    public void multiDimensionArray() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#arrayMulti(java.lang.String[][][])' aria-label='javadng.javadoc.CanonicalizeParamTest'>#arrayMulti(String[][][])</a></code></span>");
    }

    native void arrayMulti(String[][][] param);

    /**
     * {@link #arrayVarArge(String...)}
     */
    @Test
    public void varArg() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#arrayVarArge(java.lang.String...)' aria-label='javadng.javadoc.CanonicalizeParamTest'>#arrayVarArge(String...)</a></code></span>");
    }

    native void arrayVarArge(String... param);

    /**
     * {@link #array(int[])}
     */
    @Test
    public void primitiveArray() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#array(int[])' aria-label='javadng.javadoc.CanonicalizeParamTest'>#array(int[])</a></code></span>");
    }

    native void array(int[] param);

    /**
     * {@link #parameterized(Function)}
     */
    @Test
    public void parameterized() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#parameterized(java.util.function.Function)' aria-label='javadng.javadoc.CanonicalizeParamTest'>#parameterized(Function)</a></code></span>");
    }

    native <X> void parameterized(Function<X, String> param);

    /**
     * {@link #parameterizedArray(Function[], Consumer...)}
     */
    @Test
    public void parameterizedArray() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#parameterizedArray(java.util.function.Function[],java.util.function.Consumer...)' aria-label='javadng.javadoc.CanonicalizeParamTest'>#parameterizedArray(Function[], Consumer...)</a></code></span>");
    }

    native <X> void parameterizedArray(Function<X, String>[] param, Consumer<X>... vararg);

    /**
     * {@link #wildcard(Object, CharSequence, Map)}
     */
    @Test
    public void wildcard() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#wildcard(java.lang.Object,java.lang.CharSequence,java.util.Map)' aria-label='javadng.javadoc.CanonicalizeParamTest'>#wildcard(Object, CharSequence, Map)</a></code></span>");
    }

    native <X, Y extends CharSequence, Z extends Map<String, String>> void wildcard(X x, Y y, Z z);

    /**
     * {@link #wildcardArray(Object[], Object...)}
     */
    @Test
    public void wildcardArray() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#wildcardArray(java.lang.Object[],java.lang.Object...)' aria-label='javadng.javadoc.CanonicalizeParamTest'>#wildcardArray(Object[], Object...)</a></code></span>");
    }

    native <X> void wildcardArray(X[] x, X... v);
}