/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.javadoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleTypeVisitor9;

import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.w3c.dom.Node;

import com.sun.source.doctree.AttributeTree;
import com.sun.source.doctree.AuthorTree;
import com.sun.source.doctree.CommentTree;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocRootTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.DocTypeTree;
import com.sun.source.doctree.EndElementTree;
import com.sun.source.doctree.EntityTree;
import com.sun.source.doctree.ErroneousTree;
import com.sun.source.doctree.IdentifierTree;
import com.sun.source.doctree.IndexTree;
import com.sun.source.doctree.InheritDocTree;
import com.sun.source.doctree.LinkTree;
import com.sun.source.doctree.LiteralTree;
import com.sun.source.doctree.ParamTree;
import com.sun.source.doctree.ReferenceTree;
import com.sun.source.doctree.ReturnTree;
import com.sun.source.doctree.SeeTree;
import com.sun.source.doctree.SinceTree;
import com.sun.source.doctree.SnippetTree;
import com.sun.source.doctree.StartElementTree;
import com.sun.source.doctree.SummaryTree;
import com.sun.source.doctree.TextTree;
import com.sun.source.doctree.ThrowsTree;
import com.sun.source.doctree.UnknownBlockTagTree;
import com.sun.source.doctree.UnknownInlineTagTree;
import com.sun.source.doctree.ValueTree;
import com.sun.source.doctree.VersionTree;
import com.sun.source.util.SimpleDocTreeVisitor;

import javadng.design.Styles;
import javadng.js.CodeHighlight;
import kiss.I;
import kiss.Variable;
import kiss.XML;
import kiss.Ⅱ;

public class DocumentInfo {

    private static final Pattern CLASS_ASSIGN = Pattern.compile("^\\[!([A-Z]+)\\]\\R+");

    /** The associated element. */
    public final Element e;

    protected final Variable<XML> comment = Variable.empty();

    /** Tag info. */
    protected final List<Ⅱ<String, XML>> typeParameterTags = new ArrayList();

    /** Tag info. */
    protected final List<Ⅱ<String, XML>> paramTags = new ArrayList();

    /** Tag info. */
    protected final List<Ⅱ<String, XML>> throwsTags = new ArrayList();

    /** Tag info. */
    protected final List<XML> authorTags = new ArrayList();

    /** Tag info. */
    protected final List<XML> seeTags = new ArrayList();

    /** Tag info. */
    protected final List<XML> sinceTags = new ArrayList();

    /** Tag info. */
    protected final List<XML> versionTags = new ArrayList();

    /** Tag info. */
    protected final Variable<XML> returnTag = Variable.empty();

    /** Tag info. */
    protected final TemplateStore templateTags;

    /** The type resolver. */
    protected final TypeResolver resolver;

    protected int[] documentLines = {-1, -1};

    private final Parser markParser = Parser.builder().extensions(List.of(TablesExtension.create())).build();

    private final HtmlRenderer htmlRenderer = HtmlRenderer.builder().extensions(List.of(TablesExtension.create())).build();

    protected DocumentInfo(Element e, TypeResolver resolver, DocumentInfo parent) {
        this.e = e;
        this.resolver = resolver;
        this.templateTags = new TemplateStore(parent == null ? null : parent.templateTags);

        try {
            DocCommentTree docs = Util.DocUtils.getDocCommentTree(e);
            if (docs != null) {
                comment.set(transform(xml(docs.getFullBody())));
                comment.to(x -> x.addClass(Styles.JavadocComment.className()));
                docs.getBlockTags().forEach(tag -> tag.accept(new TagScanner(), this));

                documentLines = Util.getDocumentLineNumbers(e);
            }
        } catch (Throwable error) {
            error.printStackTrace();
        }
    }

    private XML transform(XML xml) {
        if (xml != null) {
            for (XML e : xml.find("p, blockquote")) {
                Node child = e.to().getFirstChild();
                if (child != null && child.getNodeType() == Node.TEXT_NODE) {
                    String text = child.getTextContent();
                    Matcher matcher = CLASS_ASSIGN.matcher(text);
                    if (matcher.find()) {
                        e.addClass(matcher.group(1));
                        child.setTextContent(matcher.replaceFirst(""));
                    }
                }
            }
        }
        return xml;
    }

    /**
     * Get the typeParameterTags property of this {@link DocumentInfo}.
     * 
     * @return The typeParameterTags property.
     */
    public final List<Ⅱ<String, XML>> getTypeParameterTags() {
        return typeParameterTags;
    }

