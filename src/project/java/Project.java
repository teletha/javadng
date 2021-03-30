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
        product("com.github.teletha", "stoneforge", ref("version.txt"));

        require("com.github.teletha", "sinobu");
        require("com.github.teletha", "stylist");
        require("com.github.teletha", "psychopath");
        require("com.github.teletha", "icymanipulator").atAnnotation();
        require("com.github.teletha", "antibug").atTest();

        versionControlSystem("https://github.com/teletha/stoneforge");
    }
}