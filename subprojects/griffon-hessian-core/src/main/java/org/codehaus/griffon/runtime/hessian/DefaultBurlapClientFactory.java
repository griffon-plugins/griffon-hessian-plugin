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
package org.codehaus.griffon.runtime.hessian;

import griffon.core.env.Metadata;
import griffon.plugins.hessian.BurlapClient;
import griffon.plugins.hessian.BurlapClientFactory;
import griffon.plugins.monitor.MBeanManager;
import org.codehaus.griffon.runtime.jmx.BurlapClientMonitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;

import static griffon.util.ConfigUtils.getConfigValue;
import static griffon.util.GriffonNameUtils.isBlank;
import static griffon.util.GriffonNameUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class DefaultBurlapClientFactory implements BurlapClientFactory {
    @Inject
    private Metadata metadata;

    @Inject
    private MBeanManager mbeanManager;

    @Nonnull
    public BurlapClient create(@Nonnull Map<String, Object> params, @Nullable String id) {
        requireNonNull(params, "Argument 'params' must not be null");
        String url = getConfigValue(params, "url");
        requireNonBlank(url, "Parameter 'url' must not be blank");

        DefaultBurlapClient delegate = new DefaultBurlapClient(url);
        if (!isBlank(id)) {
            BurlapClientMonitor monitor = new BurlapClientMonitor(metadata, delegate, id);
            JMXAwareBurlapClient rmiClient = new JMXAwareBurlapClient(delegate);
            rmiClient.addObjectName(mbeanManager.registerMBean(monitor, false).getCanonicalName());
            return rmiClient;
        }
        return delegate;
    }

    @Override
    public void destroy(@Nonnull BurlapClient client) {
        requireNonNull(client, "Argument 'client' must not be null");
        System.out.println("DESTROY "+client);
        if (client instanceof JMXAwareBurlapClient) {
            ((JMXAwareBurlapClient) client).disposeMBeans();
        }
    }
}