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

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import antibug.CleanRoom;
import kiss.I;

public class MimicTest {

    @RegisterExtension
    CleanRoom room = new CleanRoom();

    @Test
    void dom() {
        assert dom("""
                <div>
                    <span>1</span>
                </div>
                """, """
                $("span")
                """, """
                <span>1</span>
                """);
    }

    private boolean dom(String input, String js, String output) {
        try (Engine engine = Engine.newBuilder().build()) {
            // create test code
            String code = """
                    import {Mimic as $} from ""
                    """;

            Source source = Source.newBuilder("js", new File("index.mjs")).build();
            try (Context context = Context.newBuilder("js").engine(engine).allowIO(true).allowExperimentalOptions(true).build()) {
                Value parsed = context.parse(source);

                Value result = context.eval("js", "console.log($)");
                System.out.println(result);
            }
        } catch (Exception e) {
            throw I.quiet(e);
        }
        return true;
    }
}
