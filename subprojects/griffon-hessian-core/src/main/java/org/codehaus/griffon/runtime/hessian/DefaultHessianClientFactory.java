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
import griffon.annotations.core.Nullable;
import griffon.core.env.Metadata;
import griffon.plugins.hessian.HessianClient;
import griffon.plugins.hessian.HessianClientFactory;
import griffon.plugins.monitor.MBeanManager;
import org.codehaus.griffon.runtime.hessian.monitor.HessianClientMonitor;

import javax.inject.Inject;
import java.util.Map;

import static griffon.util.ConfigUtils.getConfigValue;
import static griffon.util.GriffonNameUtils.isBlank;
import static griffon.util.GriffonNameUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class DefaultHessianClientFactory implements HessianClientFactory {
    @Inject
    private Metadata metadata;

    @Inject
    private MBeanManager mbeanManager;

    @Nonnull
    public HessianClient create(@Nonnull Map<String, Object> params, @Nullable String id) {
        requireNonNull(params, "Argument 'params' must not be null");
        String url = getConfigValue(params, "url");
        requireNonBlank(url, "Parameter 'url' must not be blank");

        DefaultHessianClient delegate = new DefaultHessianClient(url);
        if (!isBlank(id)) {
            HessianClientMonitor monitor = new HessianClientMonitor(metadata, delegate, id);
            JMXAwareHessianClient rmiClient = new JMXAwareHessianClient(delegate);
            rmiClient.addObjectName(mbeanManager.registerMBean(monitor, false).getCanonicalName());
            return rmiClient;
        }
        return delegate;
    }

    @Override
    public void destroy(@Nonnull HessianClient client) {
        requireNonNull(client, "Argument 'client' must not be null");
        if (client instanceof JMXAwareHessianClient) {
            unregisterMBeans((JMXAwareHessianClient) client);
        }
    }

    private void unregisterMBeans(@Nonnull JMXAwareHessianClient client) {
        for (String objectName : client.getObjectNames()) {
            mbeanManager.unregisterMBean(objectName);
        }
        client.clearObjectNames();
    }
}