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

import java.util.List;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import stoneforge.javadoc.JavadocTestSupport;

public class StructuredTypeTest extends JavadocTestSupport {

    @ParameterizedTest
    @ValueSource(ints = 0)
    public void primitiveInt(int type) {
        assert checkParamType(currentMethod(), "<code>int</code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void primitiveIntArray(int[] type) {
        assert checkParamType(currentMethod(), "<code array='fix'>int</code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void primitiveIntVarArray(int... type) {
        assert checkParamType(currentMethod(), "<code array='var'>int</code>");
    }

    @ParameterizedTest
    @ValueSource(longs = 0)
    public void primitiveLong(long type) {
        assert checkParamType(currentMethod(), "<code>long</code>");
    }

    @ParameterizedTest
    @ValueSource(floats = 0)
    public void primitiveFloat(float type) {
        assert checkParamType(currentMethod(), "<code>float</code>");
    }

    @ParameterizedTest
    @ValueSource(doubles = 0)
    public void primitiveDouble(double type) {
        assert checkParamType(currentMethod(), "<code>double</code>");
    }

    @ParameterizedTest
    @ValueSource(shorts = 0)
    public void primitiveShort(short type) {
        assert checkParamType(currentMethod(), "<code>short</code>");
    }

    @ParameterizedTest
    @ValueSource(bytes = 0)
    public void primitiveByte(byte type) {
        assert checkParamType(currentMethod(), "<code>byte</code>");
    }

    @ParameterizedTest
    @ValueSource(chars = '0')
    public void primitiveChar(char type) {
        assert checkParamType(currentMethod(), "<code>char</code>");
    }

    @ParameterizedTest
    @ValueSource(booleans = false)
    public void primitiveBoolean(boolean type) {
        assert checkParamType(currentMethod(), "<code>boolean</code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void topLevelType(String type) {
        assert checkParamType(currentMethod(), "<code>java.lang.String</code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void memberType(Thread.State type) {
        assert checkParamType(currentMethod(), "<code>java.lang.Thread.State</code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void nestedMemberType(ProcessBuilder.Redirect.Type type) {
        assert checkParamType(currentMethod(), "<code>java.lang.ProcessBuilder.Redirect.Type</code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void arrayType(String[] type) {
        assert checkParamType(currentMethod(), "<code array='fix'>java.lang.String</code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void vararg(String... type) {
        assert checkParamType(currentMethod(), "<code array='var'>java.lang.String</code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public <T> void arrayGeneric(T[] type) {
        assert checkParamType(currentMethod(), "<code array='fix'>T</code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public <T> void varargGeneric(T... type) {
        assert checkParamType(currentMethod(), "<code array='var'>T</code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public <T> void variableType(T type) {
        assert checkParamType(currentMethod(), "<code>T</code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void parameterizedType(List<String> type) {
        assert checkParamType(currentMethod(), "<code>java.util.List<code class='parameters'><code>java.lang.String</code></code></code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void parameterizedTypes(Map<String, Class> type) {
        assert checkParamType(currentMethod(), "<code>java.util.Map<code class='parameters'><code>java.lang.String</code>, <code>java.lang.Class</code></code></code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public <T> void parameterizedTypeByVariable(List<T> type) {
        assert checkParamType(currentMethod(), "<code>java.util.List<code class='parameters'><code>T</code></code></code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public <T extends CharSequence> void parameterizedTypeByBoundedVariable(List<T> type) {
        assert checkParamType(currentMethod(), "<code>java.util.List<code class='parameters'><code>T</code></code></code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void parameterizedTypeByWildcardType(List<?> type) {
        assert checkParamType(currentMethod(), "<code>java.util.List<code class='parameters'><code>?</code></code></code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void parameterizedTypeByLowerBoundedType(List<? extends CharSequence> type) {
        assert checkParamType(currentMethod(), "<code>java.util.List<code class='parameters'><code>?</code><code class='extends'><code>java.lang.CharSequence</code></code></code></code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public <T> void parameterizedTypeByLowerBoundedVariable(List<? extends T> type) {
        assert checkParamType(currentMethod(), "<code>java.util.List<code class='parameters'><code>?</code><code class='extends'><code>T</code></code></code></code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public <T extends CharSequence> void parameterizedTypeByLowerBoundedBoundedVariable(List<? extends T> type) {
        assert checkParamType(currentMethod(), "<code>java.util.List<code class='parameters'><code>?</code><code class='extends'><code>T</code></code></code></code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public void parameterizedTypeByUpperBoundedType(List<? super CharSequence> type) {
        assert checkParamType(currentMethod(), "<code>java.util.List<code class='parameters'><code>?</code><code class='super'><code>java.lang.CharSequence</code></code></code></code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public <T> void parameterizedTypeByUpperBoundedVariable(List<? super T> type) {
        assert checkParamType(currentMethod(), "<code>java.util.List<code class='parameters'><code>?</code><code class='super'><code>T</code></code></code></code>");
    }

    @ParameterizedTest
    @ArgumentsSource(NullProvider.class)
    public <T extends CharSequence> void parameterizedTypeByUpperBoundedBoundedVariable(List<? super T> type) {
        assert checkParamType(currentMethod(), "<code>java.util.List<code class='parameters'><code>?</code><code class='super'><code>T</code></code></code></code>");
    }

    /**
     * Shortcut method.
     * 
     * @param info
     * @param expected
     * @return
     */
    private boolean checkParamType(ExecutableInfo info, String expected) {
        return sameXML(info.createParameter(0), expected);
    }
}