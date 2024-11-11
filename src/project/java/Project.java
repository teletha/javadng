/*
 * Copyright (C) 2024 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */

import javax.lang.model.SourceVersion;

public class Project extends bee.api.Project {

    {
        product("com.github.teletha", "javadng", ref("version.txt"));
        require(SourceVersion.latest(), SourceVersion.RELEASE_21);

        require("com.github.teletha", "sinobu");
        require("com.github.teletha", "stylist");
        require("com.github.teletha", "psychopath");
        require("com.github.teletha", "lycoris");
        require("com.github.teletha", "icymanipulator").atAnnotation();
        require("com.github.teletha", "antibug").atTest();
        require("com.github.teletha", "viewtify").atTest();
        require("com.github.javaparser", "javaparser-core");
        require("org.commonmark", "commonmark");
        require("org.commonmark", "commonmark-ext-gfm-tables");

        versionControlSystem("https://github.com/teletha/javadng");
    }
}