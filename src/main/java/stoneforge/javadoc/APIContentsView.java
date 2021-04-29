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

import java.util.List;
import java.util.stream.IntStream;

import kiss.XML;
import stoneforge.HTML;
import stoneforge.javadoc.analyze.ClassInfo;
import stoneforge.javadoc.analyze.ExecutableInfo;
import stoneforge.javadoc.analyze.FieldInfo;
import stoneforge.javadoc.analyze.MethodInfo;
import stoneforge.javadoc.analyze.SampleInfo;
import stylist.Style;
import stylist.StyleDSL;
import stylist.property.Background.BackgroundImage;
import stylist.value.Color;
import stylist.value.LinearGradient;
import stylist.value.Numeric;

/**
 * 
 */
class APIContentsView extends HTML {

    private JavadocModel model;

    private final ClassInfo info;

    /**
     * @param info
     */
    public APIContentsView(JavadocModel model, ClassInfo info) {
        this.model = model;
        this.info = info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declare() {
        $("section", style.MemberSection, () -> {
            $("code", style.PackcageName, text(info.packageName));
            $("h2", attr("class", info.type), style.TypeName, () -> {
                $("code", style.Name, text(info.name));
                $(info.createTypeVariableNames());
            });

            // type parameters
            int size = info.numberOfTypeVariables();
            if (size != 0) {
                $("ul", style.typeParameter, foŕ(size, i -> {
                    $("li", info.createTypeVariable(i), info.createTypeVariableComment(i));
                }));
            }

            // super types
            List<XML> supers = info.createSuperTypes();
            if (!supers.isEmpty()) {
                $("ul", style.extend, () -> {
                    for (XML sup : supers) {
                        $("li", sup);
                    }
                });
            }

            // implemented types
            List<XML> interfaces = info.createInterfaceTypes();
            if (!interfaces.isEmpty()) {
                $("ul", style.implement, () -> {
                    for (XML xml : interfaces) {
                        $("li", xml);
                    }
                });
            }

            // sub types
            List<XML> subs = info.createSubTypes();
            if (!subs.isEmpty()) {
                $("ul", style.sub, () -> {
                    for (XML xml : subs) {
                        $("li", xml);
                    }
                });
            }

            $(info.createComment());
        });

        for (FieldInfo field : info.fields()) {
            writeMember(field);
        }

        for (ExecutableInfo constructor : info.constructors()) {
            writeMember(constructor);
        }

        for (MethodInfo method : info.methods()) {
            writeMember(method);
        }
    }

    /**
     * Write HTML for each members.
     * 
     * @param member
     */
    private void writeMember(FieldInfo member) {
        $("section", attr("id", member.id()), style.MemberSection, () -> {
            $("h2", style.MemberName, () -> {
                XML type = member.createType();

                $(member.createModifier());
                $("code", style.Name, text(member.name));
                if (type != null) $("i", style.Return, type);
            });

            $(member.createComment());

            List<SampleInfo> list = model.samples.get(info.id() + "#" + member.id());
            if (list != null) {
                for (SampleInfo sample : list) {
                    $("pre", style.Sample, () -> {
                        $("code", attr("class", "language-java"), text(sample.code));
                    });
                }
            }
        });
    }

    /**
     * Write HTML for each members.
     * 
     * @param member
     */
    private void writeMember(ExecutableInfo member) {
        $("section", attr("id", member.id()), style.MemberSection, () -> {
            $("h2", style.MemberName, () -> {
                XML type = member.createReturnType();

                $(member.createModifier());
                $("code", style.Name, text(member.name));
                $(member.createParameter());
                if (type != null) $("i", style.Return, type);
            });

            int types = member.numberOfTypeVariables();
            int params = member.numberOfParameters();
            int returns = member.returnVoid() ? 0 : 1;
            int exceptions = member.numberOfExceptions();

            if (0 < types + params + returns + exceptions) {
                $("table", style.SignatureTable, () -> {
                    IntStream.range(0, types).forEach(i -> {
                        $("tr", style.SignatureTypeVariable, () -> {
                            $("td", member.createTypeVariable(i));
                            $("td", member.createTypeVariableComment(i));
                        });
                    });

                    IntStream.range(0, params).forEach(i -> {
                        $("tr", style.SignatureParameter, () -> {
                            $("td", member.createParameter(i), text(" "), member.createParameterName(i));
                            $("td", member.createParameterComment(i));
                        });
                    });

                    if (0 < returns) {
                        $("tr", style.SignatureReturn, () -> {
                            $("td", member.createReturnType());
                            $("td", member.createReturnComment());
                        });
                    }

                    IntStream.range(0, exceptions).forEach(i -> {
                        $("tr", style.SignatureException, () -> {
                            $("td", member.createException(i));
                            $("td", member.createExceptionComment(i));
                        });
                    });
                });
            }
            $(member.createComment());

            List<SampleInfo> list = model.samples.get(info.id() + "#" + member.id());
            if (list != null) {
                for (SampleInfo sample : list) {
                    $("pre", style.Sample, () -> {
                        $("code", attr("class", "language-java"), text(sample.code));
                    });
                }
            }
        });
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

        Style Sample = () -> {
            margin.top(1, em).bottom(0.3, em);
            position.relative();

            $.select("> code", () -> {
                border.radius(4, px);
            });

            $.after(() -> {
                position.absolute().top(4, px).right(8, px);
                content.text("Example from Testcase");
                font.weight.bold().color(theme.front.opacify(-0.3)).family("sans-serif");
            });
        };
    }
}