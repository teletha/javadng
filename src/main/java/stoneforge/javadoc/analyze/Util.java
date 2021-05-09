/*
 * Copyright (C) 2020 stoneforge Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package stoneforge.javadoc.analyze;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.DocSourcePositions;
import com.sun.source.util.DocTrees;
import com.sun.source.util.TreePath;

import kiss.I;
import psychopath.Directory;
import psychopath.File;

public final class Util {

    /** Guilty Accessor. */
    public static DocTrees DocUtils;

    /** Guilty Accessor. */
    public static Elements ElementUtils;

    /** Guilty Accessor. */
    public static Types TypeUtils;

    /** Guilty Accessor. */
    public static Directory Samples;

    /**
     * Find the top-level {@link TypeElement} (not member class).
     * 
     * @param e
     * @return
     */
    public static TypeElement getTopLevelTypeElement(Element e) {
        Element parent = e.getEnclosingElement();

        while (parent != null && parent.getKind() != ElementKind.PACKAGE) {
            e = parent;
            parent = e.getEnclosingElement();
        }
        return (TypeElement) e;
    }

    /**
     * Get the source code of the specified class.
     */
    public static String getSourceCode(String fqcn, String memberDescriptor) {
        try {
            File file = Samples.file(fqcn.replace('.', '/') + ".java");
            CompilationUnit parsed = StaticJavaParser.parse(file.asJavaFile());
            for (MethodDeclaration method : parsed.findAll(MethodDeclaration.class)) {
                if (method.getSignature().asString().equals(memberDescriptor)) {
                    Position begin = method.getBegin().get();
                    Position end = method.getEnd().get();
                    List<String> lines = file.lines().toList().subList(begin.line, end.line);

                    return stripHeaderWhitespace(lines.stream().collect(Collectors.joining("\r\n")));
                }
            }
        } catch (Exception e) {
            throw I.quiet(e);
        }
        return "";
    }

    /**
     * Get the source code of the specified {@link Element}.
     * 
     * @param doc
     * @return
     */
    public static String getSourceCode(DocumentInfo doc) {
        return getSourceCode(doc.e);
    }

    public static String getSourceCode(Element type, String memberDescriptor) {
        if (memberDescriptor.charAt(0) == '#') {
            memberDescriptor = memberDescriptor.substring(1);
        }

        for (Element e : type.getEnclosedElements()) {
            if (e.toString().equals(memberDescriptor)) {
                return getSourceCode(e);
            }
        }
        return "";
    }

    /**
     * Get the source code of the specified {@link Element}.
     * 
     * @param e
     * @return
     */
    public static String getSourceCode(Element e) {
        try {
            DocSourcePositions positions = DocUtils.getSourcePositions();

            TreePath path = Util.DocUtils.getPath(e);
            CompilationUnitTree cut = path.getCompilationUnit();
            int start = (int) positions.getStartPosition(cut, path.getLeaf());
            int end = (int) positions.getEndPosition(cut, path.getLeaf());
            return stripHeaderWhitespace(cut.getSourceFile().getCharContent(true).subSequence(start, end).toString());
        } catch (IOException error) {
            throw I.quiet(error);
        }
    }

    /**
     * Strip whitespace prettily for the formatted source code.
     * 
     * @param text
     * @return
     */
    public static String stripHeaderWhitespace(String text) {
        List<String> lines = I.list(text.split("\\r\\n|\\r|\\n"));

        if (lines.size() == 1) {
            return text;
        }

        // remove the empty line from head
        ListIterator<String> iter = lines.listIterator();
        while (iter.hasNext()) {
            String line = iter.next();
            if (line.isEmpty()) {
                iter.remove();
            } else {
                break;
            }
        }

        // remove the empty line from tail
        iter = lines.listIterator(lines.size());
        while (iter.hasPrevious()) {
            String line = iter.previous();
            if (line.isEmpty()) {
                iter.remove();
            } else {
                break;
            }
        }

        // remove @Override
        iter = lines.listIterator();
        while (iter.hasNext()) {
            String line = iter.next();
            if (line.trim().equals("@Override")) {
                iter.remove();
            }
        }

        // strip the common width indent
        int indent = lines.stream().mapToInt(Util::countHeaderWhitespace).filter(i -> 0 < i).min().getAsInt();
        return lines.stream().map(line -> stripHeaderWhitespace(line, indent)).collect(Collectors.joining("\r\n"));
    }

    private static int countHeaderWhitespace(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return i;
            }
        }
        return line.length();
    }

    private static String stripHeaderWhitespace(String line, int size) {
        if (line.length() < size) {
            return line;
        }

        for (int i = 0; i < size; i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return line;
            }
        }
        return line.substring(size);
    }

    /**
     * Collect all types.
     * 
     * @param type
     * @return
     */
    public static Set<TypeMirror>[] getAllTypes(Element type) {
        Set<TypeMirror> supers = new LinkedHashSet();
        Set<TypeMirror> interfaces = new TreeSet<>(Comparator
                .<TypeMirror, String> comparing(t -> ((TypeElement) Util.TypeUtils.asElement(t)).getSimpleName().toString()));
        collect(type.asType(), supers, interfaces);

        return new Set[] {supers, interfaces};
    }

    /**
     * Collect all types.
     * 
     * @param type
     * @param superTypes
     * @param interfaceTypes
     */
    private static void collect(TypeMirror type, Set<TypeMirror> superTypes, Set<TypeMirror> interfaceTypes) {
        for (TypeMirror up : Util.TypeUtils.directSupertypes(type)) {
            if (up.toString().equals("java.lang.Object")) {
                continue;
            }

            Element e = Util.TypeUtils.asElement(up);
            if (e.getKind() == ElementKind.INTERFACE) {
                interfaceTypes.add(up);
            } else {
                superTypes.add(up);
            }
            collect(up, superTypes, interfaceTypes);
        }
    }
}