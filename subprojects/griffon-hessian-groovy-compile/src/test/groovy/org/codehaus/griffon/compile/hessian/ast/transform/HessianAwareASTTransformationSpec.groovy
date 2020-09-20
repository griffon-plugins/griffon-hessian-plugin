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
package org.codehaus.griffon.compile.hessian.ast.transform

import griffon.plugins.hessian.HessianHandler
import spock.lang.Specification

import java.lang.reflect.Method

/**
 * @author Andres Almiray
 */
class HessianAwareASTTransformationSpec extends Specification {
    def 'HessianAwareASTTransformation is applied to a bean via @HessianAware'() {
        given:
        GroovyShell shell = new GroovyShell()

        when:
        def bean = shell.evaluate('''
        @griffon.transform.hessian.HessianAware
        class Bean { }
        new Bean()
        ''')

        then:
        bean instanceof HessianHandler
        HessianHandler.methods.every { Method target ->
            bean.class.declaredMethods.find { Method candidate ->
                candidate.name == target.name &&
                    candidate.returnType == target.returnType &&
                    candidate.parameterTypes == target.parameterTypes &&
                    candidate.exceptionTypes == target.exceptionTypes
            }
        }
    }

    def 'HessianAwareASTTransformation is not applied to a HessianHandler subclass via @HessianAware'() {
        given:
        GroovyShell shell = new GroovyShell()

        when:
        def bean = shell.evaluate('''
        import griffon.plugins.hessian.*
        import griffon.plugins.hessian.exceptions.*

        import griffon.annotations.core.Nonnull
        @griffon.transform.hessian.HessianAware
        class HessianHandlerBean implements HessianHandler {
            @Override
             <R> R withHessian(@Nonnull Map<String,Object> params, @Nonnull HessianClientCallback<R> callback) throws HessianException{
                return null
            }
            @Override
            void destroyHessianClient(@Nonnull String clientId) {}
        }
        new HessianHandlerBean()
        ''')

        then:
        bean instanceof HessianHandler
        HessianHandler.methods.every { Method target ->
            bean.class.declaredMethods.find { Method candidate ->
                candidate.name == target.name &&
                    candidate.returnType == target.returnType &&
                    candidate.parameterTypes == target.parameterTypes &&
                    candidate.exceptionTypes == target.exceptionTypes
            }
        }
    }
}
