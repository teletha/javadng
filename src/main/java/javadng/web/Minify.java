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
import java.util.ArrayDeque;
import java.util.Deque;

import kiss.I;
import psychopath.File;
import psychopath.Locator;

public class Minify {

    private final static char BASE = '乀';

    private final StringBuilder output = new StringBuilder();

    private String line;

    private int index;

    private final Deque<Character> stacks = new ArrayDeque();

    private char end = BASE;

    /**
     * 
     */
    private Minify(BufferedReader reader, Writer writer) {
        try {
            while ((line = reader.readLine()) != null) {
                line = line.strip();

                for (index = 0; index < line.length(); index++) {
                    char c = line.charAt(index);

                    if (end != BASE) {
                        output.append(c);

                        if (c == end) {
                            if (matcheBackward("\\")) {

                            } else {
                                end = stacks.pollLast();
                            }
                        }
                        continue;
                    }

                    switch (c) {
                    case '"':
                    case '\'':
                        trimBackward();
                        output.append(c);
                        stacks.addLast(end);
                        end = c;
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
                    case '/':
                    case '%':
                    case '=':
                    case '>':
                    case '<':
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
                output.append('\n');
            }

            trimBackward();
            writer.append(output).close();
        } catch (IOException e) {
            throw I.quiet(e);
        }
    }

    private void trimForward() {
        for (; index + 1 < line.length() && isDeletable(line.charAt(index + 1)); index++) {
        }
    }

    private void trimBackward() {
        int index = output.length() - 1;

        if (index == -1) {
            return;
        }

        for (; isDeletable(output.charAt(index)); index--) {
            output.deleteCharAt(index);
        }
    }

    private boolean isDeletable(char c) {
        return c == ' ' || c == '\n' || c == '\r';
    }

    private boolean matcheBackward(String text) {
        int length = text.length();
        int size = output.length() - 1;
        return output.subSequence(size - length, size).equals(text);
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
        minify("docs/main.js", "docs/main.min.js");
    }
}
