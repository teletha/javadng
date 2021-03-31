/*
 * Copyright (C) 2020 stoneforge Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package stoneforge.javadoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentUser {

    public static void main(String[] args) throws IOException {
        List<String> path = new ArrayList();
        path.add("F:/Application/Maven Repository/com/fasterxml/jackson/core/jackson-core/2.12.2/jackson-core-2.12.2.jar");
        path.add("F:/Application/Maven Repository/com/fasterxml/jackson/core/jackson-databind/2.12.2/jackson-databind-2.12.2.jar");
        path.add("F:/Application/Maven Repository/com/alibaba/fastjson/1.2.75/fastjson-1.2.75.jar");
        path.add("F:/Application/Maven Repository/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar");
        path.add("F:/Application/Maven Repository/com/pgs-soft/HttpClientMock/1.0.0/HttpClientMock-1.0.0.jar");
        path.add("F:/Application/Maven Repository/net/bytebuddy/byte-buddy/1.10.22/byte-buddy-1.10.22.jar");
        path.add("F:/Application/Maven Repository/org/junit/jupiter/junit-jupiter-api/5.7.1/junit-jupiter-api-5.7.1.jar");
        path.add("F:/Application/Maven Repository/org/apiguardian/apiguardian-api/1.1.0/apiguardian-api-1.1.0.jar");
        path.add("F:/Application/Maven Repository/com/github/teletha/antibug/1.0.3/antibug-1.0.3.jar");

        Javadoc.with.sources("../sinobu/src/main/java")
                .output("docs/api")
                .product("Sinobu")
                .project("Sinobu")
                .version("1.0")
                .sample("../sinobu/src/test/java")
                .classpath(path.toArray(String[]::new))
                .useExternalJDKDoc()
                .build()
                .show();
    }
}