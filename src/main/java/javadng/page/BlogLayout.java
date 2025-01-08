/*
 * Copyright (C) 2025 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
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
            display.grid().templateColumns.size(0.6, 1.6, 0.6, fr).templateRows.size(0.4, 1.6, fr)
                    .gap(0, px)
                    .templateAreas(header, header, header)
                    .templateAreas(navi, article, aside);
        };
    }
}