    /**
     * Get the paramTags property of this {@link DocumentInfo}.
     * 
     * @return The paramTags property.
     */
    public final List<Ⅱ<String, XML>> getParamTags() {
        return paramTags;
    }

    /**
     * Get the throwsTags property of this {@link DocumentInfo}.
     * 
     * @return The throwsTags property.
     */
    public final List<Ⅱ<String, XML>> getThrowsTags() {
        return throwsTags;
    }

    /**
     * Get the authorTags property of this {@link DocumentInfo}.
     * 
     * @return The authorTags property.
     */
    public final List<XML> getAuthorTags() {
        return authorTags;
    }

    /**
     * Get the seeTags property of this {@link DocumentInfo}.
     * 
     * @return The seeTags property.
     */
    public final List<XML> getSeeTags() {
        return seeTags;
    }

    /**
     * Get the sinceTags property of this {@link DocumentInfo}.
     * 
     * @return The sinceTags property.
     */
    public final List<XML> getSinceTags() {
        return sinceTags;
    }

    /**
     * Get the versionTags property of this {@link DocumentInfo}.
     * 
     * @return The versionTags property.
     */
    public final List<XML> getVersionTags() {
        return versionTags;
    }

    /**
     * Get the returnTag property of this {@link DocumentInfo}.
     * 
     * @return The returnTag property.
     */
    public final Variable<XML> getReturnTag() {
        return returnTag;
    }

    /**
     * Get the templateTags property of this {@link DocumentInfo}.
     * 
     * @return The templateTags property.
     */
    public final TemplateStore getTemplateTags() {
        return templateTags;
    }

    /**
     * Get the resolver property of this {@link DocumentInfo}.
     * 
     * @return The resolver property.
     */
    public final TypeResolver getResolver() {
        return resolver;
    }

    /**
     * Create comment element.
     * 
     * @return
     */
    public final XML contents() {
        return comment.isAbsent() ? null : comment.v.clone();
    }

    /**
     * Get the line positions of documentation comments for this element.
     * 
     * @return
     */
    public final int[] line() {
        return documentLines;
    }

    /**
     * Determines if this element has a documentation comment.
     * 
     * @return
     */
    public final boolean hasContents() {
        return comment.isPresent();
    }

    /**
     * Parse {@link TypeMirror} and build its XML expression.
     * 
     * @param type A target type.
     * @return New XML expression.
     */
    protected final XML parseTypeAsXML(TypeMirror type) {
        return new TypeXMLBuilder().parse(type).parent().children();
    }

    /**
     * Test visibility of the specified {@link Element}.
     * 
     * @param e
     * @return
     */
    protected final boolean isVisible(Element e, ClassInfo clazz) {
        Set<Modifier> modifiers = e.getModifiers();
        if (clazz.isTest()) {
            return !modifiers.contains(Modifier.PRIVATE);
        } else {
            return modifiers.contains(Modifier.PUBLIC) || modifiers.contains(Modifier.PROTECTED);
        }
    }

    /**
     * Find comment.
     * 
     * @param name
     * @return
     */
    protected final XML findParamTagBy(String name) {
        for (Ⅱ<String, XML> param : paramTags) {
            if (param.ⅰ.equals(name)) {
                return param.ⅱ;
            }
        }
        return null;
    }

    /**
     * Find comment.
     * 
     * @param name
     * @return
     */
    protected final XML findThrowsTagBy(String name) {
        for (Ⅱ<String, XML> param : throwsTags) {
            if (param.ⅰ.equals(name)) {
                return param.ⅱ;
            }
        }
        return null;
    }

    /**
     * Find comment.
     * 
     * @param name
     * @return
     */
    protected final XML findTypeVariableTagBy(String name) {
        for (Ⅱ<String, XML> param : typeParameterTags) {
            if (param.ⅰ.equals(name)) {
                return param.ⅱ;
            }
        }
        return null;
    }

    /**
     * @param docs Documents.
     * @return
     */
    private XML xml(List<? extends DocTree> docs) {
        return new DocumentXMLBuilder().parse(docs).build();
    }

    /**
     * Create empty node.
     * 
     * @return
     */
    private XML emptyXML() {
        return null;
    }

