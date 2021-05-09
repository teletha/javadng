/*
 * Copyright (C) 2020 stoneforge Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package stoneforge.javadoc;

import java.util.List;

import javax.lang.model.element.Modifier;

import stoneforge.HTML;
import stoneforge.javadoc.analyze.ClassInfo;
import stylist.Style;
import stylist.StyleDSL;

class DocumentSubNavigationView extends HTML {

    private final ClassInfo info;

    /**
     * @param info
     */
    public DocumentSubNavigationView(ClassInfo info) {
        this.info = info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declare() {

        $("ol", () -> {
            for (ClassInfo child : info.children(Modifier.PUBLIC)) {
                $("li", () -> {
                    $("a", attr("href", "#" + child.id()), text(child.title()));

                    List<ClassInfo> grands = child.children(Modifier.PUBLIC);
                    if (!grands.isEmpty()) {
                        $("ol", () -> {
                            for (ClassInfo grand : grands) {
                                $("li", () -> {
                                    $("a", attr("href", "#" + grand.id()), text(grand.title()));
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private interface styles extends StyleDSL, BaseStyle {

        Style Title = () -> {
            margin.top(0.9, rem);
            font.weight.bold().size(1, rem);
        };

        Style Return = () -> {
            font.color(theme.secondary.lighten(-30));

            $.before(() -> {
                content.text(":");
                padding.horizontal(0.3, em);
            });
        };
    }
}