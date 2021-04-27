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

import stoneforge.HTML;
import stoneforge.javadoc.analyze.ClassInfo;
import stoneforge.javadoc.analyze.FieldInfo;
import stylist.Style;
import stylist.StyleDSL;
import stylist.property.Background.BackgroundImage;
import stylist.value.Color;
import stylist.value.LinearGradient;
import stylist.value.Numeric;

/**
 * 
 */
class DocumentContentsView extends HTML {

    private JavadocModel model;

    private final ClassInfo info;

    /**
     * @param info
     */
    public DocumentContentsView(JavadocModel model, ClassInfo info) {
        this.model = model;
        this.info = info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declare() {
        $("section", style.MemberSection, () -> {
            $(info.createComment());
        });

        for (FieldInfo field : info.fields()) {
            $("section", attr("id", field.id()), style.MemberSection, () -> {
                $(field.createComment());
            });
        }
    }

    /**
     * Style definition.
     */
    private interface style extends StyleDSL, BaseStyle {

        Color keyword = Color.hsl(0, 29, 49);

        Numeric signatureLabelWidth = Numeric.of(2.5, rem);

        Style MemberSection = () -> {
            margin.bottom(1.6, rem).top(0.6, rem);
            padding.horizontal(1.3, rem).vertical(0.8, rem);
            border.radius(4, px);
            background.color(Color.White);

            $.target(() -> {
                background.image(BackgroundImage.of(new LinearGradient().angle(90, deg)
                        .color(Color.hsl(200, 55, 65), Numeric.of(0, px), Numeric.of(4, px))
                        .color(Color.White, Numeric.of(4, px), Numeric.of(100, percent))));
            });
        };

        Style PackcageName = () -> {
            font.size(9, px).color("#999");
            display.block();
            margin.bottom(-5, px).left(22, px);
        };

        Style TypeName = () -> {
            font.family(theme.baseFont).size(1.2, rem).weight.normal();
            margin.bottom(0.3, rem);
        };

        Style MemberName = () -> {
            font.family(theme.baseFont).size(1, rem).weight.normal();
            display.block();
        };

        Style Name = () -> {
            font.weight.bold().style.normal();
            margin.right(0.175, rem);
        };

        Style Return = () -> {
            font.color(theme.secondary.lighten(-30));

            $.before(() -> {
                content.text(":");
                padding.horizontal(0.3, em);
            });
        };

        Style SignatureTable = () -> {
            padding.left(signatureLabelWidth);
            margin.top(0.1, rem).bottom(0.6, rem);

            $.select("td", () -> {
                padding.right(0.8, rem);
                text.verticalAlign.top().overflow.ellipsis();
                font.lineHeight(1.3);

                $.not($.lastChild(), () -> {
                    text.whiteSpace.nowrap();
                });

                $.empty().after(() -> {
                    content.text("No description.");
                });
            });
        };

        Style SignatureDefinition = () -> {
            position.relative();

            $.before(() -> {
                position.absolute();
                display.inlineBlock().width(signatureLabelWidth);
                margin.left(signatureLabelWidth.negate());
                font.size(0.8, rem).color(keyword);
            });
        };

        Style SignatureTypeVariable = () -> {
            SignatureDefinition.style();

            $.before(() -> {
                content.text("Type");
            });
        };

        Style SignatureParameter = () -> {
            SignatureDefinition.style();

            $.before(() -> {
                content.text("Param");
            });
        };

        Style SignatureReturn = () -> {
            SignatureDefinition.style();

            $.before(() -> {
                content.text("Return");
                font.color(theme.secondary.lighten(-30));
            });
        };

        Style SignatureException = () -> {
            SignatureDefinition.style();

            $.before(() -> {
                content.text("Throw");
            });
        };

        Style traits = () -> {
            Numeric pad = Numeric.of(5, rem);

            listStyle.none();
            display.flex().wrap.enable();
            padding.left(pad);

            $.before(() -> {
                font.color(keyword);
                display.block().width(pad);
                margin.left(pad.negate());
            });

            $.select("li", () -> {
                display.inlineBlock();
            });
        };

        Style typeParameter = traits.with(() -> {
            $.before(() -> {
                content.text("Type");
            });

            $.select("li", () -> {
                display.width(100, percent);

                $.select("> i", () -> {
                    $.nthChild("1", () -> {
                        font.weight.bold();
                    });

                    $.nthChild("2", () -> {
                        Styles.SignatureParameterPart.style();
                        margin.right(1.5, rem);
                    });
                });
            });
        });

        Style extend = traits.with(() -> {
            $.before(() -> {
                content.text("Extends");
            });

            $.select("li").not($.lastChild()).after(() -> {
                font.color(theme.front.lighten(30)).size.smaller();
                content.text("\\025b6");
                margin.horizontal(0.8, rem);
            });
        });

        Style implement = traits.with(() -> {
            $.before(() -> {
                content.text("Implements");
            });

            $.select("li", () -> {
                margin.right(1.4, rem);
            });
        });

        Style sub = traits.with(() -> {
            $.before(() -> {
                content.text("Subtypes");
            });

            $.select("li", () -> {
                margin.right(1.4, rem);
            });
        });

        Style SampleDesc = () -> {
            display.block();
            border.top.radius(4, px);
            border.left.radius(4, px);
            margin.top(1, em);
            padding.horizontal(0.5, em).top(0.5, em);
            background.color("#f0f0f0");
            border.bottom.doubles().color("white").width(3, px);
        };

        Style Sample = () -> {
            margin.bottom(0.3, em);

            $.select("> code", () -> {
                border.bottom.radius(4, px);
                border.right.radius(4, px);
            });
        };
    }
}