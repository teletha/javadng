/*
 * Copyright (C) 2024 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package javadng.writer;

public abstract class Document {

    /** The text buffer. */
    private final StringBuilder builder = new StringBuilder();

    protected final void p(CharSequence... fragments) {
        for (CharSequence frag : fragments) {
            builder.append(frag);
        }
    }

    protected final CharSequence a(CharSequence text, CharSequence link) {
        return "<a href=\"" + link + "\">" + text + "</a>";
    }

}
