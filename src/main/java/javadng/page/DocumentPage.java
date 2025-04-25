/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.page;

import javadng.Document;
import javadng.JavadocModel;
import javadng.design.JavadngDSL;
import javadng.design.Styles;
import kiss.I;
import kiss.XML;
import stylist.Style;
import stylist.value.Numeric;

public class DocumentPage extends Page<Document> {

    /**
     * @param depth
     * @param model
     * @param content
     */
    public DocumentPage(int depth, JavadocModel model, Document content) {
        super(depth, model, content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareContents() {
        try {
            if (contents.hasContents()) {
                $("section", Styles.Section, Styles.JavadocComment, () -> {
                    write(2, contents, S.SectionLevel1, true);
                });
            }

            for (Document child : contents.children()) {
                if (child.hasContents()) {
                    $("div", Styles.Section, () -> {
                        $("section", id(child.id()), Styles.JavadocComment, () -> {
                            write(2, child, S.SectionLevel1, true);
                        });

                        for (Document foot : child.children()) {
                            if (foot.hasContents()) {
                                $("section", id(foot.id()), Styles.JavadocComment, S.foot, () -> {
                                    write(3, foot, S.SectionLevel2, false);
                                });
                            }
                        }
                    });
                }
            }
        } catch (Throwable e) {
            throw I.quiet(e);
        }
    }

    private void write(int level, Document document, Style additionalStyle, boolean useIcons) {
        XML doc = document.contents();
        XML heading = doc.find("h,h1,h2,h3").first().remove();

        $("header", Styles.JavadocComment, additionalStyle, () -> {
            $(xml(heading.size() != 0 ? heading : I.xml("h" + level).text(document.title())));
            if (useIcons) {
                $("div", S.meta, () -> {
                    $("span", clazz("perp"), S.icon, () -> {
                        $(svg("copy"));
                    });

                    $("a", clazz("tweet"), S.icon, () -> {
                        $(svg("twitter"));
                    });

                    document.region().ifPresent(area -> {
                        String editor = model.repository().locateEditor(area);
                        if (editor != null) {
                            $("a", href(editor), clazz("edit"), S.icon, () -> {
                                $(svg("edit"));
                            });
                        }
                    });

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

    interface S extends JavadngDSL {

        Numeric IconSize = Numeric.num(16, px);

        Style SectionLevel1 = () -> {
            position.relative();
        };

        Style SectionLevel2 = () -> {
            position.relative();
        };

        Style meta = () -> {
            position.absolute().top(Numeric.num(50, percent).subtract(IconSize.divide(2)).subtract(3, px)).right(IconSize.divide(2));
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