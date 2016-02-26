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

import info.novatec.smoketest.core.application.Bundle;
import info.novatec.smoketest.core.application.Environment;
import info.novatec.smoketest.core.application.Setup;
import info.novatec.smoketest.core.application.configuration.Configuration;
import info.novatec.smoketest.core.service.testing.ITestExecutionService;

import javax.inject.Inject;

/**
 * <p> Abstract base class for smoke tests. This class ensures a proper instantiation of {@link Setup} and {@link
 * Environment}. Additionally, the environment is run and all provided tests are executed with the {@link
 * ITestExecutionService}.<br>
 * SmokeTest is itself a Bundle and added to Setup as root bundle, thus subclasses can  override methods of
 * {@link Bundle} and add additional functionality. </p>
 * <p>
 * The example demonstrates how an  exemplary smoke test is created.
 * The kind of {@link Configuration} is provided as type parameter to the SmokeTest class. As can be
 * seen the example adds a {@link CoreBundle} and a {@link info.novatec.smoketest.introscope.IntroscopeBundle} in the
 * {@link Bundle#setup(Setup.Builder)} method. The first provides services which are needed by for a proper {@link
 * Environment} setup. The latter adds introscope specific implementations for all components which are only defined but
 * not implemented in the core application code.<br> The test which shall be executed is provided in the {@link
 * Bundle#configure(Environment.Builder)} method. </p>
 * <pre>
 * {@code
 *  public class MySmokeTest extends SmokeTest<IntroscopeConfiguration> {
 *
 *      {@literal @}Override
 *      public void setup(Setup.Builder<IntroscopeConfiguration> builder) {
 *          builder.addBundle(new CoreBundle())
 *                 .addBundle(new IntroscopeBundle());
 *      }
 *
 *      {@literal @}Override
 *      public void configure(Environment.Builder<IntroscopeConfiguration> builder) {
 *          builder.tests(SimpleTest.class);
 *      }
 *
 *      public static void main(String[] args) {
 *          new MySmokeTest().start(args);
 *      }
 *  }
 * }
 * </pre>
 *
 * @param <T>
 *         The type of the required {@link Configuration}.
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 * @see Bundle
 * @see Setup
 * @see Environment
 */
public abstract class SmokeTest<T extends Configuration> extends Bundle<T> {

    /**
     * The {@link ITestExecutionService}.
     */
    @Inject
    private ITestExecutionService executor;

    @Override
    public void run(Environment<T> environment) {
        executor.execute();
    }

    /**
     * Starts the smoke test by creating a {@link Setup}, the resulting {@link Environment} and runs the Environment.
     *
     * @param args
     *         The command line arguments
     */
    public final void start(final String... args) {
        Setup<T> setup = new Setup.Builder<>(this)
                .build(args);
        if (setup != null) {
            Environment<T> environment = new Environment.Builder<>(setup)
                    .build();
            if (environment != null) {
                environment.run();
            }
        }
    }
}
