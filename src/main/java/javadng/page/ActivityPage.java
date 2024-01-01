/*
 * Copyright (C) 2024 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.page;

public class ActivityPage extends DocumentPage {

    /**
     * @param depth
     * @param model
     * @param content
     */
    public ActivityPage(int depth, JavadocModel model, DocumentProvider content) {
        super(depth, model, content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void declareSubNavigation() {
        $("ul", foŕ(contents.children(), section -> {
            $("li", () -> {
                $("a", attr("href", "doc/changelog.html#" + section.id()), text(section.title()));
            });
        }));
    }
}