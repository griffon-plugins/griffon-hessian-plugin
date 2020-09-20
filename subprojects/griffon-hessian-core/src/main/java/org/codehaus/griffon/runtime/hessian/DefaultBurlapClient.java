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

import com.caucho.burlap.client.BurlapProxyFactory;
import griffon.annotations.core.Nonnull;
import griffon.plugins.hessian.BurlapClient;
import griffon.plugins.hessian.exceptions.BurlapException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static griffon.util.GriffonNameUtils.requireNonBlank;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class DefaultBurlapClient implements BurlapClient {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBurlapClient.class);

    private static final String ERROR_PROXY_CLASS_NAME_BLANK = "Argument 'proxyClassName' must not be blank";

    private final Map<String, Object> proxies = new ConcurrentHashMap<>();
    private final String url;
    private final BurlapProxyFactory burlapFactory;

    public DefaultBurlapClient(@Nonnull String url) {
        this.url = requireNonBlank(url, "Argument 'url' must not be blank");
        burlapFactory = new BurlapProxyFactory();
    }

    @Override
    public <T, R> R proxy(@Nonnull Class<T> proxyClass, @Nonnull Function<T, R> consumer) {
        requireNonNull(proxyClass, "Argument 'proxyClass' must not be null");
        String proxyClassName = proxyClass.getName();
        LOG.debug("Invoking burlap consumer on proxy {}", proxyClassName);
        return consumer.apply(proxyClass.cast(locateProxy(proxyClass)));
    }

    @Override
    public void removeProxy(@Nonnull String proxyClassName) {
        requireNonBlank(proxyClassName, ERROR_PROXY_CLASS_NAME_BLANK);
        if (proxies.containsKey(proxyClassName)) {
            LOG.debug("Removing proxy {}", proxyClassName);
            proxies.remove(proxyClassName);
        }
    }

    @Nonnull
    @Override
    public Set<String> geProxyClassNames() {
        Set<String> names = new LinkedHashSet<>();
        synchronized (proxies) {
            names.addAll(proxies.keySet());
        }
        return unmodifiableSet(names);
    }

    @Nonnull
    @Override
    public Map<String, Object> getProxies() {
        Map<String, Object> map = new LinkedHashMap<>();
        synchronized (proxies) {
            map.putAll(proxies);
        }
        return unmodifiableMap(map);
    }

    @Nonnull
    private Object locateProxy(@Nonnull Class<?> proxyClass) {
        String proxyClassName = proxyClass.getName();
        Object proxy = proxies.get(proxyClassName);
        if (proxy == null) {
            try {
                LOG.debug("Locating burlap proxy {} at {}", proxyClass.getName(), url);
                proxy = burlapFactory.create(proxyClass, url);
            } catch (MalformedURLException e) {
                throw new BurlapException("An error occurred while looking up " + proxyClassName + " at " + url, e);
            }
            proxies.put(proxyClassName, proxy);
        }
        return proxy;
    }
}
