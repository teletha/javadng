/*
 * Copyright (C) 2020 stoneforge Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.parser.analyze;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import kiss.I;
import kiss.XML;

public abstract class MemberInfo extends DocumentInfo {

    /** The name of this member. */
    public final String name;

    /** The modifier of this member. */
    public final Set<Modifier> modifiers;

    /** The source type. */
    private final DocumentInfo parent;

    /**
     * @param e
     * @param resolver
     * @param parent
     */
    public MemberInfo(Element e, TypeResolver resolver, DocumentInfo parent) {
        super(e, resolver, parent);

        String name = e.getSimpleName().toString();
        if (name.equals("<init>")) {
            name = e.getEnclosingElement().getSimpleName().toString();
        }

        this.name = name;
        this.modifiers = e.getModifiers();
        this.parent = parent;
    }

    /**
     * Check whether this member has the specified modifiers or not.
     * 
     * @return Result.
     */
    public final boolean is(Modifier... modifiers) {
        for (Modifier modifier : modifiers) {
            if (!this.modifiers.contains(modifier)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether this member has public modifier or not.
     * 
     * @return Result.
     */
    public final boolean isPublic() {
        return modifiers.contains(Modifier.PUBLIC);
    }

    /**
     * Check whether this member has static modifier or not.
     * 
     * @return Result.
     */
    public final boolean isStatic() {
        return modifiers.contains(Modifier.STATIC);
    }

    /**
     * Check whether this member is deprecated or not.
     * 
     * @return Result.
     */
    public final boolean isDeprecated() {
        return Util.ElementUtils.isDeprecated(e);
    }

    /**
     * Check whether this member is overridden or not.
     * 
     * @return Result.
     */
    public final boolean isOverridden() {
        return e.getAnnotation(Override.class) != null;
    }

    /**
     * Build modifier element.
     * 
     * @return
     */
    public final XML createModifier() {
        Set<Modifier> visibility = new HashSet();
        Set<Modifier> nonvisibility = new HashSet();
        boolean isPackagePrivate = true;

        for (Modifier modifier : modifiers) {
            switch (modifier) {
            case ABSTRACT:
            case FINAL:
            case VOLATILE:
            case SYNCHRONIZED:
            case TRANSIENT:
            case DEFAULT:
                nonvisibility.add(modifier);
                break;

            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
                isPackagePrivate = false;
                // fall-throgh

            default:
                visibility.add(modifier);
                break;
            }
        }

        XML xml = I.xml("<i/>");
        if (isPackagePrivate) xml.addClass("PACKAGEPRIVATE");
        if (isOverridden()) xml.addClass("OVERRIDE");
        for (Modifier modifier : visibility) {
            xml.addClass(modifier.name());
        }

        if (!nonvisibility.isEmpty()) {
            xml = I.xml("<i/>").append(xml);
            for (Modifier modifier : nonvisibility) {
                xml.addClass(modifier.name());
            }
        }
        return xml;
    }

    /**
     * Build name element.
     * 
     * @return
     */
    public final XML createName() {
        return I.xml("code")
                .child("a")
                .attr("href", resolver.resolveDocumentLocation((TypeElement) parent.e) + "#" + id())
                .text(name)
                .parent();
    }

    /**
     * Compute the member identifier.
     * 
     * @return
     */
    public abstract String id();

    /**
     * Compute the documentation title
     * 
     * @return
     */
    public String title() {
        // compute from heading text in javadoc
        if (hasDocument()) {
            XML first = comment.v.first().firstChild();
            if (first.name().equals("h") || first.name().matches("h\\d")) {
                return first.text();
            }
        }

        // compute from class name
        String title = name.replace('_', ' ').replaceAll(".+\\.", "");

        // append '?'
        if (title.endsWith(" ")) {
            title = title.replaceAll(" $", "?");
        }

        return title;
    }
}