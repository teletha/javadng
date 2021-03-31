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
import javax.lang.model.util.SimpleElementVisitor9;

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
    private class Scanner extends SimpleElementVisitor9<SampleInfo, SampleInfo> {

        /**
         * {@inheritDoc}
         */
        @Override
        public SampleInfo visitExecutable(ExecutableElement e, SampleInfo p) {
            if (isVisible(e)) {
                if (e.getKind() == ElementKind.METHOD) {
                    System.out.println(e);
                }
            }
            return p;
        }
    }
}
