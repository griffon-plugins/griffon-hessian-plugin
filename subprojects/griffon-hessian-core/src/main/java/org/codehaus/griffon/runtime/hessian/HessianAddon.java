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
package org.codehaus.griffon.runtime.hessian;

import griffon.annotations.core.Nonnull;
import griffon.core.GriffonApplication;
import griffon.core.env.Metadata;
import griffon.plugins.hessian.BurlapClientStorage;
import griffon.plugins.hessian.BurlapHandler;
import griffon.plugins.hessian.HessianClientStorage;
import griffon.plugins.hessian.HessianHandler;
import griffon.plugins.monitor.MBeanManager;
import org.codehaus.griffon.runtime.core.addon.AbstractGriffonAddon;
import org.codehaus.griffon.runtime.hessian.monitor.BurlapClientStorageMonitor;
import org.codehaus.griffon.runtime.hessian.monitor.HessianClientStorageMonitor;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andres Almiray
 */
@Named("hessian")
public class HessianAddon extends AbstractGriffonAddon {
    @Inject
    private HessianClientStorage hessianClientStorage;

    @Inject
    private BurlapClientStorage burlapClientStorage;

    @Inject
    private BurlapHandler burlapHandler;

    @Inject
    private HessianHandler hessianHandler;

    @Inject
    private MBeanManager mbeanManager;

    @Inject
    private Metadata metadata;

    @Override
    public void init(@Nonnull GriffonApplication application) {
        mbeanManager.registerMBean(new HessianClientStorageMonitor(metadata, hessianClientStorage));
        mbeanManager.registerMBean(new BurlapClientStorageMonitor(metadata, burlapClientStorage));
    }

    @Override
    public void onShutdown(@Nonnull GriffonApplication application) {
        for (String clientId : hessianClientStorage.getKeys()) {
            hessianHandler.destroyHessianClient(clientId);
        }
        for (String clientId : burlapClientStorage.getKeys()) {
            burlapHandler.destroyBurlapClient(clientId);
        }
    }
}
