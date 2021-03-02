/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2014-2021 The author and/or original authors.
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
package griffon.plugins.hessian;

import griffon.annotations.core.Nonnull;
import griffon.annotations.core.Nullable;
import griffon.core.artifact.GriffonService;
import griffon.plugins.hessian.exceptions.BurlapException;
import griffon.util.CollectionUtils;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.kordamp.jipsy.annotations.ServiceProviderFor;

import javax.inject.Inject;
import java.util.Map;
import java.util.function.Function;

@ServiceProviderFor(GriffonService.class)
public class BurlapCalculatorService extends AbstractGriffonService {
    @Inject
    private BurlapHandler hessianHandler;

    public Double calculate(final double num1, final double num2) {
        Map<String, Object> params = CollectionUtils.<String, Object>map()
            .e("url", "http://localhost:8080/burlap/calculator")
            .e("id", "client");
        return hessianHandler.withBurlap(params,
            new BurlapClientCallback<Double>() {
                @Nullable
                public Double handle(@Nonnull Map<String, Object> params, @Nonnull BurlapClient client)
                    throws BurlapException {
                    return client.proxy(Calculator.class, new Function<Calculator, Double>() {
                        @Override
                        public Double apply(Calculator calculator) {
                            return calculator.add(num1, num2);
                        }
                    });
                }
            });
    }
}