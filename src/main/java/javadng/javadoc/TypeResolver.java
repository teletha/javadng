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

import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.reflect.Modifier;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree.Kind;

import kiss.I;

public class TypeResolver {

    /** built-in java.lang types */
    private static final Map<String, String> JavaLangTypes = collectJavaLangTypes();

    /**
     * Register all classes under the java.lang package as classes that can be resolved.
     */
    private static Map<String, String> collectJavaLangTypes() {
        Map<String, String> map = new HashMap();

        try {
            ModuleReference module = ModuleFinder.ofSystem().find("java.base").get();

            try (Stream<String> resources = module.open().list()) {
                resources.filter(name -> name.startsWith("java/lang/") && name.indexOf("/", 10) == -1 && name.endsWith(".class"))
                        .map(name -> name.replace('/', '.').substring(0, name.length() - 6))
                        .map(name -> {
                            try {
                                return Class.forName(name);
                            } catch (Throwable e) {
                                return null;
                            }
                        })
                        .filter(clazz -> clazz != null && Modifier.isPublic(clazz.getModifiers()))
                        .forEach(clazz -> map.put(clazz.getSimpleName(), clazz.getCanonicalName()));
            }
            return map;
        } catch (IOException e) {
            throw I.quiet(e);
        }
    }

    /** PackageName-URL pair */
    private final Map<String, String> externals;

    /** Internal package mames */
    private final Set<String> internals;

    /** Imported types. */
    private final Map<String, String> importedTypes = new HashMap();

    private TypeElement clazz;

    /**
     * @param externals
     */
    public TypeResolver(Map<String, String> externals, Set<String> internals, TypeElement clazz) {
        this.externals = externals == null ? Map.of() : externals;
        this.internals = internals == null ? Set.of() : internals;
        this.clazz = clazz;

        collectImportedTypes(clazz);
        collectMemberTypes(clazz);
    }

    /**
     * Collect the imported types.
     * 
     * @param clazz
     */
    private void collectImportedTypes(Element clazz) {
        I.signal(Util.DocUtils.getPath(clazz))
                .take(tree -> tree.getKind() == Kind.COMPILATION_UNIT)
                .as(CompilationUnitTree.class)
                .flatIterable(CompilationUnitTree::getImports)
                .to(tree -> {
                    if (tree.isStatic()) {

                    } else {
                        String fqcn = tree.getQualifiedIdentifier().toString();
                        importedTypes.put(fqcn.substring(fqcn.lastIndexOf(".") + 1), fqcn);
                    }
                });
    }

    /**
     * Collect the member types.
     * 
     * @param clazz
     */
    private void collectMemberTypes(TypeElement clazz) {
        String fqcn = clazz.getQualifiedName().toString();
        importedTypes.put(fqcn.substring(fqcn.lastIndexOf(".") + 1), fqcn);

        I.signal(clazz.getEnclosedElements())
                .take(e -> e.getKind().isInterface() || e.getKind().isClass())
                .as(TypeElement.class)
                .to(this::collectMemberTypes);
    }

    private static final Pattern ARRAY = Pattern.compile("([^\\[\\]\\.]+)([\\[\\]\\.]+)$");

    /**
     * Return the URL of the document for the specified type.
     * 
     * @param type A target type to locate document.
     * @return
     */
    public final boolean isExternal(String type) {
        return resolve(type).isExternal();
    }

    /**
     * Return the URL of the document for the specified type.
     * 
     * @param type A target type to locate document.
     * @return
     */
    public final boolean isExternal(DeclaredType type) {
        return isExternal((TypeElement) type.asElement());
    }

    /**
     * Return the URL of the document for the specified type.
     * 
     * @param type A target type to locate document.
     * @return
     */
    public final boolean isExternal(TypeElement type) {
        return resolve(type).isExternal();
    }

    /**
     * Compute FQCN from the specified simple name.
     * 
     * @param className
     */
    public final String resolveFQCN(String className) {
        String front;
        String rear;

        Matcher matcher = ARRAY.matcher(className);
        if (matcher.matches()) {
            front = matcher.group(1);
            rear = matcher.group(2);
        } else {
            front = className;
            rear = "";
        }

        String fqcn = importedTypes.get(front);
        if (fqcn == null) fqcn = JavaLangTypes.get(front);

        return (fqcn == null ? front : fqcn) + rear;
    }

