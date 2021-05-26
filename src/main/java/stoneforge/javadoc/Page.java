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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kiss.I;
import stoneforge.HTML;
import stoneforge.javadoc.analyze.ClassInfo;
import stylist.Style;
import stylist.StyleDSL;
import stylist.property.Background.BackgroundImage;
import stylist.value.Color;
import stylist.value.Numeric;

public abstract class Page<T> extends HTML {

    protected final JavadocModel model;

    protected final T contents;

    /**
     * @param model
     * @param info
     */
    protected Page(JavadocModel model, T info) {
        this.model = model;
        this.contents = info;
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
                script("https://cdn.jsdelivr.net/npm/uhtml@2.7.3/min.js");
                script("https://unpkg.com/mithril/mithril.min.js");
                script("https://unpkg.com/vue/dist/vue.min.js");
                stylesheetAsync("https://cdn.jsdelivr.net/gh/highlightjs/cdn-release/build/styles/zenburn.min.css");
                scriptAsync("highlight.js");
                stylesheet("/main.css");
                $("script", text("//")); // start js engine eagerly
            });
            $("body", styles.Workbench, () -> {
                // =============================
                // Top Navigation
                // =============================
                String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());

                $("header", styles.HeaderArea, () -> {
                    $("h1", styles.HeaderTitle, attr("date", date), attr("ver", model.version()), code(model.product()));
                    $("nav", styles.HeaderNav, () -> {
                        for (ClassInfo info : I.signal(model.docs).map(ClassInfo::outermost).toSet()) {
                            $("a", attr("href", "/doc/" + info.children().get(0).id() + ".html"), svg("text"), text(info.title()));
                        }
                        $("a", attr("href", "/api/"), svg("package"), code("API"));
                        $("a", attr("href", model.repository()
                                .locateCommunity()), attr("target", "_blank"), svg("user"), text("Community"));
                        $("a", attr("href", "/doc/changelog.html"), svg("activity"), text("Activity"));
                        $("a", attr("href", model.repository().locate()), attr("target", "_blank"), svg("github"), text("Repository"));
                    });
                    $("div", attr("id", "ViewMode"), styles.ViewMode, () -> {
                        $("a", attr("id", "light"), attr("title", "Change to a brighter color scheme"), () -> {
                            $(svg("sun"));
                        });

                        $("a", attr("id", "dark"), attr("title", "Change to a darker color scheme"), () -> {
                            $(svg("moon"));
                        });
                    });
                });

                $("main", styles.MainArea, () -> {
                    // =============================
                    // Left Side Navigation
                    // =============================
                    $("nav", styles.Navigation, () -> {
                        $("div");
                    });

                    // =============================
                    // Main Contents
                    // =============================
                    $("article", attr("id", "Article"), styles.Contents, () -> {
                        if (contents != null) {
                            declareContents();
                        }
                    });

                    // =============================
                    // Right Side Navigation
                    // =============================
                    $("aside", attr("id", "SubNavi"), styles.SubNavigation, () -> {
                        $("div", styles.SubNavigationStickyBlock, () -> {
                            if (contents != null) {
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
    private interface styles extends StyleDSL, StyleConstants {

        Numeric NavigationWidth = Numeric.of(17, vw);

        Style Workbench = () -> {
            background.color(theme.back).image(theme.backImage).repeat();
            font.size(theme.font).family(theme.base).color(theme.front).lineHeight(theme.line);
        };

        Style HeaderArea = () -> {
            background.color(Color.Inherit).image(BackgroundImage.inherit()).repeat();
            position.sticky().top(0, rem);
            display.width(MaxWidth).height(HeaderHeight).zIndex(10).flex().alignItems.center();
            margin.auto();
            padding.top(22, px);
            border.bottom.color(theme.primary).width(1, px).solid();
        };

        Style HeaderTitle = () -> {
            font.size(2.5, rem).family(theme.title).weight.normal().color(theme.primary);

            $.after(() -> {
                content.attr("date").text("\\000AVersion\\00A0").attr("ver");
                font.size(0.8, rem).lineHeight(1.1).color(theme.front).family(theme.condensed);
                display.inlineBlock();
                padding.left(1.1, rem);
                text.whiteSpace.pre();
            });
        };

        Style HeaderNav = () -> {
            margin.left(4, rem);

            $.child(() -> {
                font.size(11, px);
                display.inlineFlex().alignItems.center().direction.column();
                padding.horizontal(1.8, rem);
                margin.top(-4, px);
                text.decoration.none();
                transition.duration(0.2, s).whenever();

                $.select("svg", () -> {
                    Numeric size = Numeric.of(26, px);

                    display.width(size).height(size);
                    stroke.color(theme.front.lighten(theme.back, -15)).width(1.2, px);
                    transition.duration(0.2, s).whenever();
                });

                $.hover(() -> {
                    text.decoration.none();
                    font.color(theme.link);

                    $.select("svg", () -> {
                        stroke.color(theme.link).width(2, px);
                        transform.translateY(-4, px);
                    });
                });
            });
        };

        Style ViewMode = () -> {
            display.flex().justifyContent.end();
            flexItem.alignSelf.end().grow(2);
            margin.bottom(10, px).right(2, rem);

            $.select("a", () -> {
                display.width(20, px).height(20, px);
                margin.left(1, em);
            });

            $.select("svg", () -> {
                display.width(20, px).height(20, px);
            });
        };

        Style Root = Style.named("html.light #light svg, html.dark #dark svg", () -> {
            fill.color(Color.hsl(55, 100, 75));
        });

        Style MainArea = () -> {
            display.width(MaxWidth).flex().direction.row();
            margin.auto();
        };

        Style Navigation = () -> {
            flexItem.basis(NavigationWidth).shrink(0).alignSelf.start();
            position.sticky().top(80, px);
            padding.top(StyleConstants.BlockVerticalGap);
            margin.bottom(1.6, rem);

            $.select("#APINavi", () -> {
                $.select(">*", () -> {
                    margin.bottom(StyleConstants.BlockVerticalGap);
                });
            });

            $.select("#DocNavi", () -> {
                font.size(1.2, em).family(theme.condensed).color(theme.front.lighten(theme.back, -15));

                $.select(".doc", () -> {
                    margin.bottom(0.5, em);

                    $.select("li", () -> {
                        padding.vertical(0.25, em);
                    });
                });

                $.select(".sub", () -> {
                    display.height(0, px);
                    listStyle.none();
                    font.size(0.85, em).color(theme.front.lighten(theme.back, -7));
                    border.left.solid().width(1, px).color(Color.hsl(0, 0, 65));
                    margin.left(10, px);
                    overflow.y.hidden();

                    transition.duration(0.5, s).whenever();

                    $.select("a", () -> {
                        $.select(".foot", () -> {
                            font.style.italic();
                            padding.left(1.1, em);
                        });

                        $.select("svg", () -> {
                            display.width(16, px).height(16, px);
                            margin.left(4, px).right(5, px);
                            stroke.width(2, px).transparent();

                            transition.duration(0.25, s).whenever();
                            transform.translateX(-16, px);
                        });

                        $.with(".now", () -> {
                            $.select("svg", () -> {
                                stroke.color(theme.front.lighten(20));
                                transform.translateX(0, px);
                            });
                        });

                        $.hover(() -> {
                            $.select("svg", () -> {
                                stroke.color(theme.link);
                                transform.translateX(0, px);
                            });
                        });

                        $.active(() -> {
                            $.select("svg", () -> {
                                transform.translateX(4, px);
                            });
                        });
                    });
                });

                $.select("a", () -> {
                    display.block();
                    text.decoration.none();

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