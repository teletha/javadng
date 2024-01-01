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

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Modifier;

import kiss.XML;

public interface DocumentProvider {

    /**
     * Compute its identifier.
     * 
     * @return
     */
    String id();

    /**
     * Compute its title.
     * 
     * @return
     */
    String title();

    /**
     * Compute its nest level.
     * 
     * @return
     */
    int nestLevel();

    /**
     * Compute the file path.
     * 
     * @return
     */
    default String filePath() {
        return null;
    }

    /**
     * Determines if this provider has any contents.
     * 
     * @return
     */
    default boolean hasDocument() {
        return document() != null;
    }

    /**
     * Get the line positions of documentation comments for this element.
     * 
     * @return
     */
    default int[] documentLine() {
        return new int[0];
    }

    /**
     * Build contents.
     * 
     * @return
     */
    XML document();

    /**
     * List up all sub contents with the specified modifiers.
     * 
     * @return
     */
    default List<? extends DocumentProvider> children(Modifier... modifier) {
        return Collections.EMPTY_LIST;
    }
}