    /**
     * Return the URL of the document for the specified type.
     * 
     * @param type A target type to locate document.
     * @return
     */
    public final String resolveDocumentLocation(String type) {
        return resolve(type).location();
    }

    /**
     * Return the URL of the document for the specified type.
     * 
     * @param type A target type to locate document.
     * @return
     */
    public final String resolveDocumentLocation(DeclaredType type) {
        return resolveDocumentLocation((TypeElement) type.asElement());
    }

    /**
     * Return the URL of the document for the specified type.
     * 
     * @param type A target type to locate document.
     * @return
     */
    public final String resolveDocumentLocation(TypeElement type) {
        return resolve(type).location();
    }

    /**
     * Resolve from element.
     * 
     * @return Resoleved type.
     */
    private final ResolvedType resolve(TypeElement e) {
        ResolvedType resolved = new ResolvedType();
        resolved.typeName = e.getSimpleName().toString();

        // enclosing
        Deque<String> enclosings = new LinkedList();
        Element enclosing = e.getEnclosingElement();
        while (enclosing.getKind() != ElementKind.PACKAGE) {
            enclosings.addFirst(((TypeElement) enclosing).getSimpleName().toString());
            enclosing = enclosing.getEnclosingElement();
        }
        resolved.enclosingName = String.join(".", enclosings);

        // pacakage
        resolved.packageName = enclosing.toString();

        // module
        enclosing = enclosing.getEnclosingElement();

        if (enclosing instanceof ModuleElement) {
            ModuleElement module = (ModuleElement) enclosing;
            resolved.moduleName = module.getQualifiedName().toString();
        } else {
            resolved.moduleName = "";
        }

        return resolved;
    }

    private final ResolvedType resolve(String typeName) {
        if (typeName.indexOf('.') == -1) {
            typeName = resolveFQCN(typeName);
        }

        TypeElement type = Util.ElementUtils.getTypeElement(typeName);

        if (type == null) {
            int index = typeName.lastIndexOf('.');

            ResolvedType resolved = new ResolvedType();
            if (index == -1) {
                resolved.typeName = typeName;
            } else {
                resolved.packageName = typeName.substring(0, index);
                resolved.typeName = typeName.substring(index + 1);
            }
            return resolved;
        } else {
            return resolve(type);
        }
    }

    /**
     * Completed resolved type.
     */
    private class ResolvedType {

        private String typeName = "";

        private String enclosingName = "";

        private String packageName = "";

        private String moduleName = "";

        /**
         * Returns the URL of the document with the specified type name.
         * 
         * @param moduleName Module name. Null or empty string is ignored.
         * @param packageName Package name. Null or empty string is ignored.
         * @param enclosingName Enclosing type name. Null or empty string is ignored.
         * @param typeName Target type's simple name.
         * @return Resoleved URL.
         */
        private String location() {
            String externalURL = externals.get(packageName);

            if (externalURL != null) {
                StringBuilder builder = new StringBuilder(externalURL);
                if (moduleName.length() != 0) builder.append(moduleName).append('/');
                if (packageName.length() != 0) builder.append(packageName.replace('.', '/')).append('/');
                if (enclosingName.length() != 0) builder.append(enclosingName).append('.');
                builder.append(typeName).append(".html");

                return builder.toString();
            }

            if (internals.contains(packageName)) {
                StringBuilder builder = new StringBuilder("api/");
                if (packageName.length() != 0) builder.append(packageName).append('.');
                if (enclosingName.length() != 0) builder.append(enclosingName).append('.');
                builder.append(typeName).append(".html");

                return builder.toString();
            }

            if (!enclosingName.isEmpty() && clazz.getQualifiedName().toString().startsWith(packageName)) {
                return "doc/" + packageName + "." + enclosingName + ".html#" + packageName + "." + enclosingName + "." + typeName;
            }
            return null;
        }

        /**
         * Check whether this type is external or not.
         * 
         * @return
         */
        private boolean isExternal() {
            return externals.containsKey(packageName);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "ResolvedType [typeName=" + typeName + ", enclosingName=" + enclosingName + ", packageName=" + packageName + ", moduleName=" + moduleName + "]";
        }
    }
}