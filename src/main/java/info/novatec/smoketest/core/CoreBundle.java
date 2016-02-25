/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 NovaTec Consulting GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package info.novatec.smoketest.core;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import info.novatec.smoketest.core.application.Bundle;
import info.novatec.smoketest.core.application.Environment;
import info.novatec.smoketest.core.application.Setup;
import info.novatec.smoketest.core.model.IMetricDefinition;
import info.novatec.smoketest.core.model.IMetricTestResult;
import info.novatec.smoketest.core.service.query.IMetricDataCollector;
import info.novatec.smoketest.core.service.query.NoOpDataCollector;
import info.novatec.smoketest.core.service.testing.ITestExecutionService;
import info.novatec.smoketest.core.service.testing.TestExecutionService;
import info.novatec.smoketest.core.service.time.ITimeService;
import info.novatec.smoketest.core.service.time.TimeService;

/**
 * This bundle provides services and command line arguments for all core services.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class CoreBundle extends Bundle<SmokeTestConfiguration> {

    /**
     * Adds command line options for configuration files and configuration overrides.
     *
     * @param setup
     *         The {@link Setup.Builder}
     */
    @Override
    public void setup(Setup.Builder<SmokeTestConfiguration> setup) {
        setup.getParser()
                .addArgument("-c", "--configuration")
                .nargs("?")
                .help("Possibility to provide a configuration file!");

        setup.getParser()
                .addArgument("-o", "--override")
                .nargs("+")
                .help("Values to be overwritten in configuration! E.g -d myProperty=Value");
    }

    @Override
    public void configure(Environment.Builder<SmokeTestConfiguration> builder) {
        //Install the core service
        builder.services(new CoreServices());
    }

    /**
     * Internal {@link com.google.inject.Module} providing service bindings.
     */
    private static class CoreServices extends AbstractModule {
        @Override
        public void configure() {
            bind(ITestExecutionService.class).to(TestExecutionService.class).in(Singleton.class);
            bind(ITimeService.class).to(TimeService.class).in(Singleton.class);

            //bind NoOp default query service
            bind(new TypeLiteral<IMetricDataCollector<IMetricDefinition, IMetricTestResult>>() {
            }).to(NoOpDataCollector.class);
        }
    }
}
