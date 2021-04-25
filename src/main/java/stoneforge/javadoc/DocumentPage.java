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
        $(new DocumentContentsView(model, info));
    }

    private String strip(String text) {
        return text.replace('_', ' ').replaceAll(".+\\.", "");
    }

    private interface styles extends StyleDSL, BaseStyle {
        Style navi = () -> {
            font.size(1, rem);
            margin.left(2, rem);
        };

        Style section = () -> {
            font.size(1, rem);
            margin.left(1.1, rem);
            listStyle.disclosureClose();
        };
    }
}
