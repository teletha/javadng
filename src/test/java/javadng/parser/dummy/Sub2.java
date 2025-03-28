/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.parser.dummy;

public class Sub2<P, Q extends Sub2<P, Q>> extends Parameterized<P, Q> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
    }
}