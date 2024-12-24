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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import icy.manipulator.Icy;
import icy.manipulator.Icy.Property;

@Icy
public interface DocModel {

    @Property
    String title();

    @Property
    Author author();

    @Property
    LocalDate published();

    @Property
    default LocalDate edited() {
        return published();
    }

    @Property
    default Category category() {
        return Category.NONE;
    }

    @Property
    default List<Tag> tags() {
        return Collections.EMPTY_LIST;
    }

    @Property
    default String text() {
        return "";
    }
}
