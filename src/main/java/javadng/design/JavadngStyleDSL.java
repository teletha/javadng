/*
 * Copyright (C) 2022 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.design;

import kiss.I;
import stylist.MediaQuery;
import stylist.StyleDSL;
import stylist.value.Numeric;

/**
 * Base style for Javadng.
 */
public interface JavadngStyleDSL extends StyleDSL {

    /** The singleton. */
    Design Theme = I.make(Design.class);

    MediaQuery Small = MediaQuery.screen().maxWidth(900, px);

    Numeric MaxWidth = Numeric.of(1300, px);

    Numeric MaxNaviWidth = Numeric.of(220, px);

    Numeric MaxSubNaviWidth = Numeric.of(220, px);

    Numeric SmallGap = Numeric.of(1, px);

    Numeric BlockVerticalGap = Numeric.of(0.5, rem);

    Numeric BlockBorderWidth = Numeric.of(3, px);

    Numeric BlockHorizontalGap = Numeric.of(10, px);

    Numeric HeaderHeight = Numeric.of(80, px);

    Numeric HeaderBottomMargin = Numeric.of(12, px);

    Numeric RightNavigationWidth = Numeric.of(20, vw);

}
