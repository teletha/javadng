/*
 * Copyright (C) 2020 stoneforge Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.parser;

import static stylist.StyleDSL.*;

import javadng.Design;
import kiss.I;
import stylist.MediaQuery;
import stylist.value.Numeric;

public interface StyleConstants {

    MediaQuery Small = MediaQuery.screen().maxWidth(900, px);

    Design theme = I.make(Design.class);

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