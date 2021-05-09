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

import java.util.List;

import javax.lang.model.element.Modifier;

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
        $(new DocumentContentsView(model, info));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareSubNavigation() {
        $("ol", () -> {
            for (ClassInfo child : info.children(Modifier.PUBLIC)) {
                $("li", () -> {
                    $("a", attr("href", "#" + child.id()), text(child.title()));

                    List<ClassInfo> grands = child.children(Modifier.PUBLIC);
                    if (!grands.isEmpty()) {
                        $("ol", () -> {
                            for (ClassInfo grand : grands) {
                                $("li", () -> {
                                    $("a", attr("href", "#" + grand.id()), text(grand.title()));
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}
