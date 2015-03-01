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
package org.codehaus.griffon.runtime.jmx;

import griffon.core.env.Metadata;
import griffon.plugins.hessian.HessianClient;
import org.codehaus.griffon.runtime.monitor.AbstractMBeanRegistration;

import javax.annotation.Nonnull;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class HessianClientMonitor extends AbstractMBeanRegistration implements HessianClientMonitorMXBean {
    private HessianClient delegate;
    private final String name;

    public HessianClientMonitor(@Nonnull Metadata metadata, @Nonnull HessianClient delegate, @Nonnull String name) {
        super(metadata);
        this.delegate = requireNonNull(delegate, "Argument 'delegate' must not be null");
        this.name = name;
    }

    @Override
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        return new ObjectName("griffon.plugins.rmi:type=HessianClient,application=" + metadata.getApplicationName() + ",name=" + this.name);
    }

    @Override
    public void postDeregister() {
        delegate = null;
        super.postDeregister();
    }

    @Override
    public ProxyInfo[] getProxyDetails() {
        Map<String, Object> services = delegate.getProxies();
        ProxyInfo[] data = new ProxyInfo[services.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : services.entrySet()) {
            data[i++] = new ProxyInfo(entry.getKey(), entry.getValue().getClass().getName());
        }
        return data;
    }

    @Override
    public int getProxyCount() {
        return delegate.getProxies().size();
    }
}