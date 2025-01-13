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

import static stylist.value.Numeric.*;

import javadng.HTML;
import javadng.design.JavadngStyleDSL;
import javadng.parser.ClassInfo;
import kiss.I;
import stylist.Query;
import stylist.Style;
import stylist.Stylist;
import stylist.property.Background.BackgroundImage;
import stylist.value.Color;
import stylist.value.Font;
import stylist.value.Numeric;

public abstract class Page<T> extends HTML {

    protected final JavadocModel model;

    protected final T contents;

    private final String base;

    /**
     * @param depth
     * @param model
     * @param content
     */
    protected Page(int depth, JavadocModel model, T content) {
        this.model = model;
        this.contents = content;
        this.base = "../".repeat(depth);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void declare() {
        $("html", attr("lang", "en"), () -> {
            $("head", () -> {
                $("meta", charset("UTF-8"));
                $("meta", name("viewport"), content("width=device-width, initial-scale=1"));
                $("meta", name("description"), content("Explains how to use " + model.product() + " and its API. " + model.description()));
                $("link", rel("preconnect"), href("https://cdn.jsdelivr.net"));
                $("link", rel("preconnect"), href("https://fonts.googleapis.com"));
                $("link", rel("preconnect"), href("https://fonts.gstatic.com"), attr("crossorigin"));
                for (Font font : Font.fromGoogle()) {
                    stylesheetAsync(font.uri);
                }
                $("title", text(model.product() + " API"));
                $("base", href(base));
                module("mimic.js");
                stylesheet(Stylist.NormalizeCSS);
                stylesheet("main.css");
            });
            $("body", style.body, () -> {
                // =============================
                // Top Navigation
                // =============================
                String published = model.repository().getLatestPublishedDate();

                $("header", style.header, () -> {
                    $("h1", style.HeaderTitle, attr("date", published), attr("ver", model.version()), code(model.product()));
                    $("nav", style.HeaderNav, () -> {
                        for (ClassInfo info : I.signal(model.docs).map(ClassInfo::outermost).toSet()) {
                            $("a", href("doc/" + info.children().get(0).id() + ".html"), svg("text"), text("Document"));
                        }
                        $("a", href("api/"), svg("package"), code("API"));
                        $("a", href(model.repository().locateCommunity()), attr("target", "_blank"), svg("user"), text("Community"));
                        $("a", href("doc/changelog.html"), svg("activity"), text("Activity"));
                        $("a", href(model.repository().locate()), attr("target", "_blank"), svg("github"), text("Repository"));
                    });
                    $("div", id("ViewMode"), style.ViewMode, () -> {
                        $("a", id("light"), title("Change to a brighter color scheme"), () -> {
                            $(svg("sun"));
                        });

                        $("a", id("dark"), title("Change to a darker color scheme"), () -> {
                            $(svg("moon"));
                        });
                    });
                });

                // =============================
                // Left Side Navigation
                // =============================
                $("nav", style.nav, () -> {
                    $("div");
                });

                // =============================
                // Main Contents
                // =============================
                $("article", id("Article"), style.article, () -> {
                    if (contents != null) {
                        declareContents();
                    }
                });

                // =============================
                // Right Side Navigation
                // =============================
                $("aside", id("SubNavi"), style.aside, () -> {
                    $("div", style.SubNavigationStickyBlock, () -> {
                        if (contents != null) {
                            declareSubNavigation();
                        }
                    });
                });

                $("footer", style.footer, () -> {
                });
            });

            script("root.js", model.data);
            module("main.js");
        });

    }

    protected abstract void declareContents();

    protected abstract void declareSubNavigation();

    /**
     * Style definition.
     */
    private interface style extends JavadngStyleDSL {

        Query BASE = Query.all().width(0, 800, px);

        Query MIDDLE = Query.all().width(800, 1200, px);

        Query LARGE = Query.all().width(1200, px);

        Numeric HeaderMinWidth = Numeric.of(300, px);

        Numeric NavigationWidth = Numeric.of(17, vw);

        Style nav = () -> {
            font.size(0.97, rem);
            position.sticky().top(JavadngStyleDSL.HeaderHeight.plus(20, px));
            margin.bottom(1.6, rem);

            $.when(BASE, () -> {
                display.none();
            });

            $.select("#APINavi", () -> {
                $.select(">*", () -> {
                    margin.bottom(JavadngStyleDSL.BlockVerticalGap);
                });
            });

            $.select("#DocNavi", () -> {
                font.size(1.1, em).color(Theme.front.lighten(Theme.back, -15)).letterSpacing(-0.5, px).lineHeight(1.6);

                $.select(".doc", () -> {
                    margin.bottom(1, rem);

                    $.select("li", () -> {
                        padding.vertical(0.35, rem);
                    });
                });

                $.select(".sub", () -> {
                    display.height(0, px);
                    listStyle.none();
                    font.size(0.9, em).color(Theme.front.lighten(Theme.back, 10));
                    border.left.solid().width(1, px).color(Color.hsl(0, 0, 65));
                    overflow.y.hidden();

                    transition.duration(0.3, s).whenever();

                    $.select("a", () -> {
                        $.select(".foot", () -> {
                            padding.left(0.8, rem);
                        });

                        $.select("svg", () -> {
                            display.width(16, px).height(16, px);
                            margin.left(-16, px);
                            stroke.width(2, px).transparent();

                            transition.duration(0.25, s).whenever();
                            transform.translateX(0, px);
                        });

                        $.with(".now", () -> {
                            font.color(Theme.accent);
                            $.select("svg", () -> {
                                stroke.color(Theme.accent);
                                transform.translateX(10, px);
                            });
                        });

                        $.hover(() -> {
                            $.select("svg", () -> {
                                stroke.color(Theme.accent);
                                transform.translateX(10, px);
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
                    text.decoration.none().whiteSpace.pre();
                    padding.vertical(0.13, rem);

                    $.select("svg", () -> {
                        display.width(20, px).height(20, px);
                        text.verticalAlign.middle();
                        margin.top(-2, px).right(1, em);
                    });
                });
            });
        };

        Style ViewMode = () -> {

            $.select("a", () -> {
                display.width(20, px).height(20, px);
                margin.right(1, em);
            });

            $.select("svg", () -> {
                display.width(20, px).height(20, px);
            });

            $.when(JavadngStyleDSL.Small, () -> {
                display.none();
            });
        };

        Style HeaderTitle = () -> {
            font.size(2.5, rem).family(Theme.title).weight.normal().color(Theme.primary);

            $.after(() -> {
                content.attr("date").text("\\000AVersion\\00A0").attr("ver");
                font.size(0.8, rem).lineHeight(1.3).color(Theme.front).family(Theme.base).letterSpacing(-0.5, px);
                display.inlineBlock();
                padding.left(1.1, rem).bottom(1, rem);
                text.whiteSpace.pre();
                flexItem.alignSelf.end();
            });
        };

        Style HeaderNav = () -> {
            margin.auto();

            $.child(() -> {
                font.size(12, px).color(Theme.front);
                display.width(90, px).inlineFlex().alignItems.center().direction.column();
                padding.horizontal(1.8, rem).vertical(0.5, rem);
                margin.top(-4, px);
                text.decoration.none().whiteSpace.nowrap();
                transition.duration(0.2, s).whenever();

                $.select("svg", () -> {
                    Numeric size = Numeric.of(26, px);

                    display.width(size).height(size);
                    stroke.color(Theme.front.lighten(Theme.back, -15)).width(1.2, px);
                    transition.duration(0.2, s).whenever();
                });

                $.hover(() -> {
                    text.decoration.none();
                    font.color(Theme.link);

                    $.select("svg", () -> {
                        stroke.color(Theme.link).width(2, px);
                        transform.translateY(-4, px);
                    });
                });
            });
        };

        Style header = () -> {
            background.color(Color.Inherit).image(BackgroundImage.inherit()).repeat();
            position.sticky().top(0, rem);
            display.zIndex(10).grid().templateColumns.size(0.25, 0.5, 0.25, fr).templateAreas(HeaderTitle, HeaderNav, ViewMode);
            padding.top(22, px);
            border.bottom.color(Theme.primary).width(1, px).solid();
        };

        Style Root = Style.named("html.light #light svg, html.dark #dark svg", () -> {
            fill.color(Color.hsl(55, 100, 75));
        });

        Style article = () -> {
            font.letterSpacing(-0.025, rem);
            position.relative();

            $.after(() -> {
                content.text("");
                position.absolute().top(JavadngStyleDSL.BlockVerticalGap).left(0, px);
                display.width(100, percent).height(100, percent).zIndex(5).opacity(0).block();
                background.color(Theme.surface);
                pointerEvents.none();

                $.transit().ease().duration(0.15, s).when().with(".fadeout", () -> {
                    display.opacity(1);
                });
            });
        };

        Style aside = () -> {
            display.width(JavadngStyleDSL.MaxSubNaviWidth);
            font.size(0.85, rem);

            $.when(BASE, MIDDLE, () -> {
                display.none();
            });
        };

        Style SubNavigationStickyBlock = () -> {
            position.sticky().top(JavadngStyleDSL.HeaderHeight.plus(15, px));
            display.block()
                    .height(Numeric.of(90, vh).subtract(JavadngStyleDSL.HeaderHeight))
                    .maxWidth(JavadngStyleDSL.RightNavigationWidth);
            overflow.auto().scrollbar.thin();
            text.whiteSpace.nowrap();

            $.hover(() -> {
                overflow.y.auto();
            });

            $.child().child(() -> {
                padding.vertical(0.25, em);
            });
        };

        Style footer = () -> {
        };

        Style body = () -> {
            background.color(Theme.back).image(Theme.backImage).repeat();
            font.size(Theme.font).family(Theme.base).color(Theme.front.lighten(Theme.back, 5)).lineHeight(Theme.line);
            margin.horizontal(35, px).bottom(14, px);

            $.when(BASE, () -> {
                display.grid().gap(20, px).templateAreas(header).templateAreas(nav).templateAreas(article).templateAreas(aside);
            });

            $.when(MIDDLE, () -> {
                display.grid().templateColumns.size(auto(1, fr), auto(85, ch)).templateRows.size(80, px, 1, fr).alignItems.start()
                        .gap(0.5, rem, 2, rem)
                        .templateAreas(header, header)
                        .templateAreas(nav, article);
            });

            $.when(LARGE, () -> {
                display.grid().templateColumns.size(auto(1, fr), auto(85, ch), auto(1, fr)).templateRows.size(80, px, 1, fr).alignItems
                        .start()
                        .gap(0.5, rem, 2, rem)
                        .templateAreas(header, header, header)
                        .templateAreas(nav, article, aside);
            });
        };
    }
}