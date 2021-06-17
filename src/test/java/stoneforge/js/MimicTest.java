/*
 * Copyright (C) 2021 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package stoneforge.js;

import java.io.File;
import java.io.IOException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;

public class MimicTest {

    @Test
    void testName() throws IOException {
        try (Engine engine = Engine.newBuilder().build()) {
            Source source = Source.newBuilder("js", new File("index.mjs")).build();
            try (Context context = Context.newBuilder("js").engine(engine).allowIO(true).allowExperimentalOptions(true).build()) {
                context.eval(source);

                Value parse = context.eval("js", "JSON.parse");
                Value stringify = context.eval("js", "JSON.stringify");
                Value x = stringify.execute(parse.execute("""
                        {"test": 1}
                        """), null, 2);
                System.out.println(x.asString());

                Value result = context.eval("js", "console.log($)");
                System.out.println(result);
            }
        }
    }
}
