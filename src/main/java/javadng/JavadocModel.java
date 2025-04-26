/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng;

import static javax.tools.Diagnostic.Kind.*;
import static javax.tools.Diagnostic.Kind.OTHER;
import static javax.tools.DocumentationTool.Location.*;
import static javax.tools.JavaFileObject.Kind.*;
import static javax.tools.StandardLocation.*;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticListener;
import javax.tools.DocumentationTool;
import javax.tools.DocumentationTool.DocumentationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import icy.manipulator.Icy;
import javadng.design.JavadngDesignScheme;
import javadng.javadoc.ClassInfo;
import javadng.javadoc.MethodInfo;
import javadng.javadoc.SampleInfo;
import javadng.javadoc.SourceCode;
import javadng.javadoc.TemplateStore;
import javadng.javadoc.TypeResolver;
import javadng.javadoc.Util;
import javadng.js.CodeHighlight;
import javadng.page.APIPage;
import javadng.page.ActivityPage;
import javadng.page.DocumentPage;
import javadng.repository.CodeRepository;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;
import kiss.I;
import kiss.XML;
import psychopath.Directory;
import psychopath.Locator;
import stylist.StyleDeclarable;
import stylist.Stylist;

@Icy
public abstract class JavadocModel {

    /** The default JDK API's location. */
    public static final String JDK = "https://docs.oracle.com/en/java/javase/24/docs/api/";

    /** The name pattern of document. */
    private static final Pattern DocName = Pattern.compile("(.*)Manual$");

    /** The scanned data. */
    public final Data data = new Data();

    /** The javadoc mode. */
    private boolean processingMainSource = true;

    /** The document repository. */
    private final List<ClassInfo> docs = new ArrayList();

    /** MethodID-SampleCode mapping. */
    public final Map<String, List<SampleInfo>> samples = new HashMap();

    /** PackageName-URL pair. */
    private final Map<String, String> externals = new HashMap();

    /** The internal pacakage names. */
    private final Set<String> internals = new HashSet();

    /**
     * The list of source directories.
     * 
     * @return
     */
    @Icy.Property
    public abstract List<Directory> sources();

    /**
     * The list of source directories.
     * 
     * @return
     */
    @Icy.Overload("sources")
    private List<Directory> sources(String... paths) {
        return I.signal(paths).map(Locator::directory).toList();
    }

    /**
     * The list of source directories.
     * 
     * @return
     */
    @Icy.Overload("sources")
    private List<Directory> sources(Path... paths) {
        return I.signal(paths).map(Locator::directory).toList();
    }

    /**
     * The list of source directories.
     * 
     * @return
     */
    @Icy.Property
    public List<psychopath.Location> classpath() {
        return null;
    }

    /**
     * The list of source directories.
     * 
     * @return
     */
    @Icy.Overload("classpath")
    private List<psychopath.Location> classpath(String... paths) {
        return I.signal(paths).map(Locator::locate).toList();
    }

    /**
     * The list of source directories.
     * 
     * @return
     */
    @Icy.Overload("classpath")
    private List<psychopath.Location> classpath(Path... paths) {
        return I.signal(paths).map(Locator::locate).toList();
    }

    /**
     * Specify the directory where the product is output.
     * 
     * @return
     */
    @Icy.Property(nullable = true)
    public abstract Directory output();

    /**
     * Specify the directory where the product is output.
     * 
     * @return
     */
    @Icy.Overload("output")
    private Directory output(String path) {
        return Locator.directory(path);
    }

    /**
     * Specify the directory where the product is output.
     * 
     * @return
     */
    @Icy.Overload("output")
    private Directory output(Path path) {
        return Locator.directory(path);
    }

    /**
     * The product name.
     * 
     * @return
     */
    @Icy.Property
    public abstract String product();

    @Icy.Intercept("product")
    private String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * The project name.
     * 
     * @return
     */
    @Icy.Property
    public abstract String project();

    /**
     * The product version.
     * 
     * @return
     */
    @Icy.Property
    public abstract String version();

    /**
     * The product version.
     * 
     * @return
     */
    @Icy.Property
    public String description() {
        return "";
    }

