/*
 * Copyright (C) 2021 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import kiss.I;
import psychopath.File;
import psychopath.Locator;

public class Minify {

    private final StringBuilder output = new StringBuilder();

    private String line;

    private int index;

    private Context current = new Context();

    /**
     * 
     */
    private Minify(BufferedReader reader, Writer writer) {
        try {
            while ((line = reader.readLine()) != null) {
                line = line.strip();

                for (index = 0; index < line.length(); index++) {
                    char c = line.charAt(index);

                    current.process(c);
                }
                current.line();
            }

            trimBackward(true);
            writer.append(output).close();
        } catch (IOException e) {
            throw I.quiet(e);
        }
    }

    private void trimForward() {
        for (; index + 1 < line.length() && isDeletable(line.charAt(index + 1), false); index++) {
        }
    }

    private void trimBackward() {
        trimBackward(false);
    }

    private void trimBackward(boolean includeLine) {
        int index = output.length() - 1;

        if (index == -1) {
            return;
        }

        for (; 0 <= index && isDeletable(output.charAt(index), includeLine); index--) {
            output.deleteCharAt(index);
        }
    }

    private boolean isDeletable(char c, boolean includeLine) {
        return c == ' ' || (includeLine && (c == '\r' || c == '\n'));
    }

    private boolean matcheForward(String text) {
        return line.subSequence(index + 1, index + 1 + text.length()).equals(text);
    }

    private boolean matcheBackward(String text) {
        int length = text.length();
        int size = output.length() - 1;
        if (size < length) {
            return false;
        }
        return output.subSequence(size - length, size).equals(text);
    }

    private boolean escaped() {
        int count = 0;
        int index = output.length() - 1;
        while (output.charAt(index) == '\\') {
            index--;
            count++;
        }
        return count % 2 == 1;
    }

    public static String minify(String code) {
        StringWriter writer = new StringWriter();
        new Minify(new BufferedReader(new StringReader(code)), writer);
        return writer.toString();
    }

    public static void minify(String inputFile, String outputFile) {
        minify(Locator.file(inputFile), Locator.file(outputFile));
    }

    public static void minify(File input, File output) {
        new Minify(input.newBufferedReader(), output.newBufferedWriter());
    }

    public static void main2(String[] args) {
        minify("docs/main.js", "docs/main.min.js");
    }

    private class Context {

        private Context previous;

        protected final void start(Context context) {
            context.previous = this;
            current = context;
        }

        protected final void end() {
            current = current.previous;
        }

        void process(char c) {
            switch (c) {
            case '"':
                trimBackward();
                output.append(c);
                start(new DoubleStringLiteral());
                break;

            case '\'':
                trimBackward();
                output.append(c);
                start(new SingleStringLiteral());
                break;

            case '/':
                if (matcheForward("*")) {
                    trimBackward();
                    start(new BlockComment());
                    break;
                } else if (matcheForward("/")) {
                    trimBackward();
                    start(new LineComment());
                    break;
                } else {
                    trimBackward();
                    output.append(c);
                    trimForward();
                    break;
                }

            case '(':
            case ')':
            case '{':
            case '}':
            case '[':
            case ']':
            case '+':
            case '-':
            case '*':
            case '%':
            case '=':
            case '>':
            case '<':
            case '|':
            case '&':
            case '^':
            case '~':
            case '!':
            case '?':
            case '`':
            case ';':
            case ':':
            case '.':
            case ',':
                trimBackward();
                output.append(c);
                trimForward();
                break;

            default:
                output.append(c);
                break;
            }
        }

        void line() {
            output.append('\n');
        }
    }

    private class LineComment extends Context {

        @Override
        void process(char c) {
            if (c == '/' && line.charAt(index - 1) == '*') {
                end();
            }
        }

        @Override
        void line() {
            end();
        }
    }

    private class BlockComment extends Context {

        @Override
        void process(char c) {
            if (c == '/' && line.charAt(index - 1) == '*') {
                end();
            }
        }

        @Override
        void line() {
        }
    }

    private class SingleStringLiteral extends Context {

        @Override
        void process(char c) {
            if (c == '\'' && !escaped()) {
                output.append(c);
                end();
            } else {
                output.append(c);
            }
        }
    }

    private class DoubleStringLiteral extends Context {

        @Override
        void process(char c) {
            if (c == '"' && !escaped()) {
                output.append(c);
                end();
            } else {
                output.append(c);
            }
        }
    }
}
