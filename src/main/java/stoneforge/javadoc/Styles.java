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

import stylist.AbstractStyleDSL;
import stylist.Browsers;
import stylist.Style;
import stylist.property.Background.BackgroundImage;
import stylist.value.Color;
import stylist.value.Font;
import stylist.value.Numeric;

public class Styles extends AbstractStyleDSL implements StyleConstants {

    // =====================================================
    // HTML Elements
    // =====================================================
    public static Style HTML = Style.named("html", () -> {
        font.size(14, px);
        scroll.smooth().padding.top(HeaderHeight);
        overflow.y.scroll();

        $.select("*[class|=el]", () -> {
            font.size(1, rem);
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
            font.color(theme.accent);
            text.decoration.underline();
            text.decorationColor.color(theme.front.opacify(-0.5));
        });
    });

    public static Style HTMLCheckbox = Browsers.checkbox(theme);

    public static Style HTMLToolTip = Browsers
            .tooltip("title", true, theme.back, theme.front.opacify(-0.08), BackgroundImage.drawSlash(theme.front.opacify(-0.15), 2));

    public static Style SVG = Style.named(".svg", () -> {
        display.width(16, px);
        stroke.current().linejoin.round().linecap.round().width(1.5, px);
        fill.none();
    });

    public static Style AnimatedSVG = SVG.with(() -> {
        stroke.width(2.5, px).color(theme.front.opacify(-0.6));

        $.transit().duration(0.5, s).when().hover(() -> {
            stroke.color(theme.accent);
        });

        $.transit().duration(0.05, s).ease().when().active(() -> {
            transform.translateY(3, px);
        });
    });

    public static Style HLJS = Style.named(".hljs", () -> {
        block();
        font.family(theme.monoFont).size(11.5, px).letterSpacing(-0.3, px);
        border.radius(4, px);
        margin.vertical(1, em);
        padding.left(1.2, em).vertical(1, em);
        position.relative();

        $.before(() -> {
            content.attr("lang");
            position.absolute().right(25, px).top(0.2, em);
        });

        $.select("a", () -> {
            position.absolute().right(5, px).top(4, px);

            $.select("svg", AnimatedSVG.with(() -> {
                stroke.current().width(2, px);
            }));
        });
    });

    public static Style JavadocComment = () -> {
        $.select("h2", () -> {
            position.relative();
            font.size(17, px).color(theme.primary).letterSpacing(1.5, px);
            padding.size(0.5, em).left(0, em);
            margin.top(0.3, em).bottom(0.8, em);
            border.vertical.dotted().width(1, px).color("gray");

            $.before(() -> {
                font.family(Font.Awesome).size(2, em).color("white");
                content.text("\\f111");
                position.absolute().left(-1, em).top(-0.13, em);
            });
            $.after(() -> {
                font.family(Font.Awesome).size(1, em).color(theme.primary);
                content.text("\\f111");
                position.absolute().left(-1.6, em).top(0.55, em);
            });
        });

        $.select("h3", () -> {
            font.size(15, px).letterSpacing(1.5, px).color(theme.primary);
            padding.vertical(0.3, em);
            margin.top(1.1, em);
        });

        $.select("p", () -> {
            block();
        });

        $.select("dl", () -> {
            block();
            display.flex().wrap.enable();

            Color borderColor = Color.hsl(0, 0, 85);
            Numeric pad = Numeric.of(2.3, em);

            $.select(">dt", () -> {
                flexItem.basis(20, percent);
                position.relative();
                margin.vertical(7, px);
                padding.vertical(0.6, em).right(pad);
                border.right.solid().width(1, px).color(borderColor);
                text.align.right();

                $.not($.lastType(), () -> {
                    $.before(() -> {
                        content.text("");
                        display.block().width(3, px).height(3, px);
                        position.absolute().right(-2, px).bottom(-8, px);
                        border.radius(50, percent).solid().width(1, px).transparent();
                        background.color(theme.secondary);
                    });
                });
            });

            $.select(">dd", () -> {
                flexItem.basis(70, percent);
                margin.vertical(7, px);
                padding.vertical(0.6, em).left(pad);
            });
        });

        $.select("ul", () -> {
            block();
            margin.left(0.5, rem);
            listStyle.inside();
        });

        $.select("ol", () -> {
            block();
            margin.left(0.5, rem);
            listStyle.inside();
        });

        $.select("blockquote", () -> {
            block();
        });
    };

    public static Style Section = () -> {
        margin.bottom(1.6, rem).top(0.6, rem);
        padding.horizontal(1.7, rem).vertical(1, rem);
        border.radius(4, px);
        background.color("white");
    };

    private static void block() {
        margin.bottom(0.8, rem);
    }

    /**
     * Define block-like.
     * 
     * @param color
     */
    public static void block(Color color, boolean paintBackground) {
        margin.left(0, px);
        padding.vertical(StyleConstants.BlockVerticalGap).horizontal(StyleConstants.BlockHorizontalGap);
        border.left.width(StyleConstants.BlockBorderWidth).solid().color(color);
        font.family(theme.baseFont).lineHeight(LineHeight);
        if (paintBackground) background.color(color.opacify(-0.8d));
    }

