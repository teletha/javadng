/*
 * Copyright (C) 2025 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.parser;

import javax.lang.model.element.VariableElement;

import kiss.XML;

public class FieldInfo extends MemberInfo {

    private final XML type;

    /**
     * @param e
     */
    FieldInfo(VariableElement e, TypeResolver resolver, DocumentInfo parent) {
        super(e, resolver, parent);

        this.type = parseTypeAsXML(e.asType()).addClass("return");
    }

    /**
     * Build type element.
     * 
     * @return
     */
    public XML createType() {
        return type.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String id() {
        return name;
    }
}