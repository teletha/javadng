/*
 * Copyright (C) 2024 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.design;

import kiss.I;
import stylist.Query;
import stylist.StyleDSL;
import stylist.value.Numeric;

/**
 * Base style for Javadng.
 */
public interface JavadngStyleDSL extends StyleDSL {

    /** The singleton. */
    Design Theme = I.make(Design.class);

    Query Small = Query.screen().width(0, 900, px);

    Numeric MaxWidth = Numeric.of(1360, px);

    Numeric NaviWidth = Numeric.minmax(200, px, 300, px);

    Numeric MaxSubNaviWidth = Numeric.of(280, px);

    Numeric SmallGap = Numeric.of(1, px);

    Numeric BlockVerticalGap = Numeric.of(0.5, rem);

    Numeric BlockBorderWidth = Numeric.of(3, px);

    Numeric BlockHorizontalGap = Numeric.of(10, px);

    Numeric HeaderHeight = Numeric.of(80, px);

    Numeric HeaderBottomMargin = Numeric.of(12, px);

    Numeric RightNavigationWidth = Numeric.of(20, vw);

}