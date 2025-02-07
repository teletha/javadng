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
import javadng.design.JavadngDSL;
import stylist.Query;
import stylist.Style;
import stylist.Stylist;
import stylist.property.Background.BackgroundImage;
import stylist.property.helper.Content;
import stylist.property.helper.Items;
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
            $("body", css.body, () -> {
                // =============================
                // Top Navigation
                // =============================
                String published = model.repository().getLatestPublishedDate();

                $("header", css.header, attr("date", published), attr("ver", model.version()), () -> {
                    $("h1", css.title, code(model.product()));
                    $("nav", css.links, () -> {
                        $("a", href("doc/" + model.rootDoc().id() + ".html"), svg("text"), id("Document"));
                        $("a", href("api/"), svg("package"), id("API"));
                        $("a", href(model.repository().locateCommunity()), attr("target", "_blank"), svg("user"), id("Community"));
                        $("a", href("doc/changelog.html"), svg("activity"), id("Activity"));
                        $("a", href(model.repository().locate()), attr("target", "_blank"), svg("github"), id("Repository"));
                    });
                    $("div", css.controls, () -> {
                        $("a", id("theme"), title("Change color scheme"), () -> {
                            $(svg("sun"));
                            $(svg("moon"));
                        });
                    });
                });

                // =============================
                // Left Side Navigation
                // =============================
                $("nav", css.nav);

                // =============================
                // Main Contents
                // =============================
                $("article", id("Article"), css.article, () -> {
                    if (contents != null) {
                        declareContents();
                    }
                });

                // =============================
                // Right Side Navigation
                // =============================
                $("aside", css.aside, () -> {
                    if (contents != null) {
                        declareSubNavigation();
                    }
                });

                $("footer", css.footer, () -> {
                });
            });

            script("root.js", model.data);
            module("main.js");
        });

    }

    protected abstract void declareContents();

    protected abstract void declareSubNavigation();

    private interface css extends JavadngDSL {

        Numeric HEADER_HEIGHT = Numeric.num(80, px);

        Numeric GAP = Numeric.num(2, ch);

        Numeric BODY_TOP = HEADER_HEIGHT.plus(GAP);

        Numeric BODY_HEIGHT = Numeric.num(100, dvh).subtract(BODY_TOP);

        Query BASE = Query.all().width(0, 800, px);

        Query MIDDLE = Query.all().width(800, 1200, px);

        Query LARGE = Query.all().width(1200, px);

        Style nav = () -> {
            display.width(100, percent);
            position.sticky().top(BODY_TOP);

            $.when(BASE, () -> {
                margin.top(1, rem);
                padding.vertical(1, rem).horizontal(2, rem);
                background.color(Theme.back.opacify(0.9)).image(Theme.backImage);
            });

            $.child(() -> {
                display.grid().align(Content.Start).rowGap(0.5, rem).flowRow();
                font.color(Theme.front.lighten(Theme.back, -15)).letterSpacing(-0.5, px).lineHeight(1.6);

                $.attr("data-hide", "true", () -> {
                    display.none();
                });

                $.lastChild(() -> {
                    display.height(BODY_HEIGHT);
                    overflow.y.auto();
                });
            });

            $.select(".doc", () -> {
                $.select("li", () -> {
                    padding.vertical(0.1, rem);
                });

                $.when(Small, () -> {
                    margin.bottom(0.2, rem);
                });
            });

            $.select(".sub", () -> {
                display.height(0, px);
                font.size(0.9, em).color(Theme.front.lighten(Theme.back, 7));
                overflow.y.hidden();
                transition.duration(0.3, s).whenever();

                $.when(Small, () -> {
                    display.none();
                });

                $.select("a", () -> {
                    padding.left(0.8, rem).vertical(0.3, rem);
                    border.left.solid().width(2, px).color(Color.hsl(0, 0, 65));
                    $.with(".foot", () -> {
                        padding.left(1.6, rem);
                    });

                    $.with(".now", () -> {
                        border.left.color(Theme.accent);
                        background.color(Theme.accent.opacify(-0.9));
                    });

                    $.firstMatch(".now", () -> {
                        border.top.radius(Theme.radius);
                    });

                    $.lastMatch(".now", () -> {
                        border.right.radius(Theme.radius);
                    });
                });
            });

            $.select("a", () -> {
                display.block();
                text.decoration.none().whiteSpace.pre();
                padding.bottom(0.1, rem);
            });
        };

        Style controls = () -> {

            $.select("#theme", () -> {
                display.width(20, px).height(20, px);
                margin.right(1, em);
            });

            $.select("svg", () -> {
                display.width(20, px).height(20, px);
            });

            $.when(JavadngDSL.Small, () -> {
                display.none();
            });
        };

        Style theme = Style.named(".light .moon, .dark .sun", () -> {
            display.none();
        });

        Style title = () -> {
            font.size(2.5, rem).family(Theme.title).weight.normal().color(Theme.primary);

            $.when(BASE, () -> {
                padding.inlineStart(2, rem);
            });
        };

        Style links = () -> {
            Numeric iconMargin = Numeric.num(4, px);
            Numeric iconSize = Numeric.num(24, px);

            display.width(60, percent).grid().flowColumn().gap(iconMargin).column(x -> x.repeatAutoFit(iconSize, 1, fr));

            $.child(() -> {
                font.size(12, px).color(Theme.front);
                display.inlineFlex().alignItems.center().direction.column();
                text.decoration.none().whiteSpace.nowrap();
                transition.duration(0.2, s).whenever();

                $.after(() -> {
                    content.attr("id");

                    $.when(BASE, () -> {
                        font.size.smaller();
                    });
                });

                $.select("svg", () -> {
                    display.size(iconSize);
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
            padding.top(22, px);
            border.bottom.color(Theme.primary).width(1, px).solid();
            display.width(100, percent)
                    .zIndex(10)
                    .grid()
                    .align(Items.Center)
                    .justify(Items.Center)
                    .column(x -> x.autoMax(1, fr).autoMax(91, ch).autoMax(1, fr))
                    .area(title, links, controls);

            $.after(() -> {
                position.absolute().top(5, px).right(1, rem);
                content.text("Updated\\00A0").attr("date").text("　　Version\\00A0").attr("ver");
                font.size(0.8, rem).color(Theme.front).family(Theme.base).letterSpacing(-0.5, px);
                text.whiteSpace.pre();
            });
        };

        Style article = () -> {
            display.width(99.9, percent);
            font.letterSpacing(-0.025, rem);
            position.relative();

            $.after(() -> {
                content.text("");
                position.absolute().top(JavadngDSL.BlockVerticalGap).left(0, px);
                display.width(91, percent).height(91, percent).zIndex(5).opacity(0).block();
                background.color(Theme.surface);
                pointerEvents.none();

                $.transit().ease().duration(0.15, s).when().with(".fadeout", () -> {
                    display.opacity(1);
                });
            });
        };

        Style aside = () -> {
            $.when(BASE, MIDDLE, () -> {
                display.none();
            });
        };

        Style footer = () -> {
        };

        Style body = () -> {
            background.color(Theme.back).image(Theme.backImage).repeat();
            font.size(Theme.font).family(Theme.base).color(Theme.front.lighten(Theme.back, 5)).lineHeight(Theme.line);
            margin.horizontal(Numeric.max(20, px, 5, dvw));

            $.when(BASE, () -> {
                display.grid().area(header).area(article).area(nav);
                margin.size(0, px);
            });

            $.when(MIDDLE, () -> {
                display.grid()
                        .gap(GAP)
                        .align(Items.Start)
                        .justify(Items.Center)
                        .column(x -> x.minmax(30, ch, 1, fr).autoMax(91, ch))
                        .row($.num(80, px), $.num(1, fr))
                        .area(header, header)
                        .area(nav, article);
            });

            $.when(LARGE, () -> {
                display.grid()
                        .gap(GAP)
                        .align(Items.Start)
                        .justify(Items.Center)
                        .column(x -> x.minmax(30, ch, 1, fr).autoMax(91, ch).autoMax(1, fr))
                        .row($.num(80, px), $.num(1, fr))
                        .area(header, header, header)
                        .area(nav, article, aside);
            });
        };
    }
}