/*
 * Copyright (C) 2023 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package apocrypha;

import java.util.List;
import java.util.Locale;

import icy.manipulator.Icy;
import icy.manipulator.Icy.Property;
import kiss.Managed;
import kiss.Singleton;

@Icy
@Managed(Singleton.class)
public abstract class ApocryphaModel {

    /**
     * Configure the site name.
     * 
     * @return Your site name.
     */
    @Property
    public abstract String name();

    /**
     * Configure the supported {@link Locale}, the first item is used as default locale.
     * 
     * @return The list of supported locales.
     */
    @Property
    public List<Locale> locale() {
        return List.of(Locale.getDefault());
    }

    /**
     * Get the base locale.
     * 
     * @return
     */
    public Locale baseLocale() {
        List<Locale> locales = locale();
        return locales.isEmpty() ? Locale.getDefault() : locales.get(0);
    }
}
