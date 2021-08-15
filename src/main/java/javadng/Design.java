/*
 * Copyright (C) 2021 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng;

import static stylist.StyleDSL.*;

import stylist.design.AbstractDesignScheme;
import stylist.design.Theme;
import stylist.image.Image;
import stylist.value.Color;
import stylist.value.Numeric;

public class Design extends AbstractDesignScheme {

    public String backImage;

    @Theme(main = true)
    void light() {
        primary = $.hsl(200, 70, 30);
        secondary = $.hsl(48, 95, 60);
        accent = $.hsl(0, 55, 60);

        front = $.hsl(0, 0, 45);
        link = $.hsl(0, 55, 60);

        back = $.hsl(185, 43, 95);
        backImage = Image.slash(Color.rgb(220, 222, 225, 0.7), 3);
        surface = $.hsl(0, 0, 98);

        base = fontFromGoogle("Roboto");
        condensed = fontFromGoogle("Fira Sans Condensed");
        title = fontFromGoogle("Ubuntu");
        mono = fontFromGoogle("JetBrains Mono");
        icon = fontFromGoogle("Material Icons");

        font = Numeric.of(13, px);
        line = Numeric.of(1.7);
        radius = Numeric.of(4, px);
    }

    @Theme
    void dark() {
        primary = $.hsl(200, 70, 50);
        accent = $.hsl(0, 65, 65);

        front = $.hsl(0, 0, 95);
        link = $.hsl(0, 65, 65);

        back = $.hsl(0, 0, 20);
        backImage = Image.slash(Color.rgb(10, 15, 35, 0.7), 3);
        surface = $.hsl(0, 0, 30);

    }
}
