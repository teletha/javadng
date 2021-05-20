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

public class Github extends Repository {

    private String owner;

    private String repository;

    private String branch;

    public Github(String owner, String repository, String branch) {
        this.owner = owner;
        this.repository = repository;
        this.branch = branch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String locate() {
        return "https://github.com/" + owner + "/" + repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String locateCommunity() {
        return locate() + "/discussions";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String locateChangeLog() {
        return "https://raw.githubusercontent.com/" + owner + "/" + repository + "/" + branch + "/CHANGELOG.md";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String locateIssues() {
        return locate() + "/issues";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String locateEditor(String file, int[] lines) {
        return locate() + "/edit/" + branch + "/src/test/java/" + file + "#L" + lines[0] + "-L" + lines[1];
    }

}
