/*
 * Copyright (C) 2020 stoneforge Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package stoneforge.javadoc;

import static stylist.StyleDSL.*;

import kiss.I;
import stoneforge.Design;
import stylist.value.Numeric;

public interface StyleConstants {

    Design theme = I.make(Design.class);

    Numeric MaxWidth = Numeric.of(90, vw);

    Numeric SmallGap = Numeric.of(1, px);

    Numeric BlockVerticalGap = Numeric.of(0.5, rem);

    Numeric BlockBorderWidth = Numeric.of(3, px);

    Numeric BlockHorizontalGap = Numeric.of(10, px);

    Numeric HeaderHeight = Numeric.of(80, px);

    Numeric RightNavigationWidth = Numeric.of(20, vw);
}