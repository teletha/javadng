/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.page;

import java.util.List;
import java.util.stream.IntStream;

import javadng.design.JavadngDSL;
import javadng.design.Styles;
import javadng.javadoc.ClassInfo;
import javadng.javadoc.ExecutableInfo;
import javadng.javadoc.FieldInfo;
import javadng.javadoc.MemberInfo;
import javadng.javadoc.MethodInfo;
import javadng.javadoc.SampleInfo;
import kiss.XML;
import stylist.Style;
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
            $("code", css.PackcageName, text(contents.packageName));
            $("h2", clazz(contents.type), css.TypeName, () -> {
                $("code", css.Name, text(contents.name));
                $(contents.createTypeVariableNames());
            });

            $("div", css.traitList, () -> {
                // type parameters
                int size = contents.numberOfTypeVariables();
                if (size != 0) {
                    $("ul", css.typeParameter, foŕ(size, i -> {
                        $("li", contents.createTypeVariable(i), contents.createTypeVariableComment(i));
                    }));
                }

                // super types
                List<XML> supers = contents.createSuperTypes();
                if (!supers.isEmpty()) {
                    $("ul", css.extend, () -> {
                        for (XML sup : supers) {
                            $("li", sup);
                        }
                    });
                }

                // implemented types
                List<XML> interfaces = contents.createInterfaceTypes();
                if (!interfaces.isEmpty()) {
                    $("ul", css.implement, () -> {
                        for (XML xml : interfaces) {
                            $("li", xml);
                        }
                    });
                }

                // sub types
                List<XML> subs = contents.createSubTypes();
                if (!subs.isEmpty()) {
                    $("ul", css.sub, () -> {
                        for (XML xml : subs) {
                            $("li", xml);
                        }
                    });
                }
            });

            $(contents.contents());
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
        $("section", id(member.id()), Styles.Section, () -> {
            $("h2", css.MemberName, () -> {
                XML type = member.createType();

                $(member.createModifier());
                $("code", css.Name, text(member.name));
                if (type != null) $("i", css.Return, type);
            });

            $(member.contents());

            List<SampleInfo> list = model.samples.get(contents.id() + "#" + member.id());
            if (list != null) {
                for (SampleInfo sample : list) {
                    $("pre", clazz("lang-java"), () -> {
                        $("code", text(sample.code));
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
        $("section", id(member.id()), Styles.Section, () -> {
            $("h2", css.MemberName, () -> {
                XML type = member.createReturnType();

                $(member.createModifier());
                $("code", css.Name, text(member.name));
                $(member.createParameter());
                if (type != null) $("i", css.Return, type);
            });

            int types = member.numberOfTypeVariables();
            int params = member.numberOfParameters();
            int returns = member.returnVoid() ? 0 : 1;
            int exceptions = member.numberOfExceptions();

            if (0 < types + params + returns + exceptions) {
                $("table", css.SignatureTable, () -> {
                    IntStream.range(0, types).forEach(i -> {
                        $("tr", css.SignatureTypeVariable, () -> {
                            $("td", member.createTypeVariable(i));
                            $("td", member.createTypeVariableComment(i));
                        });
                    });

                    IntStream.range(0, params).forEach(i -> {
                        $("tr", css.SignatureParameter, () -> {
                            $("td", member.createParameter(i), text(" "), member.createParameterName(i));
                            $("td", member.createParameterComment(i));
                        });
                    });

                    if (0 < returns) {
                        $("tr", css.SignatureReturn, () -> {
                            $("td", member.createReturnType());
                            $("td", member.createReturnComment());
                        });
                    }

                    IntStream.range(0, exceptions).forEach(i -> {
                        $("tr", css.SignatureException, () -> {
                            $("td", member.createException(i));
                            $("td", member.createExceptionComment(i));
                        });
                    });
                });
            }
            $(member.contents());

            List<SampleInfo> list = model.samples.get(contents.id() + "#" + member.id());
            if (list != null) {
                for (SampleInfo sample : list) {
                    $("pre", clazz("lang-java"), () -> {
                        $("code", text(sample.code));
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
        $("div", css.outline, () -> {
            members("Constructors", contents.constructors());
            members("Static Fields", contents.staticFields());
            members("Fields", contents.nonStaticFields());
            members("Static Methods", contents.staticMethods());
            members("Methods", contents.nonStaticMethods());
        });
    }

    private void members(String title, List<? extends MemberInfo> members) {
        if (members.size() != 0) {
            $("h5", css.title, text(title));
            $("ul", foŕ(members, m -> {
                $("li", css.member, () -> {
                    $(m.createModifier());
                    $(m.createName());

                    if (m instanceof ExecutableInfo) {
                        ExecutableInfo e = (ExecutableInfo) m;
                        $(e.createParameter());
                    }

                    if (m instanceof MethodInfo) {
                        $("i", css.returnValue, ((MethodInfo) m).createReturnType());
                    }

                    if (m instanceof FieldInfo) {
                        $("i", css.returnValue, ((FieldInfo) m).createType());
                    }
                });
            }));
        }
    }

    /**
     * Style definition.
     */
    private interface css extends JavadngDSL {

        Style outline = () -> {
            JavadngDSL.scrollable();
            display.block().height($.num(91, vh).subtract(15, px)).maxWidth(JavadngDSL.RightNavigationWidth);
            text.whiteSpace.nowrap();
            font.size(0.8, em);
        };

        Color keyword = Color.hsl(0, 29, 49);

        Color RETURN = Theme.secondary.lighten(Theme.back, -25);

        Color TYPEVARIABLE = Theme.secondary.lighten(Theme.back, 25);

        Numeric signatureLabelWidth = Numeric.num(3, rem);

        Style PackcageName = () -> {
            font.size(9, px).color("#999");
            display.block();
            margin.bottom(-5, px).left(22, px);
        };

        Style TypeName = () -> {
            font.family(JavadngDSL.Theme.base).size(1.2, rem).weight.normal();
            margin.bottom(0.3, rem);
        };

        Style MemberName = () -> {
            font.family(JavadngDSL.Theme.base).size(1, rem).weight.normal();
            display.block();
            margin.left(-1.1, rem);
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
            margin.top(0.2, rem).bottom(2, rem);
            font.size(0.85, em);

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
                font.color(TYPEVARIABLE);
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

        Style traitList = () -> {
            font.size(0.85, em);
            margin.bottom(2, em);
        };

        Style traits = () -> {
            Numeric pad = Numeric.num(6, rem);

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
                font.color(JavadngDSL.Theme.front.lighten(30)).size.smaller();
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

        Style parameterName = Style.named(".parameterName", () -> {
            padding.left(0.3, em);
        });

        Style title = () -> {
            margin.top(0.9, rem);
            font.weight.bold().size(1, rem);
        };

        Style member = () -> {
            padding.vertical(0.1, em);

            $.has("a.now", () -> {
                font.color(Theme.accent).weight.bold();
            });
        };

        Style returnValue = () -> {
            font.color(RETURN);

            $.before(() -> {
                content.text(":");
                padding.horizontal(0.3, em);
            });
        };
    }
}