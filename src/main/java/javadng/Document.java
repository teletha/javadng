/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import kiss.XML;

public interface Document {

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

    default Optional<Region> region() {
        return Optional.empty();
    }

    /**
     * Build contents.
     * 
     * @return
     */
    XML contents();

    /**
     * Determines if this provider has any contents.
     * 
     * @return
     */
    default boolean hasContents() {
        return contents() != null;
    }

    /**
     * List up all sub contents with the specified modifiers.
     * 
     * @return
     */
    default List<? extends Document> children() {
        return Collections.EMPTY_LIST;
    }
}