    /**
     * Specify the directory of samples.
     * 
     * @return
     */
    @Icy.Property
    public List<Directory> sample() {
        return List.of();
    }

    /**
     * Specify the directory of samples.
     * 
     * @return
     */
    @Icy.Overload("sample")
    private List<Directory> sample(String path) {
        return List.of(Locator.directory(path));
    }

    /**
     * Specify the directory of samples.
     * 
     * @return
     */
    @Icy.Overload("sample")
    private List<Directory> sample(Path path) {
        return List.of(Locator.directory(path));
    }

    /**
     * Specify the source encoding.
     * 
     * @return
     */
    @Icy.Property
    public Charset encoding() {
        return Charset.defaultCharset();
    }

    /**
     * Specify the task execution listener.
     * 
     * @return
     */
    @Icy.Property
    public DiagnosticListener<? super JavaFileObject> listener() {
        return o -> System.out.println(o);
    }

    /**
     * Specify the code repository.
     * 
     * @return
     */
    @Icy.Property
    public CodeRepository repository() {
        return null;
    }

    /**
     * Use JDK as the resolvable external document.
     * 
     * @return
     */
    public final Javadoc useExternalJDKDoc() {
        return useExternalDoc(JDK);
    }

    /**
     * Specifies the URL of the resolvable external document.
     * 
     * @param urls A list of document URL．
     * @return Chainable API.
     */
    public final Javadoc useExternalDoc(String... urls) {
        if (urls != null) {
            for (String url : urls) {
                if (url != null && url.startsWith("http") && url.endsWith("/api/")) {
                    I.http(url + "overview-tree.html", XML.class)
                            .retry(e -> e.delay(Duration.ofMillis(200)).take(20))
                            .flatIterable(xml -> xml.find(".horizontal a"))
                            .waitForTerminate()
                            .to(xml -> {
                                externals.put(xml.text(), url);
                            });
                }
            }
        }
        return (Javadoc) this;
    }

    /**
     * Generate documents.
     */
    public final Javadoc build() {
        synchronized (JavadocModel.class) {
            TemplateStore.register("product", product());
            TemplateStore.register("project", project());
            TemplateStore.register("version", version());

            Internal.model = this;

            DocumentationTool tool = ToolProvider.getSystemDocumentationTool();

            // ========================================================
            // Collect sample source
            // ========================================================
            if (!sample().isEmpty()) {
                processingMainSource = false;

                try (ToListener listener = new ToListener("sample");
                        StandardJavaFileManager m = tool.getStandardFileManager(listener(), Locale.getDefault(), encoding())) {
                    m.setLocation(SOURCE_PATH, I.signal(sources()).startWith(sample()).map(Directory::asJavaFile).toList());
                    m.setLocation(CLASS_PATH, classpath() == null ? null
                            : classpath().stream().map(psychopath.Location::asJavaFile).collect(Collectors.toList()));

                    List<JavaFileObject> files = I.signal(m.list(SOURCE_PATH, "", Set.of(SOURCE), true))
                            .take(o -> accept(o.getName()) && (o.getName().endsWith("Test.java") || o.getName().endsWith("Manual.java")))
                            .toList();

                    if (!files.isEmpty()) {
                        DocumentationTask task = tool.getTask(listener, m, listener(), Internal.class, List.of("-package"), files);

                        if (task.call()) {
                            listener().report(new Message(OTHER, "sample", "Succeed in scanning sample sources."));
                        } else {
                            listener().report(new Message(ERROR, "sample", "Fail in scanning sample sources."));
                            return (Javadoc) this;
                        }
                    }
                } catch (Throwable e) {
                    throw I.quiet(e);
                } finally {
                    processingMainSource = true;
                }
            }

            // ========================================================
            // Scan javadoc from main source
            // ========================================================
            try (ToListener listener = new ToListener("build");
                    StandardJavaFileManager m = tool.getStandardFileManager(listener(), Locale.getDefault(), encoding())) {
                m.setLocation(SOURCE_PATH, I.signal(sources()).map(Directory::asJavaFile).toList());
                m.setLocation(CLASS_PATH, classpath() == null ? null
                        : classpath().stream().map(psychopath.Location::asJavaFile).collect(Collectors.toList()));
                m.setLocationFromPaths(DOCUMENTATION_OUTPUT, List.of(output() == null ? Path.of("") : output().create().asJavaPath()));

                DocumentationTask task = tool.getTask(listener, m, listener(), Internal.class, List.of("-protected"), m
                        .list(SOURCE_PATH, "", Set.of(SOURCE), true));

                if (task.call()) {
                    listener().report(new Message(OTHER, "build", "Succeed in building documents."));
                } else {
                    listener().report(new Message(ERROR, "build", "Fail in building documents."));
                }
            } catch (Throwable e) {
                throw I.quiet(e);
            }
        }
        return (Javadoc) this;
    }

