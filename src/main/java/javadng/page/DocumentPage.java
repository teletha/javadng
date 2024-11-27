/*
 * Copyright (C) 2024 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.page;

import javax.lang.model.element.Modifier;

import javadng.design.JavadngStyleDSL;
import javadng.design.Styles;
import kiss.I;
import kiss.XML;
import stylist.Style;
import stylist.value.Numeric;

public class DocumentPage extends Page<DocumentProvider> {

    /**
     * @param depth
     * @param model
     * @param content
     */
    public DocumentPage(int depth, JavadocModel model, DocumentProvider content) {
        super(depth, model, content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareContents() {
        try {
            if (contents.hasDocument()) {
                $("section", Styles.Section, Styles.JavadocComment, () -> {
                    write(contents, S.SectionLevel1, true);
                });
            }

            for (DocumentProvider child : contents.children(Modifier.PUBLIC)) {
                if (child.hasDocument()) {
                    $("section", attr("id", child.id()), Styles.Section, Styles.JavadocComment, () -> {
                        write(child, S.SectionLevel1, true);

                        for (DocumentProvider foot : child.children(Modifier.PUBLIC)) {
                            if (foot.hasDocument()) {
                                $("section", attr("id", foot.id()), Styles.JavadocComment, S.foot, () -> {
                                    write(foot, S.SectionLevel2, false);
                                });
                            }
                        }
                    });
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw I.quiet(e);
        }
    }

    private void write(DocumentProvider provider, Style additionalStyle, boolean useIcons) {
        XML doc = provider.document();
        XML heading = doc.find("h,h1,h2,h3,h4,h5,h6,h7").first().remove();

        $("header", Styles.JavadocComment, additionalStyle, () -> {
            $(xml(heading.size() != 0 ? heading : I.xml("h" + provider.nestLevel()).text(provider.title())));
            if (useIcons) {
                $("div", S.meta, () -> {
                    $("span", attr("class", "perp"), S.icon, () -> {
                        $(svg("copy"));
                    });

                    $("a", attr("class", "tweet"), S.icon, () -> {
                        $(svg("twitter"));
                    });

                    String editor = model.repository().locateEditor(provider.filePath(), provider.documentLine());
                    if (editor != null) {
                        $("a", attr("href", editor), attr("class", "edit"), S.icon, () -> {
                            $(svg("edit"));
                        });
                    }
                });
            }
        });
        $(xml(doc));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareSubNavigation() {
    }

    interface S extends JavadngStyleDSL {

        Numeric IconSize = Numeric.of(16, px);

        Style SectionLevel1 = () -> {
            position.relative();
        };

        Style SectionLevel2 = () -> {
            position.relative();
        };

        Style meta = () -> {
            position.absolute().top(Numeric.of(50, percent).subtract(IconSize.divide(2)).subtract(3, px)).right(IconSize.divide(2));
        };

        Style icon = () -> {
            display.inlineBlock().width(IconSize).height(IconSize);
            font.lineHeight(1);
            margin.left(IconSize);
            cursor.pointer();
        };

        Style foot = () -> {
            margin.top(3.6, rem).bottom(1, rem);

            $.prev(SectionLevel1, () -> {
                margin.top(1, rem);
            });
        };
    }
}