/*
 * Copyright (C) 2021 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.parser.analyze;

import java.util.HashMap;
import java.util.Map;

import kiss.I;
import kiss.XML;

/**
 * Hard typed {@link Map} for template.
 */
public class TemplateStore extends HashMap<String, XML> {
    private static final long serialVersionUID = -4452932715112144902L;

    private static final TemplateStore Root = new TemplateStore();

    private TemplateStore() {
    }

    TemplateStore(TemplateStore parent) {
        if (parent == null) {
            parent = Root;
        }
        putAll(parent);
    }

    /**
     * Register your template.
     * 
     * @param key
     * @param value
     */
    public static final void register(String key, String value) {
        register(key, I.xml("<span>" + value + "</span>"));
    }

    /**
     * Register your template.
     * 
     * @param key
     * @param value
     */
    public static final void register(String key, XML value) {
        Root.put(key, value);
    }
}