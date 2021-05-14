/*
 * Copyright (C) 2019 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package stoneforge.javadoc;

import java.util.Arrays;

import javax.lang.model.element.Modifier;

import kiss.XML;
import stoneforge.javadoc.analyze.ClassInfo;
import stylist.Style;
import stylist.StyleDSL;

public class DocumentPage extends Page {

    /**
     * @param model
     * @param info
     */
    public DocumentPage(JavadocModel model, ClassInfo info) {
        super(model, info);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareContents() {
        $("section", Styles.Section, () -> {
            $(mark(info.createComment()));
        });

        for (ClassInfo child : info.children(Modifier.PUBLIC)) {
            System.out.println(child.id() + "  " + Arrays.toString(child.documentLine()));
            $("section", attr("id", child.id()), Styles.Section, () -> {
                $(mark(child.createComment()));

                for (ClassInfo foot : child.children(Modifier.PUBLIC)) {
                    $("section", attr("id", foot.id()), () -> {
                        $(foot.createComment());
                    });
                }
            });
        }
    }

    private XML mark(XML xml) {
        if (xml != null) {
        }
        return xml;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareSubNavigation() {
    }

    class styles implements StyleDSL {
        Style header = () -> {
            position.sticky().top(80, px);
        };
    }
}
