/*
 * Copyright (C) 2024 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package apocrypha.doc;

import java.io.Reader;

import kiss.Extensible;

public interface DocConverter<T> extends Extensible {

    Doc convert(Reader source);
}
