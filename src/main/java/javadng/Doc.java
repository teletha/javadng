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

/**
 * Scanned doc data.
 */
public class Doc {

    public String title;

    public String path;

    public List<Doc> subs = new ArrayList();
}