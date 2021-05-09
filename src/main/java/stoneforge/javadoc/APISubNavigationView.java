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

import stoneforge.HTML;
import stoneforge.javadoc.analyze.ClassInfo;
import stoneforge.javadoc.analyze.ExecutableInfo;
import stoneforge.javadoc.analyze.FieldInfo;
import stoneforge.javadoc.analyze.MemberInfo;
import stoneforge.javadoc.analyze.MethodInfo;
import stylist.Style;
import stylist.StyleDSL;

/**
 * 
 */
class APISubNavigationView extends HTML {

    private final ClassInfo info;

    /**
     * @param info
     */
    public APISubNavigationView(ClassInfo info) {
        this.info = info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declare() {
        members("Constructors", info.constructors());
        members("Static Fields", info.staticFields());
        members("Fields", info.nonStaticFields());
        members("Static Methods", info.staticMethods());
        members("Methods", info.nonStaticMethods());
    }

    private void members(String title, List<? extends MemberInfo> members) {
        if (members.size() != 0) {
            $("h5", Styles.Title, text(title));
            $("ul", foŕ(members, m -> {
                $("li", () -> {
                    $(m.createModifier());
                    $(m.createName());

                    if (m instanceof ExecutableInfo) {
                        ExecutableInfo e = (ExecutableInfo) m;
                        $(e.createParameter());
                    }

                    if (m instanceof MethodInfo) {
                        $("i", Styles.Return, ((MethodInfo) m).createReturnType());
                    }

                    if (m instanceof FieldInfo) {
                        $("i", Styles.Return, ((FieldInfo) m).createType());
                    }
                });
            }));
        }
    }

    /**
     * Style definition.
     */
    private interface Styles extends StyleDSL, BaseStyle {

        Style Title = () -> {
            margin.top(0.9, rem);
            font.weight.bold().size(1, rem);
        };

        Style Return = () -> {
            font.color(theme.secondary.lighten(-30));

            $.before(() -> {
                content.text(":");
                padding.horizontal(0.3, em);
            });
        };
    }
}