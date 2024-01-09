/*
 * Copyright (C) 2023 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.writer;

import kiss.Extensible;

public abstract class Writable implements Extensible {

    protected void p(String text) {

    }

    public void page() {
        p("""
                # Purpose of Use
                This library aims to simplify and highly condense the functions related to domains that
                are frequently encountered in real-world development projects, making them easier to use.
                Some specific domains are listed below.

                - [Dependency Injection](https://en.wikipedia.org/wiki/Dependency_injection)
                - Object lifecycle management
                - [JavaBeans](https://en.wikipedia.org/wiki/JavaBeans)-like property based type modeling
                - HTTP(S)
                - [JSON](https://en.wikipedia.org/wiki/JSON)
                - [HTML](https://en.wikipedia.org/wiki/HTML)([XML](https://en.wikipedia.org/wiki/XML))
                - Reactive Programming ([Rx](http://reactivex.io/))
                - Asynchronous processing
                - Parallel processing
                - Multilingualization
                - Template  engine([Mustache](https://mustache.github.io/mustache.5.html))
                - Dynamic plug-in mechanism
                - Object Persistence
                - Logging

                With a few exceptions, Sinobu and its APIs are designed to be simple to use and easy to
                understand by adhering to the following principles.

                - [Keep it stupid simple](https://en.wikipedia.org/wiki/KISS_principle)
                - [Less is more](https://en.wikipedia.org/wiki/Less_is_more_(architecture))
                - [Type safety](https://en.wikipedia.org/wiki/Type_safety)
                - Refactoring safety

                # How to install
                It is probably easiest to use a build tool such as [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/).
                ```xml
                <dependency>
                    <groupId>{{project}}</groupId>
                    <artifactId>{{product}}</artifactId>
                    <version>{{version}}<version>
                </dependency>
                ```
                """);
    }
}
