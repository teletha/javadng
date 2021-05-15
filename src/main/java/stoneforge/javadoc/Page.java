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
                stylesheet("https://cdn.jsdelivr.net/gh/highlightjs/cdn-release/build/styles/zenburn.min.css");
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
                            $("a", attr("href", "/doc/" + info.children().get(0).id() + ".html"), text(info.title()));
                        }
                        $("a", attr("href", "/api/"), text("API"));
                        $("a", text("Community"));
                    });
                });

                $("main", Styles.MainArea, () -> {
                    // =============================
                    // Left Side Navigation
                    // =============================
                    $("nav", Styles.Navigation, () -> {
                        $("div");
                    });

                    // =============================
                    // Main Contents
                    // =============================
                    $("article", attr("id", "Article"), Styles.Contents, () -> {
                        if (info != null) {
                            declareContents();
                        }
                    });

                    // =============================
                    // Right Side Navigation
                    // =============================
                    $("aside", attr("id", "SubNavi"), Styles.SubNavigation, () -> {
                        $("div", Styles.SubNavigationStickyBlock, () -> {
                            if (info != null) {
                                declareSubNavigation();
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

    protected abstract void declareSubNavigation();

    /**
     * Style definition.
     */
    private interface Styles extends StyleDSL, BaseStyle {

        Numeric NavigationWidth = Numeric.of(17, vw);

        Style workbench = () -> {
            background.color(Color.rgb(235, 246, 247)).image(BackgroundImage.drawSlash(Color.rgb(220, 222, 225, 0.7), 3)).repeat();
            font.size(FontSize).family(theme.baseFont).color(theme.front).lineHeight(LineHeight);
        };

        Style HeaderArea = () -> {
            background.color(Color.Inherit).image(BackgroundImage.inherit()).repeat();
            position.sticky().top(0, rem);
            display.width(MaxWidth).height(HeaderHeight).zIndex(10).flex().alignItems.baseline();
            margin.auto();
            padding.top(22, px);
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

        Style Navigation = () -> {
            flexItem.basis(NavigationWidth).shrink(0).alignSelf.start();
            position.sticky().top(80, px);

            $.child(() -> {
                padding.top(BaseStyle.BlockVerticalGap);
                margin.bottom(1.6, rem);

                $.child().child(() -> {
                    margin.bottom(BaseStyle.BlockVerticalGap);
                });
            });

            $.select("#DocNavi", () -> {
                font.size(1.2, em).family(theme.condensedFont).color(Color.hsl(0, 0, 30));

                $.select(".doc", () -> {
                    listStyle.none();

                    $.select("li", () -> {
                        padding.vertical(0.25, em);

                        $.with(".active", () -> {
                            $.select(".sub", () -> {
                                display.block();
                            });
                        });
                    });
                });

                $.select(".sub", () -> {
                    display.none();
                    listStyle.none();
                    font.size(0.85, em).color(Color.hsl(0, 0, 45)).lineHeight(1.3);
                    border.left.solid().width(1, px).color(Color.hsl(0, 0, 65));
                    margin.left(10, px).vertical(0.5, em);
                    padding.left(Numeric.of(12, px).plus(1, em));
                });

                $.select(".foot", () -> {
                    listStyle.none();
                    padding.left(0.2, em);
                    font.style.italic();

                    $.select("svg", () -> {
                        display.width(18, px).height(18, px);
                        margin.right(0, px);
                    });
                });

                $.select("a", () -> {
                    display.block();
                    text.decoration.none();

                    $.with(".now", () -> {
                        font.color(theme.accent);
                    });

                    $.select("svg", () -> {
                        display.width(20, px).height(20, px);
                        text.verticalAlign.middle();
                        margin.top(-2, px).right(1, em);
                    });
                });
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