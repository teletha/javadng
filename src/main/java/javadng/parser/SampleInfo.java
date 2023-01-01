/*
 * Copyright (C) 2023 The JAVADNG Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package javadng.parser;

import kiss.Variable;
import kiss.XML;

public class SampleInfo {

    /** The class reference. */
    public final String classID;

    /** The method reference. */
    public final String methodID;

    /** The sample code. (raw text) */
    public final String code;

    /** The comment for sample. */
    public final Variable<XML> comment = Variable.empty();

    /**
     * @param classID
     * @param methodID
     * @param code
     */
    public SampleInfo(String classID, String methodID, String code) {
        this.classID = classID;
        this.methodID = methodID;
        this.code = code;
    }

    /**
     * Get the sample id.
     * 
     * @return
     */
    public String id() {
        return classID + "#" + methodID;
    }
}