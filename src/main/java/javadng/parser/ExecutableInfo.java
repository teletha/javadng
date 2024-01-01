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

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;

import javadng.design.Styles;
import kiss.I;
import kiss.XML;

public class ExecutableInfo extends ParameterizableInfo {

    /** The parameter name manager. */
    private final List<String> names = new ArrayList();

    /** The parameter signature manager. */
    private final List<XML> signatures = new ArrayList();

    /** The parameter comment manager. */
    private final List<XML> comments = new ArrayList();

    /** The exception signature manager. */
    private final List<XML> exceptionSignatures = new ArrayList();

    /** The exception comment manager. */
    private final List<XML> exceptionComments = new ArrayList();

    private final String id;

    /**
     * @param e
     */
    ExecutableInfo(ExecutableElement e, TypeResolver resolver, DocumentInfo parent) {
        super(e, resolver, parent);

        StringJoiner joiner = new StringJoiner(",");
        List<? extends VariableElement> params = e.getParameters();
        for (int i = 0; i < params.size(); i++) {
            VariableElement param = params.get(i);
            joiner.add(canonicalize(param.asType(), i + 1 == params.size() && e.isVarArgs()));

            XML xml = parseTypeAsXML(param.asType());
            if (e.isVarArgs() && i + 1 == params.size()) {
                xml.attr("array", "var");
            }
            names.add(param.toString());
            signatures.add(xml);
            comments.add(findParamTagBy(param.toString()));
        }

        for (TypeMirror type : e.getThrownTypes()) {
            exceptionSignatures.add(parseTypeAsXML(type));
            exceptionComments.add(findThrowsTagBy(type.toString()));
        }

        this.id = name + "(" + joiner + ")";
    }

    /**
     * If you're referring to it from elsewhere in Javadoc, the parser won't resolve it with fully
     * qualified names, so strip off the information on the referenced side beforehand.
     * 
     * @param text
     * @return
     */
    private String canonicalize(TypeMirror type, boolean varArgs) {
        switch (type.getKind()) {
        case ARRAY:
            return canonicalize(((ArrayType) type).getComponentType(), false) + (varArgs ? "..." : "[]");
        case BOOLEAN:
            return "boolean";
        case BYTE:
            return "byte";
        case CHAR:
            return "char";
        case DECLARED:
            return type.toString().replaceAll("<.+>", "");
        case DOUBLE:
            return "double";
        case FLOAT:
            return "float";
        case INT:
            return "int";
        case LONG:
            return "long";
        case SHORT:
            return "short";
        case TYPEVAR:
            return "java.lang.Object";
        case WILDCARD:
            return "java.lang.Object";
        default:
            throw new Error("Fix bug! " + type.getKind());
        }
    }

    /**
     * Compute the number of parameters.
     */
    public final int numberOfParameters() {
        return names.size();
    }

    /**
     * Compute the number of exceptions.
     */
    public final int numberOfExceptions() {
        return exceptionSignatures.size();
    }

    /**
     * Build parameter element.
     * 
     * @return
     */
    public final XML createParameter() {
        XML xml = I.xml("code").addClass(Styles.SignatureParameterPart.className());
        xml.append("(");
        for (int i = 0, size = names.size(); i < size; i++) {
            xml.append(createParameter(i));
            xml.append(" ");
            xml.append(I.xml("code").addClass("parameterName").text(names.get(i)));

            if (i + 1 != size) {
                xml.append(", ");
            }
        }
        xml.append(")");

        return xml;
    }

    /**
     * Build parameter element.
     * 
     * @return
     */
    public final XML createParameter(int index) {
        return signatures.get(index).clone();
    }

    /**
     * Build parameter element.
     * 
     * @return
     */
    public final XML createParameterName(int index) {
        return I.xml("<code/>").text(names.get(index));
    }

    /**
     * Build parameter element.
     * 
     * @return
     */
    public final XML createParameterComment(int index) {
        XML comment = comments.get(index);

        if (comment == null) {
            return null;
        } else {
            return comment.clone();
        }
    }

    /**
     * Build exception element.
     * 
     * @return
     */
    public final XML createException(int index) {
        return exceptionSignatures.get(index).clone();
    }

    /**
     * Build exception element.
     * 
     * @return
     */
    public final XML createExceptionComment(int index) {
        XML comment = exceptionComments.get(index);

        if (comment == null) {
            return null;
        } else {
            return comment.clone();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String id() {
        return id;
    }

    /**
     * Build return type element.
     * 
     * @return
     */
    public XML createReturnType() {
        return null;
    }

    /**
     * Build return comment.
     * 
     * @return
     */
    public XML createReturnComment() {
        return null;
    }

    /**
     * Check the return type is void or not.
     * 
     * @return
     */
    public boolean returnVoid() {
        return true;
    }
}