/*
 * Copyright (C) 2019 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.parser;

import javax.lang.model.element.Modifier;

import javadng.parser.analyze.ClassInfo;
import kiss.I;
import kiss.XML;
import stylist.Style;
import stylist.StyleDSL;
import stylist.value.Numeric;

public class DocumentPage extends Page<ClassInfo> {

    /**
     * @param depth
     * @param model
     * @param info
     */
    public DocumentPage(int depth, JavadocModel model, ClassInfo info) {
        super(depth, model, info);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareContents() {
        if (contents.hasDocument()) {
            $("section", Styles.Section, Styles.JavadocComment, () -> {
                write(contents, styles.SectionLevel1);
            });
        }

        for (ClassInfo child : contents.children(Modifier.PUBLIC)) {
            if (child.hasDocument()) {
                $("section", attr("id", child.id()), Styles.Section, Styles.JavadocComment, () -> {
                    write(child, styles.SectionLevel1);

                    for (ClassInfo foot : child.children(Modifier.PUBLIC)) {
                        if (foot.hasDocument()) {
                            $("section", attr("id", foot.id()), Styles.JavadocComment, styles.foot, () -> {
                                write(foot, styles.SectionLevel2);
                            });
                        }
                    }
                });
            }
        }
    }

    private void write(ClassInfo info, Style additionalStyle) {
        XML doc = info.createComment();
        XML heading = doc.find("h,h1,h2,h3,h4,h5,h6,h7").first().remove();

        $("header", Styles.JavadocComment, additionalStyle, () -> {
            $(xml(heading.size() != 0 ? heading : I.xml("h" + info.nestLevel()).text(info.title())));
            $("div", styles.meta, () -> {
                $("a", attr("class", "perp"), styles.icon, () -> {
                    $(svg("copy"));
                });

                $("a", attr("class", "tweet"), styles.icon, () -> {
                    $(svg("twitter"));
                });

                String editor = model.repository().locateEditor(info.filePath(), info.documentLine());
                if (editor != null) {
                    $("a", attr("href", editor), attr("class", "edit"), styles.icon, () -> {
                        $(svg("edit"));
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

    interface styles extends StyleDSL, StyleConstants {

        Numeric IconSize = Numeric.of(14, px);

        Style SectionLevel1 = () -> {
            position.relative();
            content.counterIncrement("top");

            $.before(() -> {
                font.family(theme.icon).size(54, px).color(theme.surface);
                position.absolute().top(-20, px).left(-58, px);
                display.zIndex(-1);
                content.text("\\eb39");
                transform.rotate(30, deg);
            });

            $.after(() -> {
                font.family(theme.icon).size(26, px).color(theme.front.opacify(-0.8));
                position.absolute().top(3, px).left(-46, px);
                content.text("\\eaa6");
            });
        };

        Style SectionLevel2 = () -> {
            position.relative();
        };

        Style meta = () -> {
            position.absolute().top(Numeric.of(50, percent).subtract(IconSize.divide(2))).right(IconSize.divide(2));
        };

        Style icon = () -> {
            display.inlineBlock().width(IconSize).height(IconSize);
            font.lineHeight(1);
            margin.left(IconSize);
        };

        Style foot = () -> {
            margin.top(3, rem).bottom(1, rem);
        };
    }
}
