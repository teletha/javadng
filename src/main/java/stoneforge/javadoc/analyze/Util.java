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
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.DocSourcePositions;
import com.sun.source.util.DocTrees;
import com.sun.source.util.TreePath;

import kiss.I;

public final class Util {

    /** Guilty Accessor. */
    public static DocTrees DocUtils;

    /** Guilty Accessor. */
    public static Elements ElementUtils;

    /** Guilty Accessor. */
    public static Types TypeUtils;

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
            String[] lines = cut.getSourceFile().getCharContent(true).subSequence(start, end).toString().split("\\r\\n|\\r|\\n");
            int indent = Arrays.stream(lines).mapToInt(Util::countHeaderWhitespace).filter(i -> 0 < i).min().getAsInt();
            return Arrays.stream(lines).map(line -> stripHeaderWhitespace(line, indent)).collect(Collectors.joining("\r\n"));
        } catch (IOException error) {
            throw I.quiet(error);
        }
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