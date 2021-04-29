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

public interface BaseStyle {

    Theme theme = Theme.with.primary($.rgb(22, 94, 131))
            .secondary($.rgb(250, 210, 50))
            .accent($.rgb(221, 81, 76))
            .front($.rgb(69, 69, 79))
            .back($.rgb(241, 250, 238))
            .link($.rgb(221, 81, 76))
            .baseFont(Font.fromGoogle("Roboto"))
            .titleFont(Font.fromGoogle("Ubuntu"))
            .monoFont(Font.fromGoogle("JetBrains Mono"));

    Font RobotoMono = Font.fromGoogle("Roboto Mono");

    Numeric FontSize = Numeric.of(12, px);

    Numeric MaxWidth = Numeric.of(90, vw);

    Numeric SmallGap = Numeric.of(1, px);

    Numeric BlockVerticalGap = Numeric.of(0.5, rem);

    Numeric BlockBorderWidth = Numeric.of(3, px);

    Numeric BlockHorizontalGap = Numeric.of(10, px);

    Numeric HeaderHeight = Numeric.of(80, px);

    Numeric RightNavigationWidth = Numeric.of(20, vw);

    double LineHeight = 1.5;

}