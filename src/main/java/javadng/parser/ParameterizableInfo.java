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

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Parameterizable;
import javax.lang.model.type.TypeMirror;

import javadng.design.Styles;
import kiss.I;
import kiss.XML;

public abstract class ParameterizableInfo extends MemberInfo {

    /** The type variable name manager. */
    private final List<String> names = new ArrayList();

    /** The type variable signature manager. */
    private final List<XML> signatures = new ArrayList();

    /** The type variable comment manager. */
    private final List<XML> comments = new ArrayList();

    /**
     * @param e
     */
    public ParameterizableInfo(Parameterizable e, TypeResolver resolver, DocumentInfo parent) {
        super(e, resolver, parent);

        e.getTypeParameters().forEach(type -> {
            XML param = parseTypeAsXML(type.asType());
            List<? extends TypeMirror> bounds = type.getBounds();
            int size = bounds.size();
            if (size != 0) {
                if (size != 1 || !bounds.get(0).toString().equals("java.lang.Object")) {
                    XML extend = I.xml("<code/>").addClass("extends");
                    for (int i = 0; i < size; i++) {
                        if (i != 0) {
                            extend.append(" & ");
                        }
                        extend.append(parseTypeAsXML(bounds.get(i)));
                    }
                    param.after(extend);
                }
            }

            String name = type.getSimpleName().toString();
            names.add(name);
            signatures.add(param.parent().children());
            comments.add(findTypeVariableTagBy(name));
        });
    }

    /**
     * Compute the number of type variable declarations.
     */
    public final int numberOfTypeVariables() {
        return names.size();
    }

    /**
     * Build type variable element.
     * 
     * @return
     */
    public final XML createTypeVariables() {
        if (names.isEmpty()) {
            return null;
        }

        XML xml = I.xml("code").addClass(Styles.SignatureParameterPart.className());
        xml.append("<");
        for (int i = 0, size = names.size(); i < size; i++) {
            xml.append(createTypeVariable(i));

            if (i + 1 != size) {
                xml.append(", ");
            }
        }
        xml.append(">");

        return xml;
    }

    /**
     * Build type variable element.
     * 
     * @return
     */
    public final XML createTypeVariable(int index) {
        return signatures.get(index).clone();
    }

    /**
     * Build type variable element.
     * 
     * @return
     */
    public final XML createTypeVariableNames() {
        if (names.isEmpty()) {
            return null;
        }

        XML xml = I.xml("code").addClass(Styles.SignatureParameterPart.className());
        xml.append("<");
        for (int i = 0, size = names.size(); i < size; i++) {
            xml.append(createTypeVariableName(i));

            if (i + 1 != size) {
                xml.append(", ");
            }
        }
        xml.append(">");

        return xml;
    }

    /**
     * Build type variable element.
     * 
     * @return
     */
    public final XML createTypeVariableName(int index) {
        return I.xml("<code/>").text(names.get(index));
    }

    /**
     * Build type variable element.
     * 
     * @return
     */
    public final XML createTypeVariableComment(int index) {
        XML comment = comments.get(index);

        if (comment == null) {
            return null;
        } else {
            return comment.clone();
        }
    }
}