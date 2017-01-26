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


import griffon.plugins.hessian.HessianClient;
import griffon.plugins.hessian.HessianClientCallback;
import griffon.plugins.hessian.HessianClientFactory;
import griffon.plugins.hessian.HessianClientStorage;
import griffon.plugins.hessian.HessianHandler;
import griffon.plugins.hessian.exceptions.HessianException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;

import static griffon.util.GriffonNameUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class DefaultHessianHandler implements HessianHandler {
    private static final String ERROR_PARAMS_NULL = "Argument 'params' must not be null";
    private static final String ERROR_CALLBACK_NULL = "Argument 'callback' must not be null";
    private static final String KEY_ID = "id";
    private static final String ERROR_CLIENTID_BLANK = "Argument 'clientId' must not be blank";

    private final HessianClientFactory hessianClientFactory;
    private final HessianClientStorage hessianClientStorage;

    @Inject
    public DefaultHessianHandler(@Nonnull HessianClientFactory hessianClientFactory, @Nonnull HessianClientStorage hessianClientStorage) {
        this.hessianClientFactory = hessianClientFactory;
        this.hessianClientStorage = hessianClientStorage;
    }

    @Nullable
    @Override
    public <R> R withHessian(@Nonnull Map<String, Object> params, @Nonnull HessianClientCallback<R> callback) throws HessianException {
        requireNonNull(callback, ERROR_CALLBACK_NULL);
        try {
            HessianClient client = getHessianClient(params);
            return callback.handle(params, client);
        } catch (Exception e) {
            throw new HessianException("An error occurred while executing REST call", e);
        }
    }

    @Override
    public void destroyHessianClient(@Nonnull String clientId) {
        requireNonBlank(clientId, ERROR_CLIENTID_BLANK);
        HessianClient client = hessianClientStorage.get(clientId);
        try {
            if (client != null) {
                hessianClientFactory.destroy(client);
            }
        } finally {
            hessianClientStorage.remove(clientId);
        }
    }


    @Nonnull
    private HessianClient getHessianClient(@Nonnull Map<String, Object> params) {
        requireNonNull(params, ERROR_PARAMS_NULL);
        if (params.containsKey(KEY_ID)) {
            String id = String.valueOf(params.remove(KEY_ID));
            HessianClient client = hessianClientStorage.get(id);
            if (client == null) {
                client = hessianClientFactory.create(params, id);
                hessianClientStorage.set(id, client);
            }
            return client;
        }
        return hessianClientFactory.create(params, null);
    }
}
