/*
 * Copyright (C) 2019 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.page;

import javadng.design.JavadngStyleDSL;
import javadng.design.Styles;
import kiss.XML;

public class DomPage extends Page<XML> {

    /**
     * @param model
     */
    public DomPage(int depth, JavadocModel model, XML xml) {
        super(depth, model, xml);
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

    interface S extends JavadngStyleDSL {
    }
}
