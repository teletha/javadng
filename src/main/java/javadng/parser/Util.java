/*
 * Copyright (C) 2024 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.parser;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StaticJavaParser;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.DocSourcePositions;
import com.sun.source.util.DocTrees;
import com.sun.source.util.TreePath;

import kiss.I;
import psychopath.Directory;

public final class Util {

    static {
        StaticJavaParser.getParserConfiguration().setLanguageLevel(LanguageLevel.CURRENT);
    }

    /** Guilty Accessor. */
    public static DocTrees DocUtils;

    /** Guilty Accessor. */
    public static Elements ElementUtils;

    /** Guilty Accessor. */
    public static Types TypeUtils;

    /** Guilty Accessor. */
    public static List<Directory> Samples;

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
     * Get the document line numbers of the specified {@link Element}.
     * 
     * @param e
     * @return
     */
    public static int[] getDocumentLineNumbers(Element e) {
        try {
            DocSourcePositions positions = DocUtils.getSourcePositions();

            TreePath path = Util.DocUtils.getPath(e);
            CompilationUnitTree cut = path.getCompilationUnit();

            DocCommentTree tree = Util.DocUtils.getDocCommentTree(e);
            int start = (int) positions.getStartPosition(cut, tree, tree);
            int end = (int) positions.getEndPosition(cut, tree, tree);

            int[] lines = {1, 1};
            CharSequence chars = cut.getSourceFile().getCharContent(true);
            for (int i = 0; i < end; i++) {
                if (i == start) {
                    lines[0] = lines[1];
                }
                if (chars.charAt(i) == '\n') lines[1]++;
            }

            return lines;
        } catch (IOException x) {
            throw I.quiet(x);
        }
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