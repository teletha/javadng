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
public interface JavadngDSL extends StyleDSL {

    /** The singleton. */
    JavadngDesignScheme Theme = I.make(JavadngDesignScheme.class);

    Query Small = Query.screen().width(0, 900, px);

    Numeric MaxWidth = Numeric.num(1360, px);

    Numeric MaxSubNaviWidth = Numeric.num(280, px);

    Numeric BlockVerticalGap = Numeric.num(0.5, rem);

    Numeric RightNavigationWidth = Numeric.num(20, vw);

}