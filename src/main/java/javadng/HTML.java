/*
 * Copyright (C) 2023 The JAVADNG Development Team
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
import kiss.I;
import kiss.Variable;
import kiss.XML;
import lycoris.Tree;
import stylist.Style;
import stylist.StyleDSL;
import stylist.StyleDeclarable;

/**
 * Domain Specific Language for HTML.
 */
public abstract class HTML extends Tree<String, XML> {

    /**
     * 
     */
    public HTML() {
        super((name, id, context) -> {
            return I.xml("<" + name + "/>");
        }, null, (follower, current) -> {
            if (follower instanceof Style) {
                Style style = (Style) follower;
                for (String className : style.className()) {
                    current.addClass(className);
                }
            } else {
                follower.accept(current);
            }
        });
    }

    /**
     * Build HTML.
     */
    protected abstract void declare();

    protected final void $(HTML html) {
        html.declare();

        for (XML xml : html.root) {
            $(xml);
        }
    }

    /**
     * <p>
     * accept node attribute with name.
     * </p>
     * 
     * @param name An attribute name.
     */
    protected final Consumer attr(Object name) {
        return attr(name, null);
    }

    /**
     * accept node attribute with name.
     * 
     * @param name An attribute name.
     */
    protected final Consumer<XML> attr(Object name, Object value) {
        return parent -> {
            if (name != null) {
                String n = String.valueOf(name);

                if (!n.isEmpty()) {
                    parent.attr(n, String.valueOf(value));
                }
            }
        };
    }

    /**
     * Accept child node.
     * 
     * @param child
     * @return
     */
    protected final Consumer<XML> xml(XML child) {
        return parent -> parent.append(child);
    }

    /**
     * Build child node.
     * 
     * @param child
     * @return
     */
    protected final Consumer<XML> xml(Variable<XML> child) {
        return parent -> child.to(parent::append);
    }

    protected final Consumer<XML> svg(String type) {
        return parent -> {
            $("svg", attr("viewBox", "0 0 24 24"), Styles.AnimatedSVG, () -> {
                $("use", attr("href", "main.svg#" + type));
            });
        };
    }

    /**
     * Build code node.
     * 
     * @param text A text.
     */
    protected final Consumer<XML> code(Object text) {
        return parent -> parent.child("code").append(text);
    }

    /**
     * Build text node.
     * 
     * @param text A text.
     */
    protected final Consumer<XML> text(Object text) {
        return parent -> {
            parent.append(parent.to().getOwnerDocument().createTextNode(String.valueOf(text)));
        };
    }

    /**
     * Shorthand method to write stylesheet link tag.
     * 
     * @param uri URI to css.
     */
    protected final void stylesheet(String uri) {
        $("link", attr("rel", "stylesheet"), attr("href", uri));
    }

    /**
     * Shorthand method to write stylesheet link tag.
     * 
     * @param uri URI to css.
     */
    protected final void stylesheetAsync(String uri) {
        $("link", attr("rel", "stylesheet"), attr("href", uri), attr("media", "print"), attr("onload", "this.media='all'"));
    }

    /**
     * Build CSS file and return the path of the generated file.
     * 
     * @param styles A style definition class to write.
     */
    protected final void stylesheet(String path, Class<? extends StyleDSL> styles) {
        $("link", attr("rel", "stylesheet"), attr("href", SiteBuilder.current.buildCSS(path, styles)));
    }

    /**
     * Build CSS file and return the path of the generated file.
     * 
     * @param styles A style definition class to write.
     */
    protected final void stylesheet(String path, StyleDeclarable styles) {
        $("link", attr("rel", "stylesheet"), attr("href", SiteBuilder.current.buildCSS(path, styles)));
    }

    /**
     * Shorthand method to write script tag.
     * 
     * @param uri URI to script.
     */
    protected final void script(String uri) {
        $("script", attr("src", uri));
    }

    /**
     * Shorthand method to write script tag.
     * 
     * @param uri URI to script.
     */
    protected final void scriptAsync(String uri) {
        $("script", attr("src", uri), attr("async", true));
    }

    /**
     * Shorthand method to write script tag.
     * 
     * @param uri URI to script.
     */
    protected final void module(String uri) {
        $("script", attr("src", uri), attr("type", "module"));
    }

    /**
     * Shorthand method to write script tag.
     * 
     * @param uri URI to script.
     */
    protected final void moduleAsync(String uri) {
        $("script", attr("src", uri), attr("async", true), attr("type", "module"));
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