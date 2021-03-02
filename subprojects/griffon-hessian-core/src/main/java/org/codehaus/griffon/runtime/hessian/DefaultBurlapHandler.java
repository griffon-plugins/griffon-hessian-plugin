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
import griffon.annotations.core.Nullable;
import griffon.plugins.hessian.BurlapClient;
import griffon.plugins.hessian.BurlapClientCallback;
import griffon.plugins.hessian.BurlapClientFactory;
import griffon.plugins.hessian.BurlapClientStorage;
import griffon.plugins.hessian.BurlapHandler;
import griffon.plugins.hessian.exceptions.BurlapException;
import griffon.plugins.hessian.exceptions.HessianException;

import javax.inject.Inject;
import java.util.Map;

import static griffon.util.GriffonNameUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class DefaultBurlapHandler implements BurlapHandler {
    private static final String ERROR_PARAMS_NULL = "Argument 'params' must not be null";
    private static final String ERROR_CALLBACK_NULL = "Argument 'callback' must not be null";
    private static final String KEY_ID = "id";
    private static final String ERROR_CLIENTID_BLANK = "Argument 'clientId' must not be blank";

    private final BurlapClientFactory burlapClientFactory;
    private final BurlapClientStorage burlapClientStorage;

    @Inject
    public DefaultBurlapHandler(@Nonnull BurlapClientFactory burlapClientFactory, @Nonnull BurlapClientStorage burlapClientStorage) {

        this.burlapClientFactory = burlapClientFactory;
        this.burlapClientStorage = burlapClientStorage;
    }

    @Nullable
    @Override
    public <R> R withBurlap(@Nonnull Map<String, Object> params, @Nonnull BurlapClientCallback<R> callback) throws BurlapException {
        requireNonNull(callback, ERROR_CALLBACK_NULL);
        try {
            BurlapClient client = getBurlapClient(params);
            System.out.println("GET " + client);
            return callback.handle(params, client);
        } catch (Exception e) {
            throw new HessianException("An error occurred while executing SOAP call", e);
        }
    }

    @Override
    public void destroyBurlapClient(@Nonnull String clientId) {
        requireNonBlank(clientId, ERROR_CLIENTID_BLANK);
        BurlapClient client = burlapClientStorage.get(clientId);
        System.out.println("DESTROY " + clientId + " " + client);
        try {
            if (client != null) {
                burlapClientFactory.destroy(client);
            }
        } finally {
            burlapClientStorage.remove(clientId);
        }
    }

    @Nonnull
    private BurlapClient getBurlapClient(@Nonnull Map<String, Object> params) {
        requireNonNull(params, ERROR_PARAMS_NULL);
        if (params.containsKey(KEY_ID)) {
            String id = String.valueOf(params.remove(KEY_ID));
            BurlapClient client = burlapClientStorage.get(id);
            if (client == null) {
                client = burlapClientFactory.create(params, id);
                burlapClientStorage.set(id, client);
            }
            return client;
        }
        return burlapClientFactory.create(params, null);
    }
}
