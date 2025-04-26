/*
 * Copyright (C) 2025 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng;

import java.util.ArrayList;
import java.util.List;

import kiss.Managed;
import kiss.Singleton;

@Managed(Singleton.class)
public class SiteSetting {

    /** The site name. */
    public String name = "";

    /** The site description. */
    public String description = "";

    /** The registory of {@link NavigationLink}. */
    private final List<NavigationLink> navigations = new ArrayList();

    public SiteSetting register(NavigationLink link) {
        if (link != null) {
            navigations.add(link);
        }
        return this;
    }

    public List<NavigationLink> links() {
        return navigations;
    }
}
