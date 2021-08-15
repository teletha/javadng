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
import java.io.BufferedWriter;
import java.io.IOException;

import kiss.I;
import psychopath.File;
import psychopath.Locator;

public class Minify {

    private final StringBuilder builder = new StringBuilder();

    /**
     * 
     */
    private Minify(BufferedReader input, BufferedWriter output) {
        input.lines().forEach(line -> {
            line = line.strip();

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                builder.append(c);
            }
            builder.append('\n');
        });

        try {
            output.append(builder).close();
        } catch (IOException e) {
            throw I.quiet(e);
        }
    }

    private void trimBackward() {
        for (int i = builder.length() - 1; isDeletable(builder.charAt(i)); i--) {
            builder.deleteCharAt(i);
        }
    }

    private boolean isDeletable(char c) {
        return c == ' ';
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
