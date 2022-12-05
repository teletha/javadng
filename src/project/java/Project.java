import javax.lang.model.SourceVersion;

/*
 * Copyright (C) 2020 stoneforge Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
public class Project extends bee.api.Project {

    {
        product("com.github.teletha", "javadng", ref("version.txt"));

        require(SourceVersion.RELEASE_19, SourceVersion.RELEASE_11);

        require("com.github.teletha", "sinobu");
        require("com.github.teletha", "stylist");
        require("com.github.teletha", "psychopath");
        require("com.github.teletha", "icymanipulator").atAnnotation();
        require("com.github.teletha", "antibug").atTest();
        require("com.github.teletha", "viewtify").atTest();
        require("com.github.javaparser", "javaparser-core");
        require("org.commonmark", "commonmark");

        versionControlSystem("https://github.com/teletha/javadng");
    }
}