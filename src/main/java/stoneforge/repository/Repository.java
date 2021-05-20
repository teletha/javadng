/*
 * Copyright (C) 2019 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package stoneforge.repository;

public abstract class Repository {

    public abstract String locate();

    public abstract String locateCommunity();

    public abstract String locateChangeLog();

    public abstract String locateIssues();

    public abstract String locateEditor(String file, int[] lines);
}
