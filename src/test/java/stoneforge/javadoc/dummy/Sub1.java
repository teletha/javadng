/*
 * Copyright (C) 2020 stoneforge Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package stoneforge.javadoc.dummy;

public class Sub1<P, Q extends Sub1<P, Q>> extends Parameterized<P, Q> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
    }
}