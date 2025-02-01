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

    Numeric MaxWidth = Numeric.num(1360, px);

    Numeric MaxSubNaviWidth = Numeric.num(280, px);

    Numeric SmallGap = Numeric.num(1, px);

    Numeric BlockVerticalGap = Numeric.num(0.5, rem);

    Numeric BlockBorderWidth = Numeric.num(3, px);

    Numeric BlockHorizontalGap = Numeric.num(10, px);

    Numeric HeaderBottomMargin = Numeric.num(12, px);

    Numeric RightNavigationWidth = Numeric.num(20, vw);

}