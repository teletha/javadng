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

import kiss.I;
import stoneforge.HTML;
import stoneforge.javadoc.analyze.ClassInfo;
import stylist.Style;
import stylist.StyleDSL;
import stylist.property.Background.BackgroundImage;
import stylist.value.Color;
import stylist.value.Numeric;

public abstract class Page extends HTML {

    protected final JavadocModel model;

    protected final ClassInfo info;

    /**
     * @param model
     * @param info
     */
    protected Page(JavadocModel model, ClassInfo info) {
        this.model = model;
        this.info = info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declare() {
        $("html", () -> {
            $("head", () -> {
                $("meta", attr("charset", "UTF-8"));
                $("title", text(model.product() + " API"));
                script("https://unpkg.com/vue/dist/vue.min.js");
                stylesheet("https://unpkg.com/vue-select/dist/vue-select.css");
                script("https://unpkg.com/vue-select");
                stylesheet("https://cdn.jsdelivr.net/gh/highlightjs/cdn-release/build/styles/default.min.css");
                script("https://cdn.jsdelivr.net/gh/highlightjs/cdn-release/build/highlight.min.js");
                stylesheet("/main.css");
            });
            $("body", Styles.workbench, () -> {
                // =============================
                // Top Navigation
                // =============================
                $("header", Styles.HeaderArea, () -> {
                    $("h1", Styles.HeaderTitle, code(model.product()));
                    $("nav", Styles.HeaderNav, () -> {
                        for (ClassInfo info : I.signal(model.docs).map(ClassInfo::outermost).toSet()) {
                            $("a", attr("href", "/doc/" + info.id() + ".html"), text(info.title()));
                        }
                        $("a", attr("href", "/api/"), text("API"));
                        $("a", text("Community"));
                    });
                });

                $("main", Styles.MainArea, () -> {
                    // =============================
                    // Left Side Navigation
                    // =============================
                    $("nav", Styles.TypeNavigation, () -> {
                        $("div");
                    });

                    // =============================
                    // Main Contents
                    // =============================
                    $("article", Styles.Contents, () -> {
                        if (info != null) {
                            declareContents();
                        }
                    });

                    // =============================
                    // Right Side Navigation
                    // =============================
                    $("aside", Styles.SubNavigation, () -> {
                        $("div", Styles.SubNavigationStickyBlock, () -> {
                            if (info != null) {
                                $(new SubNavigationView(info));
                            }
                        });
                    });
                });

                script("root.js", model.data);
                script("main.js");
            });
        });
    }

    protected abstract void declareContents();

    /**
     * Style definition.
     */
    private interface Styles extends StyleDSL, BaseStyle {

        Numeric LeftNavigationWidth = Numeric.of(17, vw);

        Style workbench = () -> {
            background.color(Color.rgb(235, 246, 247)).image(BackgroundImage.drawSlash(Color.rgb(220, 222, 225, 0.7), 3)).repeat();
            font.size(FontSize).family(theme.baseFont).color(theme.front).lineHeight(LineHeight);
        };

        Style HeaderArea = () -> {
            background.color(Color.Inherit).image(BackgroundImage.inherit()).repeat();
            position.sticky().top(0, rem);
            display.width(MaxWidth).height(HeaderHeight).zIndex(10).flex().alignItems.baseline();
            margin.auto();
            padding.top(26, px);
            border.bottom.color(theme.primary).width(1, px).solid();
        };

        Style HeaderTitle = () -> {
            font.size(2.5, rem).family(theme.titleFont).weight.normal().color(theme.primary);
        };

        Style HeaderNav = () -> {
            margin.left(3, rem);

            $.child(() -> {
                font.size(1.3, rem);
                display.inlineBlock();
                padding.horizontal(1, rem);
                position.relative();
                text.decoration.none();

                $.hover(() -> {
                    text.decoration.none();

                    $.after(() -> {
                        content.text("");
                        display.block().width(0, px).height(0, px);
                        border.width(8, px).solid().color(Color.Transparent);
                        margin.left(-4, px);
                        border.top.width(0, px);
                        border.bottom.width(6, px);
                        border.bottom.color(theme.primary);
                        position.absolute().top(100, percent).left(50, percent);
                    });
                });
            });
        };

        Style MainArea = () -> {
            display.width(MaxWidth).flex().direction.row();
            margin.auto();
        };

        Style TypeNavigation = () -> {
            flexItem.basis(LeftNavigationWidth).shrink(0);

            $.child(() -> {
                position.sticky().top(HeaderHeight);
                padding.top(BaseStyle.BlockVerticalGap);

                $.child().child(() -> {
                    margin.bottom(BaseStyle.BlockVerticalGap);
                });
            });

            $.select(".el-select", () -> {
                display.width(100, percent);
            });

            $.select(".el-checkbox", () -> {
                display.block();
            });

            $.select("ol", () -> {
                font.size(0.9, rem);
                margin.left(2, rem);
                cursor.pointer();
            });

            $.select("ol ol", () -> {
                font.size(0.9, rem);
                margin.left(1, rem).bottom(0.5, rem);
                listStyle.disclosureClose();
            });

            $.select("ol a", () -> {
                display.block();
            });
        };

        Style Contents = () -> {
            flexItem.grow(1);
            margin.horizontal(2, rem);
        };

        Style SubNavigation = () -> {
            flexItem.basis(RightNavigationWidth).shrink(0);
        };

        Style SubNavigationStickyBlock = () -> {
            position.sticky().top(HeaderHeight);
            display.block().height(Numeric.of(90, vh).subtract(HeaderHeight)).maxWidth(RightNavigationWidth);
            overflow.auto().scrollbar.thin();
            text.whiteSpace.nowrap();

            $.hover(() -> {
                overflow.y.auto();
            });

            $.child().child(() -> {
                padding.vertical(0.15, em);
            });
        };
    }
}