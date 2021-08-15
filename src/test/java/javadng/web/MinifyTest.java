/*
 * Copyright (C) 2021 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.web;

import org.junit.jupiter.api.Test;

class MinifyTest {

    @Test
    void equal() {
        assert Minify.minify("var test = 0;").equals("var test=0;");
    }

    @Test
    void arithmetic() {
        assert Minify.minify("1 + 2").equals("1+2");
        assert Minify.minify("1 - 2").equals("1-2");
        assert Minify.minify("1 * 2").equals("1*2");
        assert Minify.minify("1 / 2").equals("1/2");
        assert Minify.minify("1 % 2").equals("1%2");
    }

    @Test
    void bracket() {
        assert Minify.minify(" { name:value } ").equals("{name:value}");
    }

    @Test
    void array() {
        assert Minify.minify(" [ 1, 2, 3 ] ").equals("[1,2,3]");
    }

    @Test
    void connma() {
        assert Minify.minify(" this . method ( ) ").equals("this.method()");
    }

    @Test
    void optional() {
        assert Minify.minify("this ? . property").equals("this?.property");
    }

    @Test
    void ternary() {
        assert Minify.minify("condition ? then : else").equals("condition?then:else");
    }

    @Test
    void lambda() {
        assert Minify.minify("() => {}").equals("()=>{}");
    }

    @Test
    void lambdaParameter() {
        assert Minify.minify("(a, b, c) -> a + b + c").equals("(a,b,c)->a+b+c");
    }

    @Test
    void litereal() {
        assert Minify.minify("var char = \"+ - * /\"").equals("var char=\"+ - * /\"");
        assert Minify.minify("call(\"in literal\" , param)").equals("call(\"in literal\",param)");
    }
}
