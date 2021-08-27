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
        assert Minify.minify("a == b").equals("a==b");
        assert Minify.minify("a === b").equals("a===b");
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
    void operator() {
        assert Minify.minify("a & b").equals("a&b");
        assert Minify.minify("a | b").equals("a|b");
        assert Minify.minify("a ^ b").equals("a^b");
        assert Minify.minify("a << b").equals("a<<b");
        assert Minify.minify("a >> b").equals("a>>b");
        assert Minify.minify("a >>> b").equals("a>>>b");
        assert Minify.minify("~ a").equals("~a");
        assert Minify.minify("! a").equals("!a");
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
    void tailedSlash() {
        assert Minify.minify("""
                1 /
                2
                """).equals("1/2");
    }

    @Test
    void literealSingleQuotes() {
        assert Minify.minify("call( ' a + b ' )").equals("call(' a + b ')");
        assert Minify.minify("call( ' \\' ' )").equals("call(' \\' ')");
        assert Minify.minify("call( ' \" \" ' )").equals("call(' \" \" ')");
    }

    @Test
    void literealSingleQuotesWithLine() {
        assert Minify.minify("""
                'tailed semi-colon ;
                 tailed bracket {
                 never delete line'
                """).equals("""
                'tailed semi-colon ;
                 tailed bracket {
                 never delete line'""");
    }

    @Test
    void literealDoubleQuotes() {
        assert Minify.minify("call( \" a + b \" )").equals("call(\" a + b \")");
        assert Minify.minify("call( \" \\\" \" )").equals("call(\" \\\" \")");
        assert Minify.minify("call( \" ' ' \" )").equals("call(\" ' ' \")");
    }

    @Test
    void literealDoubleQuotesWithLine() {
        assert Minify.minify("""
                "tailed semi-colon ;
                 tailed bracket {
                 never delete line"
                """).equals("""
                "tailed semi-colon ;
                 tailed bracket {
                 never delete line"
                """.trim());
    }

    @Test
    void lineComment() {
        assert Minify.minify("// comment").equals("");
        assert Minify.minify("let a = 0; // comment").equals("let a=0;");
        assert Minify.minify("""
                // comment
                let a = 0;
                """).equals("let a=0;");
    }

    @Test
    void blockComment() {
        assert Minify.minify("/* comment */").equals("");
        assert Minify.minify("call(/* comment */)").equals("call()");
        assert Minify.minify("/** comment */").equals("");
        assert Minify.minify("""
                /**
                  * multi line comment
                  */
                """).equals("");
    }

    @Test
    void emptyLine() {
        assert Minify.minify("""

                method()

                """).equals("method()");
    }

    @Test
    void commentOnlyLine() {
        assert Minify.minify("""
                // call method!
                method()

                """).equals("method()");

        assert Minify.minify("""
                /**
                 * Comment
                 */
                method()

                """).equals("method()");

    }

    @Test
    void compressLineByComma() {
        assert Minify.minify("""
                a,
                b
                """).equals("a,b");

        assert Minify.minify("""
                a,

                /* comment */
                b
                """).equals("a,b");
    }
}
