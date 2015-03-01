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
package org.codehaus.griffon.runtime.hessian;

import griffon.plugins.hessian.HessianClient;
import griffon.plugins.hessian.UnaryConsumer;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class HessianClientDecorator implements HessianClient {
    private final HessianClient delegate;

    public HessianClientDecorator(@Nonnull HessianClient delegate) {
        this.delegate = requireNonNull(delegate, "Argument 'delegate' must not be null");
    }

    @Nonnull
    protected HessianClient getDelegate() {
        return delegate;
    }

    @Override
    public <T, R> R proxy(@Nonnull Class<T> proxyClass, @Nonnull UnaryConsumer<T, R> consumer) {
        return delegate.proxy(proxyClass, consumer);
    }

    @Override
    public void removeProxy(@Nonnull String proxyClassName) {
        delegate.removeProxy(proxyClassName);
    }

    @Override
    @Nonnull
    public Set<String> geProxyClassNames() {
        return delegate.geProxyClassNames();
    }

    @Override
    @Nonnull
    public Map<String, Object> getProxies() {
        return delegate.getProxies();
    }
}
