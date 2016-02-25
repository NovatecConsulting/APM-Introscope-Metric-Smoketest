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

package info.novatec.smoketest.core.application;

import info.novatec.smoketest.core.application.configuration.Configuration;

/**
 * <p> Defines a set of functionality to be executed within an {@link Environment}. To enable a bundle it needs to be
 * added to {@link Setup}. While bootstrapping an application a bundle runs through three phases. </p> <p> In the first
 * phase the bundle has the possibility to modify the Setup which is in a later step used to create the final
 * environment. This means core components can be provided, exchanged to influence the further environment creation.
 * Also the CLI can be enhanced to have additional parameters this bundle might utilize. The modifications are done via
 * {@link Bundle#setup(Setup.Builder)}. </p> <p> In the second phase the bundle has the chance to modify the environment
 * itself. This means that this bundle can add tests, provide/override service as Guice Module. At this point the
 * configuration is available and might be used for further processing. </p> <p> In the last phase the bundle is run. At
 * this point the Environment is available and this bundle already got all dependencies injected. </p>
 *
 * @param <T>
 *         The type of the related {@link Configuration}.
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 * @see Environment
 * @see Setup
 * @see Bundle
 */
public abstract class Bundle<T extends Configuration> {

    /**
     * Gets the name of the bundle. By default  <code>getClass().getName()</code> is used.
     *
     * @return The name of the bundle.
     */
    public String getName() {
        return getClass().getName();
    }

    /**
     * First phase of a bundles life cycle. In this phase to bundle can utilizes the {@link Setup.Builder} to
     * reconfigure the Setup. Possible changes are for example adding bundles, providing a programmatically created
     * configuration or replacing the
     * {@link info.novatec.smoketest.core.application.configuration.IConfigurationFactory}.
     *
     * @param builder
     *         The {@link Setup.Builder} to configure the Setup
     * @see Setup.Builder
     */
    public void setup(Setup.Builder<T> builder) {
        //NOP
    }

    /**
     * In the second phase of a bundle's life cycle, bundles are in charge to configure the {@link Environment}. This is
     * achieved by using the {@link Environment.Builder}. It is possible to define test classes as well as {@link
     * com.google.inject.Module}s to provide additional services to the application. Also, existing services might be
     * overwritten.
     *
     * @param builder
     *         The {@link Environment.Builder} to configure the Setup
     * @see info.novatec.smoketest.core.application.Environment.Builder
     */
    public void configure(Environment.Builder<T> builder) {
        //NOP
    }

    /**
     * In the last phase of a bundle'S life cycle it is actually executed. At this point all services are available. It
     * this bundles needs any additional services, they are now accessible from the Environment or, if defined, the
     * corresponding members are already injected.
     *
     * @param environment
     *         The ready {@link Environment}
     */
    public void run(Environment<T> environment) {
        //NOP
    }
}
