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

import kiss.I;
import kiss.XML;
import stoneforge.HTML;
import stoneforge.javadoc.analyze.ClassInfo;
import stoneforge.javadoc.analyze.ExecutableInfo;
import stoneforge.javadoc.analyze.MethodInfo;
import stoneforge.javadoc.analyze.SampleInfo;
import stylist.Style;
import stylist.StyleDSL;
import stylist.value.Color;
import stylist.value.Numeric;

/**
 * 
 */
class ContentsView extends HTML {

    private JavadocModel model;

    private final ClassInfo info;

    /**
     * @param info
     */
    public ContentsView(JavadocModel model, ClassInfo info) {
        this.model = model;
        this.info = info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declare() {
        $("section", style.TypeSection, () -> {
            $("div", style.PackcageName, code(info.packageName));

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

            $("div", style.TypeComment, () -> {
                $(info.createComment());
            });

            for (ExecutableInfo constructor : info.constructors()) {
                writeMember(constructor);
            }

            for (MethodInfo method : info.methods()) {
                writeMember(method);
            }
        });
    }

    /**
     * Write HTML for each members.
     * 
     * @param member
     */
    private void writeMember(ExecutableInfo member) {
        $("section", style.MemberSection, () -> {
            $("h2", attr("id", member.id()), style.MemberName, () -> {
                XML type = member.createReturnType();

                $(member.createModifier());
                $("code", style.Name, member.createName());
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
                    $("div", style.SampleDesc, xml(sample.comment.or(I.xml("<span>Example by Testcase</span>"))));
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

        Style TypeSection = () -> {
            margin.top(2, rem).bottom(2, rem);
        };

        Style TypeComment = () -> {
            font.size(14, px);
            margin.top(2, rem).bottom(2, rem);
        };

        Style MemberSection = () -> {
            margin.vertical(1.6, rem);
            padding.size(1.3, rem);
            border.radius(4, px);
            background.color(Color.White);
        };

        Style PackcageName = () -> {
            font.size.small();
        };

        Style TypeName = () -> {
            font.family(Roboto).size(1.2, rem).weight.normal();
            display.block();
            margin.top(0.5, rem).bottom(0.3, rem);
        };

        Style MemberName = () -> {
            font.family(Roboto).size(1, rem).weight.normal();
            display.block();
        };

        Style Name = () -> {
            font.weight.bold().style.normal();
            margin.right(0.175, rem);
        };

        Style Return = () -> {
            font.color(palette.secondary.lighten(-30));

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
                line.height(1.3);

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
                font.color(palette.secondary.lighten(-30));
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
                font.color(palette.font.lighten(30)).size.smaller();
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