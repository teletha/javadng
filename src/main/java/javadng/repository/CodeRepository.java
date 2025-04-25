/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.repository;

import java.net.URI;

import javadng.Document;

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
     * Build the document for change log.
     * 
     * @param text
     * @return
     */
    public abstract Document getChangeLog(String text);

    /**
     * Build the published date for latest version.
     * 
     * @return
     */
    public abstract String getLatestPublishedDate();

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
}