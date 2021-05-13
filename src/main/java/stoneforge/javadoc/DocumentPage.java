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
            $(info.createComment());
        });

        for (ClassInfo child : info.children()) {
            $("section", attr("id", child.id()), Styles.Section, () -> {
                $(child.createComment());

                for (ClassInfo foot : child.children()) {
                    $(foot.createComment());
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareSubNavigation() {
    }
}
