/*
 * Copyright (C) 2021 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package stoneforge;

import static stylist.StyleDSL.*;

import kiss.I;
import stylist.design.DesignScheme;
import stylist.design.Theme;
import stylist.value.Font;
import stylist.value.Numeric;

public class Design extends DesignScheme {

    public static final Design theme = I.make(Design.class);

    @Theme(main = true)
    void light() {
        primary = $.hsl(200, 70, 30);
        secandary = $.hsl(48, 70, 30);
        accent = $.hsl(0, 55, 60);

        text = $.hsl(0, 0, 45);
        link = $.hsl(0, 55, 60);

        back = $.hsl(185, 43, 95);
        surface = $.hsl(0, 0, 98);

        base = Font.fromGoogle("Roboto");
        condensed = Font.fromGoogle("Fira Sans Condensed");
        title = Font.fromGoogle("Ubuntu");
        mono = Font.fromGoogle("JetBrains Mono");

        radius = Numeric.of(4, px);
    }

    @Theme
    void dark() {
        primary = $.hsl(200, 70, 50);
        secandary = $.hsl(48, 70, 30);
        accent = $.hsl(0, 55, 50);

        text = $.hsl(0, 0, 95);
        link = $.hsl(0, 55, 45);

        back = $.hsl(0, 0, 10);
        surface = $.hsl(0, 0, 15);

        base = Font.fromGoogle("Roboto");
        condensed = Font.fromGoogle("Fira Sans Condensed");
        title = Font.fromGoogle("Ubuntu");
        mono = Font.fromGoogle("JetBrains Mono");

        radius = Numeric.of(4, px);
    }
}