    private boolean accept(String name) {
        for (Directory directory : sample()) {
            if (name.startsWith(directory.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Build {@link Doclet} to generate documents.
     * 
     * @return
     */
    public final Class<? extends Doclet> buildDocletClass() {
        Internal.model = this;

        return Internal.class;
    }

    public final Set<ClassInfo> docs() {
        return I.signal(docs).map(ClassInfo::outermost).skipNull().toSet();
    }

    /**
     * 
     */
    private class ToListener extends Writer {

        private final String code;

        /**
         * @param code
         */
        private ToListener(String code) {
            this.code = code;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            String message = new String(cbuf, off, len).trim();
            if (message.length() != 0) {
                listener().report(new Message(Kind.NOTE, code, message));
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void flush() throws IOException {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException {
        }
    }

    /**
     * 
     */
    private static class Message implements Diagnostic<JavaFileObject> {

        private final Kind kind;

        private final String code;

        private final String message;

        private Message(Kind kind, String code, String message) {
            this.kind = kind;
            this.code = code;
            this.message = message;
        }

        @Override
        public javax.tools.Diagnostic.Kind getKind() {
            return kind;
        }

        @Override
        public JavaFileObject getSource() {
            return null;
        }

        @Override
        public long getPosition() {
            return Diagnostic.NOPOS;
        }

        @Override
        public long getStartPosition() {
            return Diagnostic.NOPOS;
        }

        @Override
        public long getEndPosition() {
            return Diagnostic.NOPOS;
        }

        @Override
        public long getLineNumber() {
            return Diagnostic.NOPOS;
        }

        @Override
        public long getColumnNumber() {
            return Diagnostic.NOPOS;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getMessage(Locale locale) {
            return message;
        }

        @Override
        public String toString() {
            return kind + ":" + code + ": " + message;
        }
    }

    /**
     * <h>DONT USE THIS CLASS</h>
     * <p>
     * It is a Doclet for internal use, but it is public because it cannot be made private due to
     * the specifications of the documentation tool.
     * </p>
     */
    public static class Internal implements Doclet {

        /** The setting model. */
        private static JavadocModel model;

        /**
         * {@inheritDoc}
         */
        @Override
        public final void init(Locale locale, Reporter reporter) {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean run(DocletEnvironment env) {
            Util.DocUtils = env.getDocTrees();
            Util.ElementUtils = env.getElementUtils();
            Util.TypeUtils = env.getTypeUtils();
            Util.Samples = model.sample();

            try {
                model.initialize();

                for (Element element : env.getSpecifiedElements()) {
                    switch (element.getKind()) {
                    case MODULE:
                        model.process((ModuleElement) element);
                        break;

                    case PACKAGE:
                        model.process((PackageElement) element);
                        break;

                    default:
                        model.process((TypeElement) element);
                        break;
                    }
                }
            } finally {
                model.complete();
            }
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final String getName() {
            return getClass().getSimpleName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final Set<? extends Option> getSupportedOptions() {
            return Set.of();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final SourceVersion getSupportedSourceVersion() {
            return SourceVersion.latest();
        }
    }

    /**
     * Initialization phase.
     */
    private void initialize() {
        // Find all package names in the source directory.
        I.signal(sources()).flatMap(Directory::walkDirectoryWithBase).to(sub -> {
            internals.add(sub.ⅰ.relativize(sub.ⅱ).toString().replace(File.separatorChar, '.'));
        });
    }

    /**
     * Process a class or interface program element. Provides access to information about the type
     * and its members. Note that an enum type is a kind of class and an annotation type is a kind
     * of interface.
     * 
     * @param root A class or interface program element root.
     */
    private void process(TypeElement root) {
        ClassInfo info = new ClassInfo(root, new TypeResolver(externals, internals, root));

        if (processingMainSource) {
            data.add(info);
        } else {
            Matcher matcher = DocName.matcher(info.outer().map(o -> o.name).or(""));

            if (matcher.matches() && info.isPublic()) {
                docs.add(0, info);
            } else {
                for (MethodInfo method : info.methods()) {
                    if (!method.getSeeTags().isEmpty()) {
                        String code = SourceCode.read(method);
                        for (XML see : method.getSeeTags()) {
                            String[] id = info.identify(see.text());
                            SampleInfo sample = new SampleInfo(id[0], id[1], code);
                            sample.comment.set(method.contents());

                            samples.computeIfAbsent(sample.id(), x -> new ArrayList()).add(sample);
                        }
                    }
                }
            }
        }
    }

    /**
     * Process a package program element. Provides access to information about the package and its
     * members.
     * 
     * @param root A package program element root.
     */
    private void process(PackageElement root) {
    }

    /**
     * Process a module program element. Provides access to information about the module, its
     * directives, and its members.
     * 
     * @param root A module program element root.
     */
    private void process(ModuleElement root) {
    }

    /**
     * Completion phase.
     */
    private void complete() {
        if (processingMainSource) {
            // sort data
            data.modules.sort(Comparator.naturalOrder());
            data.packages.sort(Comparator.naturalOrder());
            data.types.sort(Comparator.naturalOrder());

            // after care
            data.connectSubType();

            // build doc tree
            for (ClassInfo info : docs) {
                Doc doc = new Doc();
                doc.title = info.title();
                doc.path = "doc/" + info.id() + ".html";
                data.docs.add(doc);

                for (Document child : info.children()) {
                    Doc childDoc = new Doc();
                    childDoc.title = child.title();
                    childDoc.path = "doc/" + info.id() + ".html#" + child.id();
                    doc.subs.add(childDoc);

                    for (Document foot : child.children()) {
                        Doc footDoc = new Doc();
                        footDoc.title = foot.title();
                        footDoc.path = "doc/" + info.id() + ".html#" + foot.id();
                        childDoc.subs.add(footDoc);
                    }
                }
            }

            if (output() != null) {
                SiteBuilder site = SiteBuilder.root(output()).guard("index.html", "main.css", "mocha.html", "mimic.test.js");

                // build CSS
                I.load(SiteBuilder.class);
                Stylist.pretty()
                        .scheme(JavadngDesignScheme.class)
                        .styles(I.findAs(StyleDeclarable.class))
                        .formatTo(output().file("main.css").asJavaPath());

                // build JS
                site.build("main.js", SiteBuilder.class.getResourceAsStream("main.js"));
                site.build("mimic.js", SiteBuilder.class.getResourceAsStream("mimic.js"));
                site.build("highlight.js", SiteBuilder.class.getResourceAsStream("highlight.js"), CodeHighlight.build());

                // build SVG
                site.build("main.svg", SiteBuilder.class.getResourceAsStream("main.svg"));

                // build HTML
                for (ClassInfo info : data.types) {
                    site.buildHTML("api/" + info.id() + ".html", new APIPage(1, this, info));
                }
                for (ClassInfo info : docs) {
                    site.buildHTML("doc/" + info.id() + ".html", new DocumentPage(1, this, info));
                }

                // build change log
                I.http(repository().locateChangeLog(), String.class).waitForTerminate().skipError().to(md -> {
                    site.buildHTML("doc/changelog.html", new ActivityPage(1, this, repository().getChangeLog(md)));
                });

                // create at last for live reload
                site.buildHTML("index.html", new APIPage(0, this, null));
            }
        }
    }
}