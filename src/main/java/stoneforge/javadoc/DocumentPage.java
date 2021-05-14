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
import stylist.value.Numeric;

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
                    $("svg", attr("class", "svg"), attr("viewBox", "0 0 24 24"), attr("alt", "Copy the permanent link"), styles.edit, () -> {
                        $("use", attr("href", "/main.svg#link"));
                    });
                    $("svg", attr("class", "svg"), attr("viewBox", "0 0 24 24"), styles.edit, () -> {
                        $("use", attr("href", "/main.svg#twitter"));
                    });
                    $("a", attr("href", editor), attr("title", "Edit document"), () -> {
                        $("svg", attr("class", "svg"), attr("viewBox", "0 0 24 24"), styles.edit, () -> {
                            $("use", attr("href", "/main.svg#edit"));
                        });
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

    interface styles extends StyleDSL, BaseStyle {

        Style header = () -> {
            display.zIndex(2);
            position.relative();
        };

        Style meta = () -> {
            position.absolute().top(Numeric.of(50, percent).subtract(10, px)).right(1, em);
        };

        Style icon = () -> {
            display.inlineBlock().width(20, px).height(20, px);
        };

        Style edit = () -> {
            display.width(20, px).height(20, px);
            stroke.width(2, px).color(theme.front.opacify(-0.5));
            margin.right(9, px);

            $.transit().duration(0.5, s).when().hover(() -> {
                stroke.color(theme.front);
            });
        };
    }
}
