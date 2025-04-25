/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.js;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kiss.I;

public class CodeHighlight {

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
        return I.signal(languages).flatMap(x -> {
            return I.http("https://unpkg.com/@highlightjs/cdn-assets@11.7.0/es/languages/" + x + ".min.js", String.class)
                    .waitForTerminate()
                    .map(text -> text.replaceAll("export default hljsGrammar", "J.registerLanguage('" + x + "', hljsGrammar)"));
        }).toList();
    }
}