/*
 * Copyright (C) 2024 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Hard typed {@link Map} for template.
 */
public class TemplateStore extends HashMap<String, String> {
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
        Root.put(key, value);
    }
}