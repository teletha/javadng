/*
 * Copyright (C) 2023 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kiss.I;

public class CodeHighlighter {

    /** The language set to highlight code. */
    private static final Set<String> languages = new HashSet();

    /**
     * Add language to highlight.
     * 
     * @param language
     */
    public static void addLanguage(String language) {
        if (language != null && !language.isEmpty()) {
            languages.add(language);
        }
    }

    /**
     * Build highlighting code.
     * 
     * @return
     */
    public static List<String> build() {
        return I.signal(languages)
                .flatMap(x -> I.http("https://unpkg.com/@highlightjs/cdn-assets/es/languages/" + x + ".min.js", String.class)
                        .waitForTerminate()
                        .map(text -> text.replaceAll("export default hljsGrammar", "J.registerLanguage('" + x + "', hljsGrammar)")))
                .toList();
    }
}
