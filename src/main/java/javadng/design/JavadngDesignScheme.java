/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.design;

import static stylist.StyleDSL.*;

import stylist.design.AbstractDesignScheme;
import stylist.design.Theme;
import stylist.image.Image;
import stylist.value.Color;
import stylist.value.Numeric;

public class JavadngDesignScheme extends AbstractDesignScheme {

    public String backImage;

    @Theme(main = true)
    void light() {
        primary = $.hsl(200, 70, 30);
        secondary = $.hsl(97, 65, 40);
        accent = $.hsl(0, 55, 60);

        front = $.hsl(0, 0, 30);
        link = $.hsl(0, 55, 60);

        back = $.hsl(185, 43, 95);
        backImage = Image.slash(Color.rgb(220, 222, 225, 0.7), 3);
        surface = $.hsl(0, 0, 98);

        base = fontFromGoogle("Inter");
        title = fontFromGoogle("Vollkorn");
        mono = fontFromGoogle("Inter");
        icon = fontFromGoogle("Material Icons");

        font = Numeric.num(15, px);
        line = Numeric.num(1.7);
        radius = Numeric.num(4, px);
    }

    @Theme
    void dark() {
        primary = $.hsl(0, 40, 80);
        secondary = $.hsl(48, 95, 60);
        accent = $.hsl(0, 65, 65);

        front = $.hsl(240, 13, 87);
        link = $.hsl(0, 65, 65);

        back = $.hsl(0, 0, 20);
        backImage = Image.slash(Color.rgb(10, 15, 35, 0.7), 3);
        surface = $.hsl(0, 0, 23);
    }
}