    /**
     * Get the class and method ID from the specified link-like text. (i.e. Class#getName(),
     * #method(int, String) etc)
     * 
     * @param linkLike
     * @return
     */
    public final String[] identify(String linkLike) {
        int index = linkLike.indexOf("#");
        if (index == -1) {
            String fqcn = resolver.resolveFQCN(linkLike);

            // If it refers to a type that has not yet been processed in the same package as the
            // type being processed, the FQCN cannot be resolved properly and should be
            // resolved separately.
            if (fqcn.equals(linkLike) && linkLike.indexOf(".") == -1) {
                fqcn = Util.ElementUtils.getPackageOf(Util.getTopLevelTypeElement(e)).getQualifiedName() + "." + linkLike;
            }
            return new String[] {fqcn, null};
        } else if (index == 0) {
            return new String[] {resolver.resolveFQCN(Util.getTopLevelTypeElement(e).toString()), qualify(linkLike.substring(1))};
        } else {
            String type = linkLike.substring(0, index);
            String member = linkLike.substring(index + 1);
            String fqcn = resolver.resolveFQCN(type);

            // If it refers to a type that has not yet been processed in the same package as the
            // type being processed, the FQCN cannot be resolved properly and should be
            // resolved separately.
            if (fqcn.equals(type) && type.indexOf(".") == -1) {
                fqcn = Util.ElementUtils.getPackageOf(Util.getTopLevelTypeElement(e)).getQualifiedName() + "." + type;
            }
            return new String[] {fqcn, qualify(member)};
        }
    }

    private String qualify(String text) {
        int start = text.indexOf('(');
        if (start == -1) {
            // field
            return text;
        } else {
            // method or constructor
            StringJoiner join = new StringJoiner(",", text.substring(0, start) + "(", ")");
            int end = text.indexOf(')', start);
            for (String param : text.substring(start + 1, end).split(",")) {
                join.add(resolver.resolveFQCN(param.trim()));
            }
            return join.toString();
        }
    }

