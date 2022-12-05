/*
 * Copyright (C) 2020 stoneforge Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javadng.HTML;
import javadng.design.JavadngStyleDSL;
import javadng.parser.ClassInfo;
import kiss.I;
import stylist.Style;
import stylist.property.Background.BackgroundImage;
import stylist.value.Color;
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
    protected void declare() {
        $("html", () -> {
            $("head", () -> {
                $("meta", attr("charset", "UTF-8"));
                $("title", text(model.product() + " API"));
                $("base", attr("href", base));
                module("mimic.js");
                stylesheet("main.css");
            });
            $("body", S.Body, () -> {
                // =============================
                // Top Navigation
                // =============================
                String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());

                $("header", S.HeaderArea, () -> {
                    $("h1", S.HeaderTitle, attr("date", date), attr("ver", model.version()), code(model.product()));
                    $("nav", S.HeaderNav, () -> {
                        for (ClassInfo info : I.signal(model.docs).map(ClassInfo::outermost).toSet()) {
                            $("a", attr("href", "doc/" + info.children().get(0).id() + ".html"), svg("text"), text(info.title()));
                        }
                        $("a", attr("href", "api/"), svg("package"), code("API"));
                        $("a", attr("href", model.repository()
                                .locateCommunity()), attr("target", "_blank"), svg("user"), text("Community"));
                        $("a", attr("href", "doc/changelog.html"), svg("activity"), text("Activity"));
                        $("a", attr("href", model.repository().locate()), attr("target", "_blank"), svg("github"), text("Repository"));
                    });
                    $("div", attr("id", "ViewMode"), S.ViewMode, () -> {
                        $("a", attr("id", "light"), attr("title", "Change to a brighter color scheme"), () -> {
                            $(svg("sun"));
                        });

                        $("a", attr("id", "dark"), attr("title", "Change to a darker color scheme"), () -> {
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
                    $("article", attr("id", "Article"), S.Contents, () -> {
                        if (contents != null) {
                            declareContents();
                        }
                    });

                    // =============================
                    // Right Side Navigation
                    // =============================
                    $("aside", attr("id", "SubNavi"), S.SubNavigation, () -> {
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

        Style Body = () -> {
            background.color(JavadngStyleDSL.Theme.back).image(JavadngStyleDSL.Theme.backImage).repeat();
            font.size(JavadngStyleDSL.Theme.font)
                    .family(JavadngStyleDSL.Theme.base)
                    .color(JavadngStyleDSL.Theme.front.lighten(JavadngStyleDSL.Theme.back, 15))
                    .lineHeight(JavadngStyleDSL.Theme.line);
            margin.horizontal(35, px).bottom(14, px);
        };

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
            border.bottom.color(JavadngStyleDSL.Theme.primary).width(1, px).solid();
        };

        Style HeaderTitle = () -> {
            font.size(2.5, rem).family(JavadngStyleDSL.Theme.title).weight.normal().color(JavadngStyleDSL.Theme.primary);
            margin.right(3.5, rem);
            display.flex().wrap.disable();

            $.after(() -> {
                content.attr("date").text("\\000AVersion\\00A0").attr("ver");
                font.size(0.8, rem)
                        .lineHeight(1.1)
                        .color(JavadngStyleDSL.Theme.front)
                        .family(JavadngStyleDSL.Theme.base)
                        .letterSpacing(-0.5, px);
                display.inlineBlock();
                padding.left(1.1, rem).bottom(1.3, rem);
                text.whiteSpace.pre();
                flexItem.alignSelf.end();
            });
        };

        Style HeaderNav = () -> {
            display.minWidth(HeaderMinWidth);

            $.child(() -> {
                font.size(11, px).color(JavadngStyleDSL.Theme.front);
                display.width(90, px).inlineFlex().alignItems.center().direction.column();
                padding.horizontal(1.8, rem).vertical(0.5, rem);
                margin.top(-4, px);
                text.decoration.none().whiteSpace.nowrap();
                transition.duration(0.2, s).whenever();

                $.select("svg", () -> {
                    Numeric size = Numeric.of(26, px);

                    display.width(size).height(size);
                    stroke.color(JavadngStyleDSL.Theme.front.lighten(JavadngStyleDSL.Theme.back, -15)).width(1.2, px);
                    transition.duration(0.2, s).whenever();
                });

                $.hover(() -> {
                    text.decoration.none();
                    font.color(JavadngStyleDSL.Theme.link);

                    $.select("svg", () -> {
                        stroke.color(JavadngStyleDSL.Theme.link).width(2, px);
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

            $.media(JavadngStyleDSL.Small, () -> {
                display.none();
            });
        };

        Style Root = Style.named("html.light #light svg, html.dark #dark svg", () -> {
            fill.color(Color.hsl(55, 100, 75));
        });

        Style MainArea = () -> {
            display.maxWidth(JavadngStyleDSL.MaxWidth).flex().direction.row();
            margin.auto();

            $.media(JavadngStyleDSL.Small, () -> {
                display.flex().wrap.enable();
            });
        };

        Style Navigation = () -> {
            display.maxWidth(JavadngStyleDSL.MaxNaviWidth);
            flexItem.basis(NavigationWidth).shrink(0).alignSelf.start();
            position.sticky().top(JavadngStyleDSL.HeaderHeight.plus(15, px));
            padding.top(JavadngStyleDSL.BlockVerticalGap);
            margin.bottom(1.6, rem);

            $.media(JavadngStyleDSL.Small, () -> {
                flexItem.order(2);
            });

            $.select("#APINavi", () -> {
                $.select(">*", () -> {
                    margin.bottom(JavadngStyleDSL.BlockVerticalGap);
                });
            });

            $.select("#DocNavi", () -> {
                font.size(1.1, em).color(JavadngStyleDSL.Theme.front.lighten(JavadngStyleDSL.Theme.back, -15)).letterSpacing(-0.5, px);

                $.select(".doc", () -> {
                    margin.bottom(1, em);

                    $.select("li", () -> {
                        padding.vertical(0.3, em);
                    });
                });

                $.select(".sub", () -> {
                    display.height(0, px);
                    listStyle.none();
                    font.size(0.9, em).color(JavadngStyleDSL.Theme.front.lighten(JavadngStyleDSL.Theme.back, 10));
                    border.left.solid().width(1, px).color(Color.hsl(0, 0, 65));
                    overflow.y.hidden();

                    transition.duration(0.3, s).whenever();

                    $.select("a", () -> {
                        $.select(".foot", () -> {
                            padding.left(1, em);
                        });

                        $.select("svg", () -> {
                            display.width(16, px).height(16, px);
                            margin.left(-16, px);
                            stroke.width(2, px).transparent();

                            transition.duration(0.25, s).whenever();
                            transform.translateX(0, px);
                        });

                        $.with(".now", () -> {
                            font.color(JavadngStyleDSL.Theme.accent);
                            $.select("svg", () -> {
                                stroke.color(JavadngStyleDSL.Theme.accent);
                                transform.translateX(10, px);
                            });
                        });

                        $.hover(() -> {
                            $.select("svg", () -> {
                                stroke.color(JavadngStyleDSL.Theme.accent);
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
                    padding.vertical(0.2, rem);

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
            font.size(14.2, px).letterSpacing(-0.025, rem);
            position.relative();

            $.after(() -> {
                content.text("");
                position.absolute().top(JavadngStyleDSL.BlockVerticalGap).left(0, px);
                display.width(100, percent).height(100, percent).zIndex(5).opacity(0).block();
                background.color(JavadngStyleDSL.Theme.surface);
                pointerEvents.none();

                $.transit().ease().duration(0.15, s).when().with(".fadeout", () -> {
                    display.opacity(1);
                });
            });
        };

        Style SubNavigation = () -> {
            display.maxWidth(JavadngStyleDSL.MaxSubNaviWidth);

            $.media(JavadngStyleDSL.Small, () -> {
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
    }
}