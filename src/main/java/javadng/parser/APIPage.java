/*
 * Copyright (C) 2019 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.parser;

import java.util.List;
import java.util.stream.IntStream;

import javadng.parser.analyze.ClassInfo;
import javadng.parser.analyze.ExecutableInfo;
import javadng.parser.analyze.FieldInfo;
import javadng.parser.analyze.MemberInfo;
import javadng.parser.analyze.MethodInfo;
import javadng.parser.analyze.SampleInfo;
import kiss.XML;
import stylist.Style;
import stylist.StyleDSL;
import stylist.value.Color;
import stylist.value.Numeric;

public class APIPage extends Page<ClassInfo> {

    /**
     * @param depth
     * @param model
     * @param info
     */
    public APIPage(int depth, JavadocModel model, ClassInfo info) {
        super(depth, model, info);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareContents() {
        $("section", Styles.Section, () -> {
            $("code", style.PackcageName, text(contents.packageName));
            $("h2", attr("class", contents.type), style.TypeName, () -> {
                $("code", style.Name, text(contents.name));
                $(contents.createTypeVariableNames());
            });

            // type parameters
            int size = contents.numberOfTypeVariables();
            if (size != 0) {
                $("ul", style.typeParameter, foŕ(size, i -> {
                    $("li", contents.createTypeVariable(i), contents.createTypeVariableComment(i));
                }));
            }

            // super types
            List<XML> supers = contents.createSuperTypes();
            if (!supers.isEmpty()) {
                $("ul", style.extend, () -> {
                    for (XML sup : supers) {
                        $("li", sup);
                    }
                });
            }

            // implemented types
            List<XML> interfaces = contents.createInterfaceTypes();
            if (!interfaces.isEmpty()) {
                $("ul", style.implement, () -> {
                    for (XML xml : interfaces) {
                        $("li", xml);
                    }
                });
            }

            // sub types
            List<XML> subs = contents.createSubTypes();
            if (!subs.isEmpty()) {
                $("ul", style.sub, () -> {
                    for (XML xml : subs) {
                        $("li", xml);
                    }
                });
            }

            $(contents.createComment());
        });

        for (FieldInfo field : contents.fields()) {
            writeMember(field);
        }

        for (ExecutableInfo constructor : contents.constructors()) {
            writeMember(constructor);
        }

        for (MethodInfo method : contents.methods()) {
            writeMember(method);
        }
    }

    /**
     * Write HTML for each members.
     * 
     * @param member
     */
    private void writeMember(FieldInfo member) {
        $("section", attr("id", member.id()), Styles.Section, () -> {
            $("h2", style.MemberName, () -> {
                XML type = member.createType();

                $(member.createModifier());
                $("code", style.Name, text(member.name));
                if (type != null) $("i", style.Return, type);
            });

            $(member.createComment());

            List<SampleInfo> list = model.samples.get(contents.id() + "#" + member.id());
            if (list != null) {
                for (SampleInfo sample : list) {
                    $("pre", () -> {
                        $("code", attr("class", "lang-java"), text(sample.code));
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
        $("section", attr("id", member.id()), Styles.Section, () -> {
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

            List<SampleInfo> list = model.samples.get(contents.id() + "#" + member.id());
            if (list != null) {
                for (SampleInfo sample : list) {
                    $("pre", () -> {
                        $("code", attr("class", "lang-java"), text(sample.code));
                    });
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareSubNavigation() {
        members("Constructors", contents.constructors());
        members("Static Fields", contents.staticFields());
        members("Fields", contents.nonStaticFields());
        members("Static Methods", contents.staticMethods());
        members("Methods", contents.nonStaticMethods());
    }

    private void members(String title, List<? extends MemberInfo> members) {
        if (members.size() != 0) {
            $("h5", style.Title, text(title));
            $("ul", foŕ(members, m -> {
                $("li", () -> {
                    $(m.createModifier());
                    $(m.createName());

                    if (m instanceof ExecutableInfo) {
                        ExecutableInfo e = (ExecutableInfo) m;
                        $(e.createParameter());
                    }

                    if (m instanceof MethodInfo) {
                        $("i", style.NaviReturn, ((MethodInfo) m).createReturnType());
                    }

                    if (m instanceof FieldInfo) {
                        $("i", style.NaviReturn, ((FieldInfo) m).createType());
                    }
                });
            }));
        }
    }

    /**
     * Style definition.
     */
    private interface style extends StyleDSL, StyleConstants {

        Color keyword = Color.hsl(0, 29, 49);

        Color RETURN = theme.secondary.lighten(theme.back, -25);

        Numeric signatureLabelWidth = Numeric.of(2.5, rem);

        Style PackcageName = () -> {
            font.size(9, px).color("#999");
            display.block();
            margin.bottom(-5, px).left(22, px);
        };

        Style TypeName = () -> {
            font.family(theme.base).size(1.2, rem).weight.normal();
            margin.bottom(0.3, rem);
        };

        Style MemberName = () -> {
            font.family(theme.base).size(1, rem).weight.normal();
            display.block();
        };

        Style Name = () -> {
            font.weight.bold().style.normal();
            margin.right(0.175, rem);
        };

        Style Return = () -> {
            font.color(RETURN);

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
                font.color(RETURN);
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

        Style Title = () -> {
            margin.top(0.9, rem);
            font.weight.bold().size(1, rem);
        };

        Style NaviReturn = () -> {
            font.color(RETURN);

            $.before(() -> {
                content.text(":");
                padding.horizontal(0.3, em);
            });
        };
    }
}
