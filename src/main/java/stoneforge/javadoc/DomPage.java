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

import kiss.XML;
import stylist.StyleDSL;

public class DomPage extends Page<XML> {

    /**
     * @param model
     */
    public DomPage(JavadocModel model, XML xml) {
        super(model, xml);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareContents() {
        $("section", Styles.Section, Styles.JavadocComment, () -> {
            $(xml(contents));
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareSubNavigation() {
    }

    interface styles extends StyleDSL, StyleConstants {
    }
}
