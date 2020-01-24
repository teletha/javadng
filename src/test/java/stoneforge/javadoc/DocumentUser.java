/*
 * Copyright (C) 2019 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package stoneforge.javadoc;

import java.io.IOException;

public class DocumentUser {

    public static void main(String[] args) throws IOException {
        Javadoc.with.sources("src/main/java", "src/test/java")
                .output("docs/api")
                .product("Stoneforge")
                .project("Stoneforge")
                .version("1.0")
                .useExternalJDKDoc()
                .build();
    }
}
