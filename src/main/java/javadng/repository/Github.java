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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javadng.Document;
import javadng.Region;
import kiss.I;
import kiss.JSON;
import kiss.XML;

/**
 * 
 */
class Github extends CodeRepository {

    private final String owner;

    private final String name;

    private final String branch;

    private String published;

    Github(URI uri) {
        String path = uri.getPath();
        int index = path.indexOf('/', 1);
        this.owner = path.substring(1, index);
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
    public String locateEditor(Region region) {
        return locate() + "/edit/" + branch + "/src/test/java/" + region.location() + "#L" + region.startLine() + "-L" + region.endLine();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document getChangeLog(String text) {
        // Parse markdown and convert to structured HTML
        Node root = Parser.builder().build().parse(text);
        String html = HtmlRenderer.builder().escapeHtml(true).build().render(root);

        // convert h3 with bugfix to h2
        html = html.replaceAll("<h3>(.+\\(.+)</h3>", "<h2>$1</h2>");

        // structure flat HTML
        StringBuilder b = new StringBuilder();
        int[] ranks = {0, 0, 0, 0, 0, 0};
        Matcher matcher = Pattern.compile("<h(\\d)>").matcher(html);
        while (matcher.find()) {
            int rank = Integer.parseInt(matcher.group(1));
            int count = ranks[rank]++;
            int nest = 0;

            // reset lower rank's count
            for (int i = rank + 1; i < ranks.length; i++) {
                if (ranks[i] != 0) {
                    nest++;
                    ranks[i] = 0;
                }
            }

            matcher.appendReplacement(b, count == 0 ? "<section><h" + rank + ">"
                    : "</section>".repeat(nest + 1) + "<section><h" + rank + ">");
        }
        matcher.appendTail(b);
        b.append("</section></section></section>");

        return new ChangeLogProvider(I.xml(b.toString()), 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized String getLatestPublishedDate() {
        if (published == null) {
            String date = I.http("https://github.com/" + owner + "/" + name + "/releases/latest", XML.class)
                    .waitForTerminate()
                    .map(html -> html.find(".markdown-body h2").first().text())
                    .to()
                    .or(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

            int start = date.indexOf('(');
            int end = date.lastIndexOf(')');
            published = start == -1 ? date : date.substring(start + 1, end);
        }
        return published;
    }

    /**
     * Document for change log.
     */
    private class ChangeLogProvider implements Document {

        private final List<Document> children = new ArrayList();

        private final String title;

        private final int nest;

        private final XML doc;

        /**
         * Build the document for change log from markdown in Github.
         */
        private ChangeLogProvider(XML xml, int nest) {
            this.nest = nest;
            this.title = xml.find(">h" + nest).text();
            this.doc = xml.find(">ul)");
            xml.find(">section").forEach(sec -> {
                children.add(new ChangeLogProvider(sec, nest + 1));
            });

            if (nest == 2) {
                children.add(new AssetProvider(title.substring(0, title.indexOf(" "))));
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String id() {
            return title.replaceAll("\\s", "");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String title() {
            return title;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasContents() {
            return 2 <= nest;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML contents() {
            return doc;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<? extends Document> children() {
            return children;
        }
    }

    /**
     * 
     */
    private class AssetProvider implements Document {

        private final String version;

        private AssetProvider(String version) {
            this.version = version;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String id() {
            return "Asserts";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String title() {
            return "Assets";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML contents() {
            return I.xml("<ul><li><a href=\"https://github.com/" + owner + "/" + name + "/archive/refs/tags/v" + version + ".zip\">Source code</a> (zip)</li></ul>");
        }
    }
}