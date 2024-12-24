/*
 * Copyright (C) 2024 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package apocrypha.doc;

import java.util.Arrays;
import java.util.List;

public interface Tag {

    Tag NONE = of("none");

    /**
     * Tag name.
     * 
     * @return
     */
    String name();

    /**
     * Create tag by name.
     * 
     * @param tag
     * @return
     */
    static Tag of(String tag) {
        if (tag == null || tag.isBlank()) {
            return NONE;
        } else {
            return () -> tag;
        }
    }

    /**
     * Create tags by names.
     * 
     * @param tags
     * @return
     */
    static List<Tag> of(String... tags) {
        return Arrays.stream(tags).map(Tag::of).toList();
    }
}
