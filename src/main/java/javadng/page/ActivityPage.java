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

public class ActivityPage extends Page<XML> {

    /**
     * @param model
     */
    public ActivityPage(int depth, JavadocModel model, XML xml) {
        super(depth, model, xml);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareContents() {
        XML xml = contents.parent().find(">section");
        for (XML section : xml) {
            $(xml(section), Styles.JavadocComment, Styles.Section);
        }
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
