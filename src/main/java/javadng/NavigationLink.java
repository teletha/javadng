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

/**
 * Represents a navigation link, typically used in application menus or navigation bars.
 * Each link consists of an identifier, a path (usually a URL or route), and an icon name or path.
 *
 * @param id a unique identifier for the navigation link
 * @param path the path or URL the link points to
 * @param icon a string representing the icon (e.g., name, CSS class, or image path)
 */
public record NavigationLink(String id, String path, String icon) {
}
