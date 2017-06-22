/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.plugins.hessian

import griffon.core.artifact.GriffonService
import griffon.metadata.ArtifactProviderFor

import javax.inject.Inject

@ArtifactProviderFor(GriffonService.class)
class GroovyHessianCalculatorService {
    @Inject
    private HessianHandler hessianHandler

    double calculate(double num1, double num2) {
        Map args = [id: 'client', url: 'http://localhost:8080/hessian/calculator']
        hessianHandler.withHessian(args) { Map params, HessianClient client ->
            client.proxy(Calculator) { Calculator calculator ->
                calculator.add(num1, num2)
            }
        }
    }
}