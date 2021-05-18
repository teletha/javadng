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

import stylist.Theme;
import stylist.value.Font;
import stylist.value.Numeric;

public interface StyleConstants {

    Theme theme = Theme.with.primary($.hsl(200, 70, 30))
            .secondary($.hsl(48, 95, 60))
            .accent($.hsl(0, 55, 60))
            .front($.hsl(0, 0, 45))
            .back($.hsl(105, 55, 96))
            .link($.hsl(0, 55, 60))
            .baseFont(Font.fromGoogle("Roboto"))
            .condensedFont(Font.fromGoogle("Fira Sans Condensed"))
            .titleFont(Font.fromGoogle("Ubuntu"))
            .monoFont(Font.fromGoogle("JetBrains Mono"))
            .borderRadius(4, px);

    Numeric FontSize = Numeric.of(13, px);

    Numeric MaxWidth = Numeric.of(90, vw);

    Numeric SmallGap = Numeric.of(1, px);

    Numeric BlockVerticalGap = Numeric.of(0.5, rem);

    Numeric BlockBorderWidth = Numeric.of(3, px);

    Numeric BlockHorizontalGap = Numeric.of(10, px);

    Numeric HeaderHeight = Numeric.of(80, px);

    Numeric RightNavigationWidth = Numeric.of(20, vw);

    double LineHeight = 1.7;
}