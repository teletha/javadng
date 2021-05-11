/*
 * Copyright (C) 2019 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package stoneforge.javadoc;

import java.util.List;

import javax.lang.model.element.Modifier;

import stoneforge.javadoc.analyze.ClassInfo;
import stylist.Style;
import stylist.StyleDSL;

public class DocumentPage extends Page {

    /**
     * @param model
     * @param info
     */
    public DocumentPage(JavadocModel model, ClassInfo info) {
        super(model, info);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareContents() {
        $("section", Styles.Section, () -> {
            $(info.createComment());
        });

        for (ClassInfo child : info.children()) {
            $("section", attr("id", child.id()), Styles.Section, () -> {
                $(child.createComment());

                for (ClassInfo foot : child.children()) {
                    $(foot.createComment());
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareSubNavigation() {
        $("ol", style.top, () -> {
            for (ClassInfo child : info.children(Modifier.PUBLIC)) {
                $("li", () -> {
                    $("a", attr("href", "#" + child.id()), text(child.title()));

                    List<ClassInfo> grands = child.children(Modifier.PUBLIC);
                    if (!grands.isEmpty()) {
                        $("ol", style.sub, () -> {
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

    private interface style extends StyleDSL, BaseStyle {

        Style top = () -> {
            listStyle.inside().none();
            font.lineHeight(2).weight.bold().color(theme.front.opacify(-0.3));
        };

        Style sub = () -> {
            listStyle.inside().none();
            padding.left(1, em);
            font.weight.normal();
        };
    }
}
