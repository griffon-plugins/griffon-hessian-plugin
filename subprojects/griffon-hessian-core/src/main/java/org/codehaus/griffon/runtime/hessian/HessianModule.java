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

import griffon.core.addon.GriffonAddon;
import griffon.core.injection.Module;
import griffon.plugins.hessian.BurlapClientFactory;
import griffon.plugins.hessian.BurlapClientStorage;
import griffon.plugins.hessian.BurlapHandler;
import griffon.plugins.hessian.HessianClientFactory;
import griffon.plugins.hessian.HessianClientStorage;
import griffon.plugins.hessian.HessianHandler;
import org.codehaus.griffon.runtime.core.injection.AbstractModule;
import org.kordamp.jipsy.ServiceProviderFor;

import javax.inject.Named;

/**
 * @author Andres Almiray
 */
@Named("hessian")
@ServiceProviderFor(Module.class)
public class HessianModule extends AbstractModule {
    @Override
    protected void doConfigure() {
        // tag::bindings[]
        bind(HessianClientStorage.class)
            .to(DefaultHessianClientStorage.class)
            .asSingleton();

        bind(HessianClientFactory.class)
            .to(DefaultHessianClientFactory.class)
            .asSingleton();

        bind(BurlapClientStorage.class)
            .to(DefaultBurlapClientStorage.class)
            .asSingleton();

        bind(BurlapClientFactory.class)
            .to(DefaultBurlapClientFactory.class)
            .asSingleton();

        bind(HessianHandler.class)
            .to(DefaultHessianHandler.class)
            .asSingleton();

        bind(BurlapHandler.class)
            .to(DefaultBurlapHandler.class)
            .asSingleton();

        bind(GriffonAddon.class)
            .to(HessianAddon.class)
            .asSingleton();
        // end::bindings[]
    }
}
