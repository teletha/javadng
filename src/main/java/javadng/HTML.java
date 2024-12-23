/*
 * Copyright (C) 2024 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng;

import java.util.function.Consumer;

import javadng.design.Styles;
import kiss.XML;

/**
 * Domain Specific Language for HTML.
 */
public abstract class HTML extends lycoris.HTML {

    protected final Consumer<XML> svg(String type) {
        return parent -> {
            $("svg", attr("viewBox", "0 0 24 24"), Styles.AnimatedSVG, () -> {
                $("use", attr("href", "main.svg#" + type));
            });
        };
    }

    /**
     * Build JSONP file and return the path of the generated file.
     * 
     * @param model A style definition class to write.
     */
    protected final void script(String path, Object model) {
        $("script", attr("src", SiteBuilder.current.buildJSONP(path, model)));
    }
}