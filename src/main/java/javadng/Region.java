/*
 * Copyright (C) 2025 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng;

/**
 * Represents a specific region within a document, identified by a location (e.g., file path or URL)
 * and an optional line range.
 * <p>
 * This class is typically used to refer to sections of source files or other line-based documents.
 * If line numbers are unknown or not applicable, they will default to -1.
 *
 * @param location the document location (e.g., file path or URL)
 * @param startLine the starting line number of the region (inclusive), or -1 if unspecified
 * @param endLine the ending line number of the region (inclusive), or -1 if unspecified
 */
public record Region(String location, int startLine, int endLine) {

    /**
     * Creates a {@code Region} with only a location.
     * Line numbers will default to -1, indicating that they are unspecified.
     *
     * @param location the document location (e.g., file path or URL)
     */
    public Region(String location) {
        this(location, -1, -1);
    }
}
