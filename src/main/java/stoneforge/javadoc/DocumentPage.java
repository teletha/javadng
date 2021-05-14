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

import javax.lang.model.element.Modifier;

import kiss.XML;
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
        if (info.hasDocument()) {
            $("section", Styles.Section, Styles.JavadocComment, () -> {
                write(info);
            });
        }

        for (ClassInfo child : info.children(Modifier.PUBLIC)) {
            if (child.hasDocument()) {
                $("section", attr("id", child.id()), Styles.Section, Styles.JavadocComment, () -> {
                    write(child);

                    for (ClassInfo foot : child.children(Modifier.PUBLIC)) {
                        if (foot.hasDocument()) {
                            $("section", attr("id", foot.id()), Styles.JavadocComment, () -> {
                                write(foot);
                            });
                        }
                    }
                });
            }
        }
    }

    private void write(ClassInfo info) {
        XML doc = info.createComment();
        XML heading = doc.find("h,h1,h2,h3,h4,h5,h6,h7").first().remove();

        $("header", Styles.JavadocComment, styles.header, () -> {
            $(xml(heading));
            $("div", styles.meta, () -> {
                String editor = model.editor().apply(info.filePath(), info.documentLine());
                if (editor != null) {
                    $("svg", attr("class", "svg"), attr("viewBox", "0 0 24 24"), styles.edit, () -> {
                        $("use", attr("href", "/main.svg#twitter"));
                    });
                    $("a", attr("href", editor), () -> {
                        $("svg", attr("class", "svg"), attr("viewBox", "0 0 24 24"), styles.edit, () -> {
                            $("use", attr("href", "/main.svg#edit"));
                        });
                    });
                    $("svg", attr("class", "svg"), attr("viewBox", "0 0 24 24"), styles.edit, () -> {
                        $("use", attr("href", "/main.svg#help"));
                    });
                }
            });
        });
        $(xml(doc));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareSubNavigation() {
    }

    interface styles extends StyleDSL {

        Style header = () -> {
            position.sticky().top(79, px);
            background.color("white");
            display.zIndex(2);
        };

        Style meta = () -> {
            margin.top(-33, px);
            text.align.right();
        };

        Style edit = () -> {
            stroke.width(2, px);
        };
    }
}
