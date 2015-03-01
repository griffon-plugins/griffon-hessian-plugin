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
package griffon.plugins.hessian;

import griffon.core.test.GriffonUnitRule;
import griffon.core.test.TestFor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@TestFor(HessianCalculatorService.class)
public class HessianCalculatorServiceTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
    }

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule();

    private static final HttpServer SERVER = HttpServer.of("/hessian", new HessianCalculator());

    private HessianCalculatorService service;

    @BeforeClass
    public static void setup() throws Exception {
        SERVER.start();
    }

    @AfterClass
    public static void cleanup() throws Exception {
        SERVER.close();
    }

    @Test
    public void addTwoNumbers() {
        // when:
        double result = service.calculate(21d, 21d);

        // then:
        assertNotNull(result);
        assertEquals(42d, result, 0d);
    }
}
