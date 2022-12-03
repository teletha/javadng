/*
 * Copyright (C) 2022 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.page;

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
    String filePath();

    /**
     * Determines if this provider has any contents.
     * 
     * @return
     */
    boolean hasDocument();

    /**
     * Get the line positions of documentation comments for this element.
     * 
     * @return
     */
    int[] documentLine();

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
    List<? extends DocumentProvider> children(Modifier... modifier);
}