    /**
     * 
     */
    private class TagScanner extends SimpleDocTreeVisitor<DocumentInfo, DocumentInfo> {

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentInfo visitAuthor(AuthorTree node, DocumentInfo p) {
            authorTags.add(xml(node.getName()));
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentInfo visitParam(ParamTree node, DocumentInfo p) {
            Ⅱ<String, XML> pair = I.pair(node.getName().toString(), xml(node.getDescription()));

            if (node.isTypeParameter()) {
                typeParameterTags.add(pair);
            } else {
                paramTags.add(pair);
            }
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentInfo visitReturn(ReturnTree node, DocumentInfo p) {
            returnTag.set(xml(node.getDescription()));
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentInfo visitSee(SeeTree node, DocumentInfo p) {
            seeTags.add(xml(node.getReference()));
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentInfo visitSince(SinceTree node, DocumentInfo p) {
            sinceTags.add(xml(node.getBody()));
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentInfo visitThrows(ThrowsTree node, DocumentInfo p) {
            throwsTags.add(I.pair(node.getExceptionName().toString(), xml(node.getDescription())));
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentInfo visitVersion(VersionTree node, DocumentInfo p) {
            versionTags.add(xml(node.getBody()));
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentInfo visitUnknownBlockTag(UnknownBlockTagTree node, DocumentInfo p) {
            templateTags.put(node.getTagName(), I.signal(node.getContent()).map(DocTree::toString).scan(Collectors.joining()).to().exact());
            return p;
        }
    }

    /**
     * 
     */
    private class DocumentXMLBuilder extends SimpleDocTreeVisitor<DocumentXMLBuilder, DocumentXMLBuilder> {

        private StringBuilder text = new StringBuilder();

        /**
         * Parse documetation.
         * 
         * @param docs
         * @return
         */
        private DocumentXMLBuilder parse(List<? extends DocTree> docs) {
            for (DocTree doc : docs) {
                doc.accept(this, this);
            }
            return this;
        }

        /**
         * Build XML fragmentation.
         * 
         * @return
         */
        private XML build() {
            try {
                if (text.length() == 0) {
                    return emptyXML();
                } else {
                    String built = text.toString();

                    if (built.charAt(0) != '<') {
                        built = htmlRenderer.render(markParser.parse(concatLineSeparation(built)));
                    }

                    // Since Javadoc text is rarely correct HTML, switch by inserting dock type
                    // declarations to use the tag soup parser instead of the XML parser.
                    built = "<!DOCTYPE span><span>" + built + "</span>";

                    return I.xml(built);
                }
            } catch (Exception e) {
                throw new Error(e.getMessage() + " [" + text.toString() + "]", e);
            }
        }

        private String concatLineSeparation(String input) {
            if (input == null || input.isEmpty()) return "";

            List<String> result = new ArrayList();
            String[] lines = input.split("\\R");
            StringBuilder buffer = new StringBuilder();

            boolean accumulating = false;

            for (String line : lines) {
                if (line.startsWith(" |") && !line.endsWith("|")) {
                    if (!accumulating) {
                        buffer.setLength(0);
                        buffer.append(line);
                        accumulating = true;
                    } else {
                        buffer.append(" ").append(line);
                    }
                } else if (accumulating) {
                    buffer.append(" ").append(line);

                    if (line.endsWith("|")) {
                        result.add(buffer.toString());
                        accumulating = false;
                    }
                } else {
                    result.add(line);
                }
            }

            if (accumulating) {
                result.add(buffer.toString());
            }
            return String.join("\r\n", result);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitAttribute(AttributeTree node, DocumentXMLBuilder p) {
            if (node.getName().contentEquals("class")) {
                for (String lang : node.getValue().toString().split(" ")) {
                    if (lang.startsWith("lang-")) {
                        CodeHighlight.addLanguage(lang.substring(5));
                    } else if (lang.startsWith("language-")) {
                        CodeHighlight.addLanguage(lang.substring(9));
                    }
                }
            }
            text.append(' ').append(node.getName()).append("=\"");
            node.getValue().forEach(n -> n.accept(this, this));
            text.append("\"");
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitComment(CommentTree node, DocumentXMLBuilder p) {
            return super.visitComment(node, p);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitDocRoot(DocRootTree node, DocumentXMLBuilder p) {
            return super.visitDocRoot(node, p);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitDocType(DocTypeTree node, DocumentXMLBuilder p) {
            return super.visitDocType(node, p);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitEndElement(EndElementTree node, DocumentXMLBuilder p) {
            text.append("</").append(node.getName()).append('>');
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitEntity(EntityTree node, DocumentXMLBuilder p) {
            text.append(node);
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitErroneous(ErroneousTree node, DocumentXMLBuilder p) {
            return super.visitErroneous(node, p);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitIdentifier(IdentifierTree node, DocumentXMLBuilder p) {
            return super.visitIdentifier(node, p);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitIndex(IndexTree node, DocumentXMLBuilder p) {
            return super.visitIndex(node, p);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitInheritDoc(InheritDocTree node, DocumentXMLBuilder p) {
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitLink(LinkTree node, DocumentXMLBuilder p) {
            String ref = node.getReference().toString();
            String[] id = identify(node.getReference().toString());
            boolean external = resolver.isExternal(id[0]);
            String label = I.signal(node.getLabel()).as(TextTree.class).map(TextTree::getBody).to().or(ref);
            boolean code = label.contains("@");
            boolean plain = node.getTagName().equals("linkplain");

            if (code) {
                writeSourceCode(SourceCode.read(id[0], id[1], plain), "java");
            } else {
                String uri = resolver.resolveDocumentLocation(id[0]);

                if (uri == null) {
                    text.append(ref);
                } else {
                    boolean apiLing = label.equals(ref);

                    if (apiLing) text.append("<code>");
                    text.append("<a href='").append(uri);
                    if (id[1] != null) text.append("#").append(id[1]);
                    text.append("' aria-label='").append(id[0]).append(external ? " 🚀" : "").append("'>").append(label).append("</a>");
                    if (apiLing) text.append("</code>");
                }
            }

            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitLiteral(LiteralTree node, DocumentXMLBuilder p) {
            String body = node.getBody().getBody();
            boolean isSourceCode = body.endsWith("@");
            if (isSourceCode) body = body.substring(0, body.length() - 1).trim();

            if (isSourceCode) {
                writeSourceCode(body, "");
            } else {
                text.append("<code>").append(resolve(body)).append("</code>");
            }

            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected DocumentXMLBuilder defaultAction(DocTree node, DocumentXMLBuilder p) {
            // Since Markdown is supported from Java23, processing is performed with defaultAction
            // to support execution environments lower than Java23.
            // In addition, since the RawTextTree type and Kind.MARKDOWN field cannot be used, other
            // methods are used to determine and acquire data.
            if (node.getKind().name().equals("MARKDOWN")) {
                text.append(htmlRenderer.render(markParser.parse(node.toString())));
            }
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitReference(ReferenceTree node, DocumentXMLBuilder p) {
            text.append(node.getSignature());
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitStartElement(StartElementTree node, DocumentXMLBuilder p) {
            text.append("<").append(node.getName());
            node.getAttributes().forEach(attr -> attr.accept(this, this));
            text.append(node.isSelfClosing() ? "/>" : ">");
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitSnippet(SnippetTree node, DocumentXMLBuilder p) {
            String lang = "java";
            for (DocTree tree : node.getAttributes()) {
                if (tree instanceof AttributeTree) {
                    AttributeTree attr = (AttributeTree) tree;
                    String name = attr.getName().toString();
                    if (name.equalsIgnoreCase("language") || name.equalsIgnoreCase("lang")) {
                        lang = attr.getValue().get(0).toString().toLowerCase();
                    }
                }
            }

            writeSourceCode(node.getBody().getBody(), lang);
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitSummary(SummaryTree node, DocumentXMLBuilder p) {
            return super.visitSummary(node, p);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitText(TextTree node, DocumentXMLBuilder p) {
            text.append(resolve(node.getBody()));
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitUnknownBlockTag(UnknownBlockTagTree node, DocumentXMLBuilder p) {
            text.append(resolve(node.toString()));
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitUnknownInlineTag(UnknownInlineTagTree node, DocumentXMLBuilder p) {
            text.append(resolve(node.toString()));
            return p;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DocumentXMLBuilder visitValue(ValueTree node, DocumentXMLBuilder p) {
            return super.visitValue(node, p);
        }

        /**
         * Write code snippet.
         * 
         * @param code
         * @param lang
         */
        private void writeSourceCode(String code, String lang) {
            lang = lang.trim().toLowerCase();

            CodeHighlight.addLanguage(lang);

            text.append("<pre class='lang-").append(lang).append("'><code>");
            text.append(resolve(code.trim()));
            text.append("</code></pre>");
        }

        /**
         * Resolve expression language.
         * 
         * @param text
         * @return
         */
        private String resolve(String text) {
            return XML.escape(I.express(text, "{@var", "}", new Object[] {templateTags}));
        }
    }

    /**
     * 
     */
    private class TypeXMLBuilder extends SimpleTypeVisitor9<XML, XML> {

        /**
         * Parse documetation.
         * 
         * @param docs
         * @return
         */
        private XML parse(TypeMirror type) {
            XML root = I.xml("<code/>");
            type.accept(this, root);
            return root;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML visitIntersection(IntersectionType t, XML xml) {
            return xml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML visitUnion(UnionType t, XML xml) {
            return xml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML visitPrimitive(PrimitiveType primitive, XML xml) {
            xml.text(primitive.toString());
            return xml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML visitNull(NullType t, XML xml) {
            return xml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML visitArray(ArrayType array, XML xml) {
            xml.attr("array", "fix");
            array.getComponentType().accept(this, xml);
            return xml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML visitDeclared(DeclaredType declared, XML xml) {
            // link to type
            TypeElement type = (TypeElement) declared.asElement();
            String name = type.getSimpleName().toString();
            String fqcn = type.getQualifiedName().toString();
            String uri = resolver.resolveDocumentLocation(type);
            boolean external = resolver.isExternal(type);

            if (uri != null) {
                if (external) {
                    fqcn += " 🚀";
                }
                xml.append(I.xml("a").attr("href", uri).attr("aria-label", fqcn).text(name));
            } else {
                xml.text(fqcn);
            }

            // type parameter
            List<? extends TypeMirror> paramTypes = declared.getTypeArguments();
            if (paramTypes.isEmpty() == false) {
                XML parameters = I.xml("<code class='parameters'/>");
                for (int i = 0, size = paramTypes.size(); i < size; i++) {
                    parameters.append(parseTypeAsXML(paramTypes.get(i)));

                    if (i + 1 != size) {
                        parameters.append(", ");
                    }
                }
                xml.append(parameters);
            }

            return xml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML visitError(ErrorType t, XML xml) {
            return xml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML visitTypeVariable(TypeVariable variable, XML xml) {
            xml.text(variable.toString());
            return xml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML visitWildcard(WildcardType wildcard, XML xml) {
            TypeMirror bounded = wildcard.getExtendsBound();
            if (bounded != null) {
                xml.text("?");
                xml.after("<code class='extends'/>").next().append(parseTypeAsXML(bounded));
                return xml;
            }

            bounded = wildcard.getSuperBound();
            if (bounded != null) {
                xml.text("?");
                xml.after("<code class='super'/>").next().append(parseTypeAsXML(bounded));
                return xml;
            }

            xml.text("?");
            return xml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML visitExecutable(ExecutableType t, XML xml) {
            return xml;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XML visitNoType(NoType no, XML xml) {
            switch (no.getKind()) {
            case VOID:
                xml.text("void");
                break;

            default:
            }
            return xml;
        }
    }
}