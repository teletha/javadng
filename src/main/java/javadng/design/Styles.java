/*
 * Copyright (C) 2024 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.design;

import stylist.Style;
import stylist.util.Browsers;
import stylist.value.Color;
import stylist.value.Numeric;

public class Styles implements JavadngStyleDSL {

    // =====================================================
    // HTML Elements
    // =====================================================
    public static Style HTML = Style.named("html", () -> {
        font.size(14, px);
        scroll.smooth().padding.top(JavadngStyleDSL.HeaderHeight);
        overflow.y.scroll();

        $.select(".disabled", () -> {
            pointerEvents.none();
            text.unselectable();
            display.opacity(0.6);
        });
    });

    public static Style HTMLCode = Style.named("code", () -> {
        font.family("inherit");
    });

    public static Style HTMLAnchor = Style.named("a", () -> {
        font.color(Color.Inherit);
        text.decorationStyle.dotted().decorationColor.color("#ccc");
        cursor.pointer();

        $.hover(() -> {
            font.color(JavadngStyleDSL.Theme.link);
            text.decoration.underline();
            text.decorationColor.color(JavadngStyleDSL.Theme.front.opacify(-0.5));
        });
    });

    public static Style HTMLCheckbox = Browsers.checkbox(JavadngStyleDSL.Theme.link);

    public static Style HTMLSelectbox = Browsers.selectbox(JavadngStyleDSL.Theme.back, JavadngStyleDSL.Theme.front);

    public static Style HTMLToolTip = Browsers.tooltip("title", true, Color.rgb(227, 227, 227), Color.rgb(63, 63, 63));

    public static Style SVG = Style.named(".svg", () -> {
        display.width(16, px);
        stroke.current().linejoin.round().linecap.round().width(1.5, px);
        fill.none();
    });

    public static Style AnimatedSVG = SVG.with(() -> {
        stroke.width(2.5, px).color(JavadngStyleDSL.Theme.front.opacify(-0.85));

        $.transit().duration(0.5, s).when().hover(() -> {
            stroke.color(JavadngStyleDSL.Theme.accent);
        });

        $.transit().duration(0.05, s).ease().when().active(() -> {
            transform.translateY(3, px);
        });
    });

    public static Style PRE = Style.named("pre", () -> {
        text.whiteSpace.preWrap().wordBreak.breakAll();
    });

    public static Style HLJS = Style.named(".hljs", () -> {
        block();
        font.family(JavadngStyleDSL.Theme.mono).size(0.9, em).lineHeight(1.6);
        border.radius(JavadngStyleDSL.Theme.radius)
                .width(1, px)
                .solid()
                .color(JavadngStyleDSL.Theme.surface.lighten(JavadngStyleDSL.Theme.front, 6));
        padding.left(1.6, em).vertical(1.2, em);
        position.relative();
        background.color(JavadngStyleDSL.Theme.surface.lighten(JavadngStyleDSL.Theme.front, 3));

        $.before(() -> {
            content.attr("lang");
            position.absolute().right(28, px).top(4, px);
        });

        $.select("a", () -> {
            position.absolute().right(8, px).top(6, px);

            $.select("svg", AnimatedSVG.with(() -> {
                display.width(14, px);
                stroke.current().width(2, px);
            }));
        });

        $.select(".hljs-emphasis", () -> {
            font.style.italic();
        });

        $.select(".hljs-strong", () -> {
            font.weight(700);
        });
    });

    public static Style HLJS_LIGHT = Style.named(".light", () -> {
        $.select(".hljs-subst", () -> {
            font.color("#2f3337");
        });

        $.select(".hljs-comment", () -> {
            font.color("#656e77");
        });

        $.select(":is(.hljs-attr,.hljs-doctag,.hljs-keyword,.hljs-selector-tag)", () -> {
            font.color("#015692");
        });

        $.select(".hljs-attribute", () -> {
            font.color("#803378");
        });

        $.select(":is(.hljs-name,.hljs-number,.hljs-quote,.hljs-selector-id,.hljs-template-tag)", () -> {
            font.color("#b75501");
        });

        $.select(".hljs-selector-class", () -> {
            font.color("#015692");
        });

        $.select(":is(.hljs-link,.hljs-regexp,.hljs-selector-attr,.hljs-string,.hljs-symbol)", () -> {
            font.color("#54790d");
        });

        $.select(":is(.hljs-meta,.hljs-selector-pseudo)", () -> {
            font.color($.hsl(100, 42, 30));
        });

        $.select(":is(.hljs-built_in,.hljs-literal)", () -> {
            font.color("#b75501");
        });

        $.select(":is(.hljs-bullet,.hljs-code)", () -> {
            font.color("#535a60");
        });

        $.select(":is(.hljs-meta .hljs-string)", () -> {
            font.color("#54790d");
        });

        $.select(".hljs-deletion", () -> {
            font.color("#c02d2e");
        });

        $.select(".hljs-addition", () -> {
            font.color("#2f6f44");
        });
    });

    public static Style HLJS_DARK = Style.named(".dark", () -> {
        $.select(".hljs-subst", () -> {
            font.color("#fff");
        });

        $.select(".hljs-comment", () -> {
            font.color($.hsl(55, 42, 60));
        });

        $.select(":is(.hljs-attr,.hljs-doctag,.hljs-keyword,.hljs-selector-tag)", () -> {
            font.color("#88aece");
        });

        $.select(".hljs-attribute", () -> {
            font.color("#c59bc1");
        });

        $.select(":is(.hljs-name,.hljs-number,.hljs-quote,.hljs-selector-id,.hljs-template-tag)", () -> {
            font.color("#f08d49");
        });

        $.select(".hljs-selector-class", () -> {
            font.color("#88aece");
        });

        $.select(":is(.hljs-link,.hljs-regexp,.hljs-selector-attr,.hljs-string,.hljs-symbol)", () -> {
            font.color("#b5bd68");
        });

        $.select(":is(.hljs-meta,.hljs-selector-pseudo)", () -> {
            font.color($.hsl(100, 42, 60));
        });

        $.select(":is(.hljs-built_in,.hljs-literal)", () -> {
            font.color("#f08d49");
        });

        $.select(":is(.hljs-bullet,.hljs-code)", () -> {
            font.color("#ccc");
        });

        $.select(":is(.hljs-meta .hljs-string)", () -> {
            font.color("#b5bd68");
        });

        $.select(".hljs-deletion", () -> {
            font.color("#de7176");
        });

        $.select(".hljs-addition", () -> {
            font.color("#76c490");
        });
    });

    public static Style JavadocComment = () -> {
        $.select("h2", () -> {
            font.size(1.75, rem).color(JavadngStyleDSL.Theme.front);
            padding.size(0.5, rem).left(0, rem);
            margin.bottom(-0.6, rem);

            $.firstLetter(() -> {
                font.color(JavadngStyleDSL.Theme.accent);
            });
        });

        $.select("h3", () -> {
            font.size(1.4, rem).color(JavadngStyleDSL.Theme.front);
            margin.bottom(-0.6, rem);

            $.firstLetter(() -> {
                font.color(JavadngStyleDSL.Theme.secondary);
            });
        });

        $.select("p", () -> {
            block();
        });

        $.select("dl", () -> {
            margin.top(2, rem);

            $.select(">dt", () -> {
                font.size(1.2, rem).color(JavadngStyleDSL.Theme.front);
                margin.top(2, rem).bottom(-1.4, rem);
            });

            $.select(">dd", () -> {
                block();
            });
        });

        $.select("ul", () -> {
            font.lineHeight(1.9);
            margin.top(1, rem);
            margin.left(2.3, rem);
            listStyle.outside();
        });

        $.select("ol", () -> {
            font.lineHeight(1.9);
            margin.top(1, rem);
            margin.left(2.3, rem);
            listStyle.outside();
        });

        $.select("blockquote", () -> {
            margin.top(1, rem);
        });

        $.select("table", () -> {
            display.width(100, percent);
            border.radius(Theme.radius).solid().width(1, px).color(Theme.front.opacify(-0.5)).spacing(0, px);
            margin.vertical(2, rem);
        });

        $.select("th", () -> {
            font.weight.bold();
            padding.size(0.5, rem);
            border.bottom.solid().width(1, px).color(Theme.front.opacify(-0.5));

            $.not($.firstChild(), () -> {
                border.left.solid().width(1, px).color(Theme.front.opacify(-0.5));
            });
        });

        $.select("td", () -> {
            padding.size(0.5, rem);
        });

        $.select("tr", () -> {
            $.select("td + td", () -> {
                border.left.solid().width(1, px).color(Theme.front.opacify(-0.5));
            });
            $.next("tr td", () -> {
                border.top.solid().width(1, px).color(Theme.front.opacify(-0.5));
            });
        });

        $.select(":where(p, table) code", () -> {
            font.color(Theme.secondary).family(Theme.mono);

            $.before(() -> {
                content.text("[");
                font.color(Theme.front.opacify(-0.8));
                padding.horizontal(0.3, rem);
            });
            $.after(() -> {
                content.text("]");
                font.color(Theme.front.opacify(-0.8));
                padding.horizontal(0.3, rem);
            });
        });
    };

    public static Style Section = () -> {
        margin.bottom(2.2, rem).top(JavadngStyleDSL.BlockVerticalGap);
        padding.horizontal(2, rem).vertical(1, rem);
        border.radius(JavadngStyleDSL.Theme.radius);
        background.color(JavadngStyleDSL.Theme.surface);
    };

    private static void block() {
        margin.top(1.8, rem);

        $.firstChild(() -> {
            margin.top(1, rem);
        });
    }

    /**
     * Define block-like.
     * 
     * @param color
     */
    public static void block(Color color, boolean paintBackground) {
        margin.left(0, px);
        padding.vertical(JavadngStyleDSL.BlockVerticalGap).horizontal(JavadngStyleDSL.BlockHorizontalGap);
        border.left.width(JavadngStyleDSL.BlockBorderWidth).solid().color(color);
        font.family(JavadngStyleDSL.Theme.base).lineHeight(JavadngStyleDSL.Theme.line);
        if (paintBackground) background.color(color.opacify(-0.8d));
    }

    /**
     * Define block-like.
     * 
     * @param color
     */
    public static void block2(Color color, boolean paintBackground) {
        padding.vertical(JavadngStyleDSL.BlockVerticalGap).horizontal(JavadngStyleDSL.BlockHorizontalGap);
        border.left.width(JavadngStyleDSL.BlockBorderWidth).solid().color(color);
        border.radius(JavadngStyleDSL.Theme.radius);
        font.family(JavadngStyleDSL.Theme.base).lineHeight(JavadngStyleDSL.Theme.line);
        position.relative();
        if (paintBackground) background.color(color.opacify(-0.8d));

        $.before(() -> {
            position.absolute();
            content.text("Signature");
            font.color(color.opacify(-0.9d)).size(1.6, rem);
        });
    }

    public static final Style SignatureParameterPart = () -> {
        font.color(JavadngStyleDSL.Theme.front.lighten(18));
    };

    public static final Style HTMLClassParameters = Style.named(".parameters", () -> {
        $.before(() -> {
            content.text("<");
        });
        $.after(() -> {
            content.text(">");
        });
    });

    public static final Style HTMLClassExtends = Style.named(".extends", () -> {
        $.before(() -> {
            content.text(" extends ");
        });
    });

    public static final Style HTMLClassSuper = Style.named(".super", () -> {
        $.before(() -> {
            content.text(" super ");
        });
    });

    public static final Style HTMLClassArray = Style.named("code[array=fix]", () -> {
        $.after(() -> {
            content.text("[]");
        });
    });

    public static final Style HTMLClassVarParam = Style.named("code[array=var]", () -> {
        $.after(() -> {
            content.text("...");
        });
    });

    public static final Style HTMLClassPackage = Style.named(".package", () -> {
        font.weight.bold();
    });

    private static final Color InterfaceColor = Color.hsl(110, 60, 40);

    private static final Color ClassColor = Color.hsl(150, 50, 40);

    public static final Style HTMLClassTypeInterface = Style.named(".Interface", () -> {
        buildMark("I", InterfaceColor, false, true);
    });

    public static final Style HTMLClassTypeFunctionalInterface = Style.named(".Functional", () -> {
        buildMark("F", InterfaceColor, false, true);
    });

    public static final Style HTMLClassTypeAbstractClass = Style.named(".AbstractClass", () -> {
        buildMark("C", ClassColor, false, true);
    });

    public static final Style HTMLClassTypeClass = Style.named(".Class", () -> {
        buildMark("C", ClassColor, true, true);
    });

    public static final Style HTMLClassTypeEnum = Style.named(".Enum", () -> {
        buildMark("E", Color.hsl(0, 29, 49), true, false);
    });

    public static final Style HTMLClassTypeAnnotation = Style.named(".Annotation", () -> {
        buildMark("A", JavadngStyleDSL.Theme.primary, true, false);
    });

    public static final Style HTMLClassTypeException = Style.named(".Exception", () -> {
        buildMark("T", JavadngStyleDSL.Theme.accent, true, false);
    });

    private static void buildMark(String mark, Color color, boolean fill, boolean circle) {
        display.flex();

        $.before(() -> {
            display.inlineBlock().width(15, px).height(15, px);
            flexItem.alignSelf.center();
            text.align.center();
            border.color(color).solid().width(1, px);
            margin.right(6, px);
            content.text(mark);
            font.size(10, px).lineHeight(13, px);
            transform.translateY(-1, px);

            if (circle) {
                border.radius(50, percent);
            } else {
                border.radius(3, px);
            }

            if (fill) {
                font.color(Color.White);
                background.color(color);
            } else {
                font.color(color);
                background.color(JavadngStyleDSL.Theme.back.lighten(JavadngStyleDSL.Theme.front, 20).opacify(-0.7));
            }
        });
    }

    public static final Style HTMLClassModifierPublic = Style.named(".PUBLIC", () -> {
        setMarkColor(ClassColor);
    });

    public static final Style HTMLClassModifierProtected = Style.named(".PROTECTED", () -> {
        setMarkColor(JavadngStyleDSL.Theme.secondary);
    });

    public static final Style HTMLClassModifierPackagePrivate = Style.named(".PACKAGEPRIVATE", () -> {
        setMarkColor(JavadngStyleDSL.Theme.primary);
    });

    public static final Style HTMLClassModifierPrivate = Style.named(".PRIVATE", () -> {
        setMarkColor(JavadngStyleDSL.Theme.accent);
    });

    /** The circle icon. */
    private static final String circleStroked = "\\e836";

    /** The circle icon. */
    private static final String circleFilled = "\\e061"; // cirle filled big e3fa small e061

    /** The circle icon. */
    private static final String circlePointed = "\\e837"; // cirle filled big e3fa small e061

    /**
     * Assign mark color.
     * 
     * @param color
     */
    private static void setMarkColor(Color color) {
        position.relative();

        $.before(() -> {
            content.text("");
            display.inlineBlock().width(0.5, rem).height(0.5, rem);
            border.radius(50, percent);
            background.color(color);
            margin.right(0.65, rem);

            $.with(".OVERRIDE", () -> {
                // content.text("O");
            });
            //
            $.with(".FIELD", () -> {
                content.text(circleStroked);
            });
        });
    }

    public static final Style HTMLClassModifierStatic = Style.named(".STATIC", () -> {
        overlayAlphabetLeftTop("S");
    });

    public static final Style HTMLClassModifierDefault = Style.named(".DEFAULT", () -> {
        overlayAlphabetRightTop("D");
    });

    public static final Style HTMLClassModifierOverride = Style.named(".OVERRIDE", () -> {
    });

    public static final Style HTMLClassModifierSynchronized = Style.named(".SYNCHRONIZED", () -> {
        overlayIconRightBottom("\\e8ae");
    });

    public static final Style HTMLClassModifierTransient = Style.named(".TRANSIENT", () -> {
        overlayIconRightBottom("\\e14c");
    });

    public static final Style HTMLClassModifierAbstract = Style.named(".ABSTRACT", () -> {
        overlayAlphabetRightTop("A");
    });

    public static final Style HTMLClassModifierFinal = Style.named(".FINAL", () -> {
        overlayAlphabetRightTop("F");
    });

    public static final Style HTMLClassModifierVolatile = Style.named(".VOLATILE", () -> {
        overlayAlphabetRightTop("V");
    });

    /**
     * Create alphabetical mark.
     * 
     * @param mark
     */
    private static void overlayAlphabetRightTop(String mark) {
        position.relative();
        $.before(() -> {
            font.color(JavadngStyleDSL.Theme.primary).size(0.6, rem).family(JavadngStyleDSL.Theme.mono);
            content.text(mark);
            position.absolute().top(-0.2, rem).left(0.55, rem);
        });
    }

    /**
     * Create alphabetical mark.
     * 
     * @param mark
     */
    private static void overlayAlphabetLeftTop(String mark) {
        position.relative();
        $.after(() -> {
            font.color(JavadngStyleDSL.Theme.accent).size(0.6, rem).family(JavadngStyleDSL.Theme.mono);
            content.text(mark);
            position.absolute().top(-0.2, rem).left(0.15, rem);
        });
    }

    /**
     * Create icon mark.
     * 
     * @param mark
     */
    private static void overlayIconRightBottom(String mark) {
        position.relative();
        $.after(() -> {
            font.color(JavadngStyleDSL.Theme.primary).size(0.7, rem).family(JavadngStyleDSL.Theme.icon);
            content.text(mark);
            position.absolute().top(0.5, rem).left(0.2, rem);
        });
    }

    public static final Style Select = Style.named("o-select", () -> {
        Numeric gap = Numeric.of(18, px);
        Numeric iconSize = Numeric.of(18, px);

        display.block().height(32, px);
        font.lineHeight(30, px).color(JavadngStyleDSL.Theme.front);
        background.color(JavadngStyleDSL.Theme.surface);
        border.radius(JavadngStyleDSL.Theme.radius).solid().width(1, px).color(JavadngStyleDSL.Theme.front.opacify(-0.6));
        outline.none();
        text.whiteSpace.nowrap().unselectable();
        position.relative();
        cursor.pointer();
        transition.duration(0.2, s).easeInOut().whenever();

        $.select("view", () -> {
            display.block();
        });

        $.select("now", () -> {
            display.block().width(Numeric.of(100, percent).subtract(iconSize.multiply(2.5)));
            padding.horizontal(gap);
            font.color(JavadngStyleDSL.Theme.front.lighten(JavadngStyleDSL.Theme.surface, 25));
            overflow.hidden();
            text.whiteSpace.nowrap().overflow.ellipsis();

            $.with(".select", () -> {
                font.color(JavadngStyleDSL.Theme.front);
            });
        });

        $.select(".svg", () -> {
            display.inlineBlock().width(iconSize);
            stroke.width(2, px);
            transform.rotate(90, deg);
            transition.duration(0.2, s).easeInOut().whenever();
            position.absolute().top(Numeric.of(50, percent).subtract(iconSize.divide(2)));

            $.with(".chevron", () -> {
                position.right(iconSize.divide(2));

                $.with(".active", () -> {
                    transform.rotate(-90, deg);
                });
            });

            $.with(".x", () -> {
                position.right(iconSize.divide(2).plus(iconSize));
                display.opacity(0);
                transform.scale(0).origin.center();

                $.hover(() -> {
                    stroke.color(JavadngStyleDSL.Theme.link);
                });

                $.with(".active", () -> {
                    display.opacity(1);
                    transform.scale(1);
                });
            });
        });

        $.select("ol", () -> {
            display.block().opacity(0).zIndex(10).width(Numeric.of(100, percent).plus(1.5, px));
            background.color(JavadngStyleDSL.Theme.surface);
            border.radius(JavadngStyleDSL.Theme.radius).width(1, px).solid().color(JavadngStyleDSL.Theme.front.opacify(-0.6));
            margin.top(6, px);
            overflow.hidden();
            pointerEvents.none();
            position.absolute().top(100, percent).left(-1, px);
            transform.origin.position(50, percent, 0, percent).scale(0.75).translateY(-21, px);
            transition.duration(0.2, s).easeInOutCubic().whenever();

            $.with(".active", () -> {
                display.opacity(1);
                transform.scale(1).translateY(0, px);
                pointerEvents.auto();
            });
        });

        $.select("li", () -> {
            display.block();
            cursor.pointer();
            listStyle.none();
            padding.horizontal(gap);

            $.hover(() -> {
                background.color(JavadngStyleDSL.Theme.surface.lighten(JavadngStyleDSL.Theme.back, 10));
            });

            $.with(".select", () -> {
                font.color(JavadngStyleDSL.Theme.accent);
            });
        });
    });

    public static final Style InputBox = Style.named("#NameFilter", () -> {
        display.block().width(100, percent);
        background.color(Color.White);
        margin.vertical(8, px);
        padding.vertical(3, px).horizontal(8, px);
        border.color(Color.rgb(60, 60, 60, 0.26)).width(1, px).solid().radius(JavadngStyleDSL.Theme.radius);
    });

    public static final Style Tree = Style.named(".tree", () -> {
        overflow.hidden().scrollbar.thin();
        display.height(60, vh);
        background.color(Color.Inherit);

        $.hover(() -> {
            overflow.y.auto();
        });

        $.select("dl", () -> {
            padding.bottom(0.5, em);

            $.with(".show dd", () -> {
                display.block();
            });
            $.with(".expand dd", () -> {
                display.block();
            });
        });

        $.select("dt", () -> {
            font.weight.bold();
            text.whiteSpace.nowrap().unselectable();
            border.radius(JavadngStyleDSL.Theme.radius);
            padding.horizontal(0.5, em).vertical(0.15, em);
            cursor.pointer();

            $.hover(() -> {
                font.color(JavadngStyleDSL.Theme.accent);
                background.color(JavadngStyleDSL.Theme.surface);
            });
        });

        $.select("dd", () -> {
            display.none();
            text.whiteSpace.nowrap().unselectable();
            border.radius(JavadngStyleDSL.Theme.radius);
            padding.horizontal(0.5, em);
            cursor.pointer();

            $.hover(() -> {
                font.color(JavadngStyleDSL.Theme.accent);
                background.color(JavadngStyleDSL.Theme.surface);
            });

            $.child(() -> {
                display.inlineBlock().width(100, percent);

                $.child(() -> {
                    display.inlineBlock().width(100, percent);
                    padding.vertical(0.15, em);
                    text.decoration.none();

                    $.hover(() -> {
                        text.decoration.none();
                    });
                });
            });
        });
    });
}