/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.parser;

import java.io.IOError;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import javadng.page.Javadoc;
import javadng.page.JavadocTestSupport;
import kiss.XML;
import kiss.Ⅱ;

/**
 * @text This is fatal error on Root.
 */
public class DocumentInfoTest extends JavadocTestSupport {

    private static final String BASE = "api/" + DocumentInfoTest.class.getName() + ".html";

    /**
     * Text
     */
    @Test
    public void text() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><p>Text</p></span>");
    }

    /**
     * <p>
     * Text
     * </p>
     */
    @Test
    public void element() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><p>Text</p></span>");
    }

    /**
     * <a type="stylesheet" href="test.css"/>
     */
    @Test
    public void attribute() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><a href=\"test.css\" type=\"stylesheet\"/></span>");
    }

    /***
     * This text is written by markdown.
     */
    @Test
    public void markdown() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><p>This text is written by markdown.</p></span>");
    }

    /**
     * ## Title
     * 
     * This text is written by markdown.
     */
    @Test
    public void markdownTitle() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><h2>Title</h2><p>This text is written by markdown.</p></span>");
    }

    /**
     * This text is written by markdown.
     * 
     * - item1
     * - item2
     * - item3
     */
    @Test
    public void markdownList() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><p>This text is written by markdown.</p><ul><li>item1</li><li>item2</li><li>item3</li></ul></span>");
    }

    @Test
    public void paramTag() {
        ExecutableInfo info = method("param");
        Ⅱ<String, XML> param = info.paramTags.get(0);
        assert param.ⅰ.equals("name1");
        assert sameXML(param.ⅱ, "<span><p>Description.</p></span>");

        param = info.paramTags.get(1);
        assert param.ⅰ.equals("name2");
        assert sameXML(param.ⅱ, "<span><p>This is <em>NOT</em> error.</p></span>");
    }

    /**
     * @param name1 Description.
     * @param name2 This is <em>NOT</em> error.
     */
    public void param(String name1, String name2) {
    }

    @Test
    public void parameterTypeTag() {
        ExecutableInfo info = method("paramType");
        Ⅱ<String, XML> param = info.typeParameterTags.get(0);
        assert param.ⅰ.equals("T");
        assert sameXML(param.ⅱ, "<span><p>Description.</p></span>");

        param = info.typeParameterTags.get(1);
        assert param.ⅰ.equals("NEXT");
        assert sameXML(param.ⅱ, "<span><p>This is <em>NOT</em> error.</p></span>");
    }

    /**
     * @param <T> Description.
     * @param <NEXT> This is <em>NOT</em> error.
     */
    public <T, NEXT> void paramType() {
    }

    /**
     * @throws NullPointerException If param is null.
     * @throws IllegalArgumentException If param is <code>0</code>.
     */
    @Test
    public void throwsTag() {
        ExecutableInfo info = currentMethod();
        Ⅱ<String, XML> param = info.throwsTags.get(0);
        assert param.ⅰ.equals("NullPointerException");
        assert sameXML(param.ⅱ, "<span><p>If param is null.</p></span>");

        param = info.throwsTags.get(1);
        assert param.ⅰ.equals("IllegalArgumentException");
        assert sameXML(param.ⅱ, "<span><p>If param is <code>0</code>.</p></span>");
    }

    @Test
    public void returnTag() {
        ExecutableInfo info = method("returnValue");
        XML description = info.returnTag.exact();
        assert sameXML(description, "<span><p>description</p></span>");
    }

    /**
     * @return description
     */
    public int returnValue() {
        return 0;
    }

    /**
     * {@literal 0 < i}
     */
    @Test
    public void literalTag() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code>0 &amp;lt; i</code></span>");
    }

    /**
     * {@link DocumentInfoTest}
     */
    @Test
    public void linkTagInternalType() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "' aria-label='javadng.parser.DocumentInfoTest'>DocumentInfoTest</a></code></span>");
    }

    /**
     * {@link #linkTagInternalMethod()}
     */
    @Test
    public void linkTagInternalMethod() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#linkTagInternalMethod()' aria-label='javadng.parser.DocumentInfoTest'>#linkTagInternalMethod()</a></code></span>");
    }

    /**
     * {@link DocumentInfoTest#linkTagInternalTypeAndMethod()}
     */
    @Test
    public void linkTagInternalTypeAndMethod() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#linkTagInternalTypeAndMethod()' aria-label='javadng.parser.DocumentInfoTest'>DocumentInfoTest#linkTagInternalTypeAndMethod()</a></code></span>");
    }

    /**
     * {@link SiblingType}
     */
    @Test
    public void linkTagInternalSiblingType() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#linkTagInternalTypeAndMethod()' aria-label='javadng.parser.DocumentInfoTest'>DocumentInfoTest#linkTagInternalTypeAndMethod()</a></code></span>");
    }

    static class SiblingType {
    }

    /**
     * {@link String}
     */
    @Test
    public void linkTagUnregisteredExternalType() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><p>String</p></span>");
    }

    /**
     * {@link String#chars()}
     */
    @Test
    public void linkTagUnregisteredExternalTypeAndMethod() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><p>String#chars()</p></span>");
    }

    /**
     * {@link String}
     */
    @Test
    public void linkTagRegisteredExternalType() {
        ExecutableInfo info = currentMethodEx();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + Javadoc.JDK + "java.base/java/lang/String.html' aria-label='java.lang.String 🚀'>String</a></code></span>");
    }

    /**
     * {@link String#chars()}
     */
    @Test
    public void linkTagRegisteredExternalTypeAndMethod() {
        ExecutableInfo info = currentMethodEx();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + Javadoc.JDK + "java.base/java/lang/String.html#chars()' aria-label='java.lang.String 🚀'>String#chars()</a></code></span>");
    }

    /**
     * {@linkplain DocumentInfoTest}
     */
    @Test
    public void linkplainTagInternalType() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "' aria-label='javadng.parser.DocumentInfoTest'>DocumentInfoTest</a></code></span>");
    }

    /**
     * {@linkplain #linkplainTagInternalMethod()}
     */
    @Test
    public void linkplainTagInternalMethod() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#linkplainTagInternalMethod()' aria-label='javadng.parser.DocumentInfoTest'>#linkplainTagInternalMethod()</a></code></span>");
    }

    /**
     * {@linkplain DocumentInfoTest#linkplainTagInternalTypeAndMethod()}
     */
    @Test
    public void linkplainTagInternalTypeAndMethod() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><code><a href='" + BASE + "#linkplainTagInternalTypeAndMethod()' aria-label='javadng.parser.DocumentInfoTest'>DocumentInfoTest#linkplainTagInternalTypeAndMethod()</a></code></span>");
    }

    /**
     * {@linkplain String}
     */
    @Test
    public void linkplainTagUnregisteredExternalType() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><p>String</p></span>");
    }

    /**
     * {@linkplain String#chars()}
     */
    @Test
    public void linkplainTagUnregisteredExternalTypeAndMethod() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.comment, "<span class='B'><p>String#chars()</p></span>");
    }

    /**
     * @author Me
     * @author <b>You</b>
     */
    @Test
    public void authorTag() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.authorTags.get(0), "<span><p>Me</p></span>");
        assert sameXML(info.authorTags.get(1), "<span><b>You</b></span>");
    }

    /**
     * @see Text
     * @see String
     */
    @Test
    public void seeTag() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.seeTags.get(0), "<span><p>Text</p></span>");
        assert sameXML(info.seeTags.get(1), "<span><p>String</p></span>");
    }

    /**
     * @since 1.0
     * @since <b>1.2</b>
     */
    @Test
    public void sinceTag() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.sinceTags.get(0), "<span><p>1.0</p></span>");
        assert sameXML(info.sinceTags.get(1), "<span><b>1.2</b></span>");
    }

    /**
     * @version 1.0
     * @version <b>1.2</b>
     */
    @Test
    public void versionTags() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.versionTags.get(0), "<span><p>1.0</p></span>");
        assert sameXML(info.versionTags.get(1), "<span><b>1.2</b></span>");
    }

    /**
     * @text This is fatal error.
     * @throws IOError {@var text}
     */
    @Test
    public void templateTag() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.throwsTags.get(0).ⅱ, "<span><p>This is fatal error.</p></span>");
    }

    /**
     * @throws IOError {@var text}
     */
    @Test
    public void templateTagFromEnclosingElement() {
        ExecutableInfo info = currentMethod();
        assert sameXML(info.throwsTags.get(0).ⅱ, "<span><p>This is fatal error on Root.</p></span>");
    }
}