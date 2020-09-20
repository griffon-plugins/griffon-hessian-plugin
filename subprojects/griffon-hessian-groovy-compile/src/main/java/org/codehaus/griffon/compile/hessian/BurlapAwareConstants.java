/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2014-2020 The author and/or original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.compile.hessian;

import org.codehaus.griffon.compile.core.BaseConstants;
import org.codehaus.griffon.compile.core.MethodDescriptor;

import static org.codehaus.griffon.compile.core.MethodDescriptor.annotatedMethod;
import static org.codehaus.griffon.compile.core.MethodDescriptor.annotatedType;
import static org.codehaus.griffon.compile.core.MethodDescriptor.annotations;
import static org.codehaus.griffon.compile.core.MethodDescriptor.args;
import static org.codehaus.griffon.compile.core.MethodDescriptor.method;
import static org.codehaus.griffon.compile.core.MethodDescriptor.throwing;
import static org.codehaus.griffon.compile.core.MethodDescriptor.type;
import static org.codehaus.griffon.compile.core.MethodDescriptor.typeParams;
import static org.codehaus.griffon.compile.core.MethodDescriptor.types;

/**
 * @author Andres Almiray
 */
public interface BurlapAwareConstants extends BaseConstants {
    String BURLAP_HANDLER_TYPE = "griffon.plugins.hessian.BurlapHandler";
    String BURLAP_HANDLER_PROPERTY = "hessianHandler";
    String BURLAP__HANDLER_FIELD_NAME = "this$" + BURLAP_HANDLER_PROPERTY;
    String BURLAP_CLIENT_CALLBACK_TYPE = "griffon.plugins.hessian.BurlapClientCallback";
    String BURLAP_EXCEPTION_TYPE = "griffon.plugins.hessian.exceptions.BurlapException";

    String METHOD_WITH_BURLAP = "withBurlap";
    String METHOD_DESTROY_BURLAP_CLIENT = "destroyBurlapClient";

    String CALLBACK = "callback";
    String PARAMS = "params";

    MethodDescriptor[] METHODS = new MethodDescriptor[]{
        annotatedMethod(
            types(type(ANNOTATION_NULLABLE)),
            type(R),
            typeParams(R),
            METHOD_WITH_BURLAP,
            args(
                annotatedType(annotations(ANNOTATION_NONNULL), JAVA_UTIL_MAP, JAVA_LANG_STRING, JAVA_LANG_OBJECT),
                annotatedType(annotations(ANNOTATION_NONNULL), BURLAP_CLIENT_CALLBACK_TYPE, R)),
            throwing(type(BURLAP_EXCEPTION_TYPE))
        ),

        method(
            type(VOID),
            METHOD_DESTROY_BURLAP_CLIENT,
            args(annotatedType(types(type(ANNOTATION_NONNULL)), JAVA_LANG_STRING))
        )
    };
}
