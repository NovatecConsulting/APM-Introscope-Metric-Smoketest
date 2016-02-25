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

package info.novatec.smoketest.core.application.configuration;

import info.novatec.smoketest.core.util.Configurations;

/**
 * <p> Base class for all configurations.The final configuration can be created in different ways. The first step is to
 * choose the correct root configuration which means the base class for the new configuration. Since different bundles
 * might depend on different bundles it needs to be assured that all configurations are compatible. </p> <p> To create a
 * configuration, it is recommended to us the {@link IConfigurationFactory}. The factory provides different ways to
 * create configurations. In the scope of this application the IConfigurationFactory is available, replaceable in the
 * {@link info.novatec.smoketest.core.application.Setup} component. </p>
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 * @see info.novatec.smoketest.core.application.Setup
 * @see IConfigurationFactory
 */
public abstract class Configuration {

    @Override
    public String toString() {
        return Configurations.toString(this);
    }
}
