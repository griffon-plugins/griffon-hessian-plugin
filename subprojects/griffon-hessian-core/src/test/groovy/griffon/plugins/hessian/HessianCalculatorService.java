/*
 * Copyright 2014-2015 the original author or authors.
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
package griffon.plugins.hessian;

import griffon.core.GriffonApplication;
import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import griffon.plugins.hessian.exceptions.HessianException;
import griffon.util.CollectionUtils;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;

@ArtifactProviderFor(GriffonService.class)
public class HessianCalculatorService extends AbstractGriffonService {
    @Inject
    private HessianHandler hessianHandler;

    @Inject
    public HessianCalculatorService(@Nonnull GriffonApplication application) {
        super(application);
    }

    public Double calculate(final double num1, final double num2) {
        Map<String, Object> params = CollectionUtils.<String, Object>map()
            .e("url", "http://localhost:8080/hessian/calculator")
            .e("id", "client");
        return hessianHandler.withHessian(params,
            new HessianClientCallback<Double>() {
                @Nullable
                public Double handle(@Nonnull Map<String, Object> params, @Nonnull HessianClient client)
                    throws HessianException {
                    return client.proxy(Calculator.class, new UnaryConsumer<Calculator, Double>() {
                        @Override
                        public Double consume(Calculator calculator) {
                            return calculator.add(num1, num2);
                        }
                    });
                }
            });
    }
}