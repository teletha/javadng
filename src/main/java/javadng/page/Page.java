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

import javadng.HTML;
import javadng.design.JavadngStyleDSL;
import javadng.parser.ClassInfo;
import kiss.I;
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
            $("body", S.Body, () -> {
                // =============================
                // Top Navigation
                // =============================
                String published = model.repository().getLatestPublishedDate();

                $("header", S.HeaderArea, () -> {
                    $("h1", S.HeaderTitle, attr("date", published), attr("ver", model.version()), code(model.product()));
                    $("nav", S.HeaderNav, () -> {
                        for (ClassInfo info : I.signal(model.docs).map(ClassInfo::outermost).toSet()) {
                            $("a", href("doc/" + info.children().get(0).id() + ".html"), svg("text"), text("Document"));
                        }
                        $("a", href("api/"), svg("package"), code("API"));
                        $("a", href(model.repository().locateCommunity()), attr("target", "_blank"), svg("user"), text("Community"));
                        $("a", href("doc/changelog.html"), svg("activity"), text("Activity"));
                        $("a", href(model.repository().locate()), attr("target", "_blank"), svg("github"), text("Repository"));
                    });
                    $("div", id("ViewMode"), S.ViewMode, () -> {
                        $("a", id("light"), title("Change to a brighter color scheme"), () -> {
                            $(svg("sun"));
                        });

                        $("a", id("dark"), title("Change to a darker color scheme"), () -> {
                            $(svg("moon"));
                        });
                    });
                });

                $("main", S.MainArea, () -> {
                    // =============================
                    // Left Side Navigation
                    // =============================
                    $("nav", S.Navigation, () -> {
                        $("div");
                    });

                    // =============================
                    // Main Contents
                    // =============================
                    $("article", id("Article"), S.Contents, () -> {
                        if (contents != null) {
                            declareContents();
                        }
                    });

                    // =============================
                    // Right Side Navigation
                    // =============================
                    $("aside", id("SubNavi"), S.SubNavigation, () -> {
                        $("div", S.SubNavigationStickyBlock, () -> {
                            if (contents != null) {
                                declareSubNavigation();
                            }
                        });
                    });
                });

                script("root.js", model.data);
                module("main.js");
            });
        });

    }

    protected abstract void declareContents();

    protected abstract void declareSubNavigation();

    /**
     * Style definition.
     */
    private interface S extends JavadngStyleDSL {

        Numeric HeaderMinWidth = Numeric.of(300, px);

        Numeric NavigationWidth = Numeric.of(17, vw);

        Style HeaderArea = () -> {
            background.color(Color.Inherit).image(BackgroundImage.inherit()).repeat();
            position.sticky().top(0, rem);
            display.minWidth(HeaderMinWidth)
                    .maxWidth(JavadngStyleDSL.MaxWidth)
                    .minHeight(JavadngStyleDSL.HeaderHeight)
                    .zIndex(10)
                    .flex().alignItems.center().wrap.enable();
            margin.auto().bottom(JavadngStyleDSL.HeaderBottomMargin);
            padding.top(22, px);
            border.bottom.color(Theme.primary).width(1, px).solid();
        };

        Style HeaderTitle = () -> {
            font.size(2.5, rem).family(Theme.title).weight.normal().color(Theme.primary);
            margin.right(3.5, rem);
            display.flex().wrap.disable();

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
            display.minWidth(HeaderMinWidth);

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

            $.when(JavadngStyleDSL.Small, () -> {
                display.none();
            });
        };

        Style Root = Style.named("html.light #light svg, html.dark #dark svg", () -> {
            fill.color(Color.hsl(55, 100, 75));
        });

        Style MainArea = () -> {
            display.maxWidth(JavadngStyleDSL.MaxWidth).flex().direction.row();
            margin.auto();

            $.when(JavadngStyleDSL.Small, () -> {
                display.flex().wrap.enable();
            });
        };

        Style Navigation = () -> {
            font.size(0.97, rem);
            display.maxWidth(JavadngStyleDSL.MaxNaviWidth);
            flexItem.basis(NavigationWidth).shrink(0).alignSelf.start();
            position.sticky().top(JavadngStyleDSL.HeaderHeight.plus(15, px));
            padding.top(JavadngStyleDSL.BlockVerticalGap);
            margin.bottom(1.6, rem);

            $.when(JavadngStyleDSL.Small, () -> {
                flexItem.order(2);
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

        Style Contents = () -> {
            Numeric gap = Numeric.of(2, rem);
            display.maxWidth(JavadngStyleDSL.MaxWidth.subtract(JavadngStyleDSL.MaxNaviWidth)
                    .subtract(JavadngStyleDSL.MaxSubNaviWidth)
                    .subtract(gap.multiply(2)));
            flexItem.grow(1);
            margin.horizontal(gap);
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

        Style SubNavigation = () -> {
            display.width(JavadngStyleDSL.MaxSubNaviWidth);
            font.size(0.85, rem);

            $.when(JavadngStyleDSL.Small, () -> {
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

        Style Body = () -> {
            background.color(Theme.back).image(Theme.backImage).repeat();
            font.size(Theme.font).family(Theme.base).color(Theme.front.lighten(Theme.back, 5)).lineHeight(Theme.line);
            margin.horizontal(35, px).bottom(14, px);
            display.grid().templateColumns.size(0.6, 1.6, 0.6, fr).templateRows.size(0.4, 1.6, fr)
                    .gap(0, px)
                    .templateAreas(HeaderArea, HeaderArea, HeaderArea)
                    .templateAreas(Navigation, MainArea, SubNavigation);
        };
    }
}