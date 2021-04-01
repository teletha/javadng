/*
 * Copyright (C) 2019 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package stoneforge.javadoc.analyze;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.SimpleElementVisitor14;

import com.sun.source.tree.MethodTree;
import com.sun.source.util.DocSourcePositions;

public class SampleInfo extends DocumentInfo {

    /**
     * @param e
     * @param resolver
     * @param parent
     */
    public SampleInfo(Element e, TypeResolver resolver, DocumentInfo parent) {
        super(e, resolver, parent);

        Scanner scanner = new Scanner();
        for (Element element : e.getEnclosedElements()) {
            element.accept(scanner, this);
        }
    }

    /**
     * 
     */
    private class Scanner extends SimpleElementVisitor14<SampleInfo, SampleInfo> {

        /**
         * {@inheritDoc}
         */
        @Override
        public SampleInfo visitExecutable(ExecutableElement e, SampleInfo p) {
            if (e.getKind() == ElementKind.METHOD) {
                new SampleMethodInfo(e, p.resolver, p);
            }
            return p;
        }
    }

    private class SampleMethodInfo extends DocumentInfo {

        /**
         * @param e
         * @param resolver
         * @param parent
         */
        public SampleMethodInfo(Element e, TypeResolver resolver, DocumentInfo parent) {
            super(e, resolver, parent);

            if (!seeTags.isEmpty()) {
                System.out.println(seeTags + "   " + parent.e + "  " + e);
                MethodTree tree = (MethodTree) Util.DocUtils.getTree(e);
                DocSourcePositions p = Util.DocUtils.getSourcePositions();
                System.out.println(p.getStartPosition(null, tree) + "   " + p.getEndPosition(null, tree));

            }
        }
    }
}
