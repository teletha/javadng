/*
 * Copyright (C) 2023 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package apocrypha;

import icy.manipulator.Icy;
import icy.manipulator.Icy.Property;

@Icy
public interface ApocryphaModel {

    @Property
    String name();

}
