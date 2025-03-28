/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.page;

import stylist.Style;
import stylist.StyleDSL;

public class BlogLayout extends Component {

    /**
     * {@inheritDoc}
     */
    @Override
    public void declare() {
        $("div", css.container, () -> {
            $("header");
            $("article");
            $("navi");
            $("aside");
        });
    }

    interface css extends StyleDSL {
        Style header = () -> {
        };

        Style article = () -> {
        };

        Style navi = () -> {
        };

        Style aside = () -> {
        };

        Style container = () -> {
            display.grid()
                    .column(x -> x.size(0.6, 1.6, 0.6, fr))
                    .row(x -> x.size(0.4, 1.6, fr))
                    .gap(0, px)
                    .area(header, header, header)
                    .area(navi, article, aside);
        };
    }
}