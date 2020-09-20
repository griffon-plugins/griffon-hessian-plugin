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
package org.codehaus.griffon.runtime.hessian;

import griffon.annotations.core.Nonnull;
import griffon.plugins.hessian.HessianClient;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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
    public <T, R> R proxy(@Nonnull Class<T> proxyClass, @Nonnull Function<T, R> consumer) {
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