    /**
     * Define block-like.
     * 
     * @param color
     */
    public static void block2(Color color, boolean paintBackground) {
        padding.vertical(StyleConstants.BlockVerticalGap).horizontal(StyleConstants.BlockHorizontalGap);
        border.left.width(StyleConstants.BlockBorderWidth).solid().color(color);
        border.radius(2, px);
        font.family(theme.baseFont).lineHeight(LineHeight);
        position.relative();
        if (paintBackground) background.color(color.opacify(-0.8d));

        $.before(() -> {
            position.absolute();
            content.text("Signature");
            font.color(color.opacify(-0.9d)).size(1.6, rem);
        });
    }

    public static final Style SignatureParameterPart = () -> {
        font.color(theme.front.lighten(18));
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

    public static final Style HTMLClassTypeInterface = Style.named(".Interface", () -> {
        buildMark("I", Color.rgb(128, 88, 165), false, true);
    });

    public static final Style HTMLClassTypeFunctionalInterface = Style.named(".Functional", () -> {
        buildMark("F", Color.rgb(128, 88, 165), false, true);
    });

    public static final Style HTMLClassTypeAbstractClass = Style.named(".AbstractClass", () -> {
        buildMark("C", Color.rgb(50, 135, 92), false, true);
    });

    public static final Style HTMLClassTypeClass = Style.named(".Class", () -> {
        buildMark("C", Color.rgb(50, 135, 92), true, true);
    });

    public static final Style HTMLClassTypeEnum = Style.named(".Enum", () -> {
        buildMark("E", Color.hsl(0, 29, 49), true, false);
    });

    public static final Style HTMLClassTypeAnnotation = Style.named(".Annotation", () -> {
        buildMark("A", theme.primary, true, false);
    });

    public static final Style HTMLClassTypeException = Style.named(".Exception", () -> {
        buildMark("T", theme.accent, true, false);
    });

    private static void buildMark(String mark, Color color, boolean fill, boolean circle) {
        display.flex();

        $.before(() -> {
            display.inlineTable().width(15, px).height(15, px);
            flexItem.alignSelf.center();
            text.align.center();
            border.color(color).solid().width(1, px);
            margin.right(6, px);
            content.text(mark);
            font.size(10, px).lineHeight(13, px);

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
                background.color(Color.White.opacify(80));
            }
        });
    }

    public static final Style HTMLClassModifierPublic = Style.named(".PUBLIC", () -> {
        setMarkColor(Color.rgb(50, 135, 92));
    });

    public static final Style HTMLClassModifierProtected = Style.named(".PROTECTED", () -> {
        setMarkColor(theme.secondary);
    });

    public static final Style HTMLClassModifierPackagePrivate = Style.named(".PACKAGEPRIVATE", () -> {
        setMarkColor(theme.primary);
    });

    public static final Style HTMLClassModifierPrivate = Style.named(".PRIVATE", () -> {
        setMarkColor(theme.accent);
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
            font.color(theme.primary).size(0.6, rem).family(theme.monoFont);
            content.text(mark);
            position.absolute().top(-0.2, rem).left(0.7, rem);
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
            font.color(theme.accent).size(0.6, rem).family(theme.monoFont);
            content.text(mark);
            position.absolute().top(-0.2, rem).left(0.3, rem);
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
            font.color(theme.primary).size(0.7, rem).family(theme.iconFont);
            content.text(mark);
            position.absolute().top(0.5, rem).left(0.4, rem);
        });
    }

    public static final Style SelectBox = Style.named(".vs__dropdown-toggle", () -> {
        background.color(Color.White);
    });

    public static final Style SelectBoxHighlight = Style.named(".vs__dropdown-option--highlight", () -> {
        font.color("inherit");
        background.color(theme.accent.opacify(-0.6));
    });

    public static final Style InputBox = Style.named("#SearchByName", () -> {
        display.block().width(100, percent);
        background.color(Color.White);
        margin.vertical(8, px);
        padding.vertical(6, px).horizontal(8, px);
        border.color(Color.rgb(60, 60, 60, 0.26)).width(1, px).solid().radius(4, px);
    });

    public static final Style Tree = Style.named(".tree", () -> {
        overflow.hidden().scrollbar.thin();
        display.height(60, vh);
        background.color(Color.Inherit);

        $.hover(() -> {
            overflow.y.auto();
        });

        $.select("dt", () -> {
            font.weight.bold();
            cursor.pointer();
            padding.vertical(0.2, rem).horizontal(0.5, rem);
            border.radius(2, px);

            $.hover(() -> {
                font.color(theme.accent);
            });
        });

        $.select("dd", () -> {
            padding.vertical(0.1, rem).left(1, rem);
            cursor.pointer();
            border.radius(2, px);
            text.whiteSpace.nowrap();

            $.hover(() -> {
                font.color(theme.accent);
            });

            $.child(() -> {
                display.inlineBlock().width(100, percent);

                $.child(() -> {
                    display.inlineBlock().width(100, percent);
                    text.decoration.none();

                    $.hover(() -> {
                        text.decoration.none();
                    });
                });
            });
        });
    });
}