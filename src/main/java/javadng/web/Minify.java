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
                current.startLine();

                int length = line.length();
                for (index = 0; index < length; index++) {
                    current.text(line.charAt(index));
                }
                current.endLine();
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
        return c == ' ' || c == '\t' || (includeLine && (c == '\r' || c == '\n'));
    }

    private boolean matcheForward(String text) {
        int target = index + 1;
        if (line.length() <= target) {
            return false;
        }
        return line.subSequence(target, target + text.length()).equals(text);
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

    public static void main(String[] args) {
        minify("docs/mimic.js", "docs/mimic.min.js");
    }

    /**
     * The lexical context.
     */
    private class Context {

        /** The parent context. */
        private Context parent;

        /**
         * Start new {@link Context}.
         * 
         * @param context
         */
        protected final void start(Context context) {
            context.parent = this;
            current = context;
        }

        /**
         * End the current {@link Context}.
         */
        protected final void end() {
            current = current.parent;
        }

        void text(char c) {
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

            case ' ':
            case '\t':
                if (output.length() != 0 && output.charAt(output.length() - 1) != '\n') {
                    output.append(c);
                }
                break;

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
                trimBackward(true);
                output.append(c);
                trimForward();
                break;

            default:
                output.append(c);
                break;
            }
        }

        void startLine() {
            line = line.stripLeading();
        }

        void endLine() {
            if (output.length() == 0) {
                return;
            }

            trimBackward();

            switch (output.charAt(output.length() - 1)) {
            case ';':
            case '{':
            case ',':
            case '/':
                break;

            default:
                output.append('\n');
                break;
            }
        }
    }

    /**
     * {@link Context} for the single line comment.
     */
    private class LineComment extends Context {

        @Override
        void text(char c) {
        }

        @Override
        void endLine() {
            end();
        }
    }

    /**
     * {@link Context} for the block comment.
     */
    private class BlockComment extends Context {

        @Override
        void text(char c) {
            if (c == '/' && line.charAt(index - 1) == '*') {
                end();
            }
        }

        @Override
        void startLine() {
        }

        @Override
        void endLine() {
        }
    }

    /**
     * {@link Context} for the string litereal.
     */
    private class SingleStringLiteral extends Context {

        @Override
        void text(char c) {
            if (c == '\'' && !escaped()) {
                output.append(c);
                end();
            } else {
                output.append(c);
            }
        }

        @Override
        void startLine() {
        }

        @Override
        void endLine() {
            output.append('\n');
        }
    }

    /**
     * {@link Context} for the string litereal.
     */
    private class DoubleStringLiteral extends Context {

        @Override
        void text(char c) {
            if (c == '"' && !escaped()) {
                output.append(c);
                end();
            } else {
                output.append(c);
            }
        }

        @Override
        void startLine() {
        }

        @Override
        void endLine() {
            output.append('\n');
        }
    }
}
