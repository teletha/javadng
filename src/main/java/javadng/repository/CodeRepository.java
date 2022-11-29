/*
 * Copyright (C) 2021 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.repository;

import java.net.URI;

import kiss.I;
import kiss.JSON;

public abstract class CodeRepository {

    /**
     * Compute the repository URL.
     * 
     * @return
     */
    public abstract String locate();

    /**
     * Compute the discussion URL.
     * 
     * @return
     */
    public abstract String locateCommunity();

    /**
     * Compute the change log URL.
     * 
     * @return
     */
    public abstract String locateChangeLog();

    /**
     * Compute the issue tracking URL.
     * 
     * @return
     */
    public abstract String locateIssues();

    /**
     * Compute the editable code URL.
     * 
     * @return
     */
    public abstract String locateEditor(String file, int[] lines);

    /**
     * Build {@link CodeRepository} by URI.
     * 
     * @param uri
     * @return
     */
    public static CodeRepository of(String uri) {
        if (uri == null) {
            return null;
        }
        return of(URI.create(uri));
    }

    /**
     * Build {@link CodeRepository} by URI.
     * 
     * @param uri
     * @return
     */
    public static CodeRepository of(URI uri) {
        if (uri == null) {
            return null;
        }

        switch (uri.getHost()) {
        case "github.com":
            return new Github(uri);

        default:
            return null;
        }
    }

    /**
     * 
     */
    private static class Github extends CodeRepository {

        private final String owner;

        private final String name;

        private final String branch;

        private Github(URI uri) {
            String path = uri.getPath();
            int index = path.indexOf('/', 1);
            this.owner = path.substring(0, index);
            this.name = path.substring(index + 1);

            JSON json = I.json("https://api.github.com/repos" + path);
            this.branch = json.get(String.class, "default_branch");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String locate() {
            return "https://github.com/" + owner + "/" + name;
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
            return "https://raw.githubusercontent.com/" + owner + "/" + name + "/" + branch + "/CHANGELOG.md";
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
}
