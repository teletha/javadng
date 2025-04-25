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

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;

import kiss.I;
import psychopath.Directory;

public class Launcher {

    /**
     * Show the generated document in your browser.
     */
    public final static void launch(Directory output) {
        try {
            psychopath.File checker = output.file("index.html");
            long[] modified = {checker.lastModifiedMilli()};
            String prefix = "/application/";

            HttpServer server = HttpServer.create(new InetSocketAddress(9321), 0);
            server.createContext("/live", context -> {
                long time = checker.lastModifiedMilli();
                if (modified[0] < time) {
                    modified[0] = time;
                    context.sendResponseHeaders(200, 1);
                    I.copy(new ByteArrayInputStream("0".getBytes()), context.getResponseBody(), true);
                } else {
                    context.sendResponseHeaders(204, -1);
                }
            });
            server.createContext(prefix, context -> {
                psychopath.File file = output.file(context.getRequestURI().getPath().substring(prefix.length()));
                byte[] body = file.text().getBytes(StandardCharsets.UTF_8);

                Headers headers = context.getResponseHeaders();
                headers.set("Content-Type", mime(file));
                context.sendResponseHeaders(200, body.length);
                I.copy(new ByteArrayInputStream(body), context.getResponseBody(), true);
            });
            server.start();

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("http://localhost:9321" + prefix + "index.html"));
                } catch (Exception e) {
                    throw I.quiet(e);
                }
            }
        } catch (BindException e) {
            // already launched
        } catch (Exception e) {
            throw I.quiet(e);
        }
    }

    /**
     * Detect mime-type.
     * 
     * @param file
     * @return
     */
    private static String mime(psychopath.File file) {
        switch (file.extension()) {
        case "css":
            return "text/css";
        case "js":
            return "application/javascript";
        case "html":
            return "text/html";
        case "svg":
            return "image/svg+xml";
        default:
            return "text/plain";
        }
    }
}
