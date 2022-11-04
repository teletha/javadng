/*
 * Copyright (C) 2020 stoneforge Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javadng.HTML;
import javadng.parser.analyze.ClassInfo;
import kiss.I;
import stylist.Style;
import stylist.StyleDSL;
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
     * @param info
     */
    protected Page(int depth, JavadocModel model, T info) {
        this.model = model;
        this.contents = info;
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
            $("body", styles.Body, () -> {
                // =============================
                // Top Navigation
                // =============================
                String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());

                $("header", styles.HeaderArea, () -> {
                    $("h1", styles.HeaderTitle, attr("date", date), attr("ver", model.version()), code(model.product()));
                    $("nav", styles.HeaderNav, () -> {
                        for (ClassInfo info : I.signal(model.docs).map(ClassInfo::outermost).toSet()) {
                            $("a", attr("href", "doc/" + info.children().get(0).id() + ".html"), svg("text"), text(info.title()));
                        }
                        $("a", attr("href", "api/"), svg("package"), code("API"));
                        $("a", attr("href", model.repository()
                                .locateCommunity()), attr("target", "_blank"), svg("user"), text("Community"));
                        $("a", attr("href", "doc/changelog.html"), svg("activity"), text("Activity"));
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
                module("main.js");
            });
        });

    }

    protected abstract void declareContents();

    protected abstract void declareSubNavigation();

    /**
     * Style definition.
     */
    private interface styles extends StyleDSL, StyleConstants {

        Numeric HeaderMinWidth = Numeric.of(300, px);

        Numeric NavigationWidth = Numeric.of(17, vw);

        Style Body = () -> {
            background.color(theme.back).image(theme.backImage).repeat();
            font.size(theme.font).family(theme.base).color(theme.front).lineHeight(theme.line);
            margin.horizontal(35, px).vertical(14, px);
        };

        Style HeaderArea = () -> {
            background.color(Color.Inherit).image(BackgroundImage.inherit()).repeat();
            position.sticky().top(0, rem);
            display.minWidth(HeaderMinWidth).maxWidth(MaxWidth).minHeight(HeaderHeight).zIndex(10).flex().alignItems.center().wrap.enable();
            margin.auto().bottom(HeaderBottomMargin);
            padding.top(22, px);
            border.bottom.color(theme.primary).width(1, px).solid();
        };

        Style HeaderTitle = () -> {
            font.size(2.5, rem).family(theme.title).weight.normal().color(theme.primary);
            margin.right(3.5, rem);
            display.flex().wrap.disable();

            $.after(() -> {
                content.attr("date").text("\\000AVersion\\00A0").attr("ver");
                font.size(0.8, rem).lineHeight(1.1).color(theme.front).family(theme.base).letterSpacing(-0.5, px);
                display.inlineBlock();
                padding.left(1.1, rem).bottom(1.3, rem);
                text.whiteSpace.pre();
                flexItem.alignSelf.end();
            });
        };

        Style HeaderNav = () -> {
            display.minWidth(HeaderMinWidth);

            $.child(() -> {
                font.size(11, px);
                display.width(90, px).inlineFlex().alignItems.center().direction.column();
                padding.horizontal(1.8, rem).vertical(0.5, rem);
                margin.top(-4, px);
                text.decoration.none().whiteSpace.nowrap();
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

            $.media(Small, () -> {
                display.none();
            });
        };

        Style Root = Style.named("html.light #light svg, html.dark #dark svg", () -> {
            fill.color(Color.hsl(55, 100, 75));
        });

        Style MainArea = () -> {
            display.maxWidth(MaxWidth).flex().direction.row();
            margin.auto();

            $.media(Small, () -> {
                display.flex().wrap.enable();
            });
        };

        Style Navigation = () -> {
            display.maxWidth(MaxNaviWidth);
            flexItem.basis(NavigationWidth).shrink(0).alignSelf.start();
            position.sticky().top(80, px);
            padding.top(StyleConstants.BlockVerticalGap);
            margin.bottom(1.6, rem);

            $.media(Small, () -> {
                flexItem.order(2);
            });

            $.select("#APINavi", () -> {
                $.select(">*", () -> {
                    margin.bottom(StyleConstants.BlockVerticalGap);
                });
            });

            $.select("#DocNavi", () -> {
                font.size(1.1, em).color(theme.front.lighten(theme.back, -15)).letterSpacing(-0.5, px);

                $.select(".doc", () -> {
                    margin.bottom(1, em);

                    $.select("li", () -> {
                        padding.vertical(0.3, em);
                    });
                });

                $.select(".sub", () -> {
                    display.height(0, px);
                    listStyle.none();
                    font.size(0.9, em).color(theme.front.lighten(theme.back, 10));
                    border.left.solid().width(1, px).color(Color.hsl(0, 0, 65));
                    overflow.y.hidden();

                    transition.duration(0.5, s).whenever();

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
                            font.color(theme.accent);
                            $.select("svg", () -> {
                                stroke.color(theme.accent);
                                transform.translateX(10, px);
                            });
                        });

                        $.hover(() -> {
                            $.select("svg", () -> {
                                stroke.color(theme.accent);
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

                    $.select("svg", () -> {
                        display.width(20, px).height(20, px);
                        text.verticalAlign.middle();
                        margin.top(-2, px).right(1, em);
                    });
                });
            });
        };

        Style Contents = () -> {
            Numeric gap = Numeric.of(1.8, rem);
            display.maxWidth(MaxWidth.subtract(MaxNaviWidth).subtract(MaxSubNaviWidth).subtract(gap.multiply(2)));
            flexItem.grow(1);
            margin.horizontal(gap);
            font.size(14.2, px).letterSpacing(0.04, em);
        };

        Style SubNavigation = () -> {
            display.maxWidth(MaxSubNaviWidth);

            $.media(Small, () -> {
                display.none();
            });
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
                padding.vertical(0.25, em);
            });
        };
    }
}