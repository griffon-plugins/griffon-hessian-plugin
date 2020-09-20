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
package griffon.plugins.hessian;

import griffon.test.core.GriffonUnitRule;
import griffon.test.core.TestFor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@TestFor(BurlapCalculatorService.class)
public class BurlapCalculatorServiceTest {
    private static final HttpServer SERVER = HttpServer.of("/burlap", new BurlapCalculator());

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
    }

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule();
    private BurlapCalculatorService service;

    @Test
    public void addTwoNumbers() {
        // when:
        double result = service.calculate(21d, 21d);

        // then:
        assertNotNull(result);
        assertEquals(42d, result, 0d);
    }

    @BeforeClass
    public static void setup() throws Exception {
        SERVER.start();
    }

    @AfterClass
    public static void cleanup() throws Exception {
        SERVER.close();
    }
}
