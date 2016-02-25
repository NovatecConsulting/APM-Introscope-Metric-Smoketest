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

package info.novatec.smoketest.core.service.testing;

import org.testng.IReporter;

import java.util.List;

/**
 * Service which executes the smoke test.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public interface ITestExecutionService {

    /**
     * Executes all tests available in the {@link info.novatec.smoketest.core.application.Environment}.
     */
    void execute();

    /**
     * Method invokes the test execution.
     *
     * @param tests
     *         The test classes to be executed
     */
    void execute(final List<Class<?>> tests);

    /**
     * Method invokes the test execution.
     *
     * @param tests
     *         The test classes to be executed
     */
    void execute(final Class<?>... tests);

    /**
     * Method adds a {@link IReporter} to the test engine. Be aware that dependency injection to reporters is possible.
     *
     * @param reporter
     *         The reporter to be added
     * @see IReporter
     */
    void addReporter(final IReporter reporter);

    /**
     * Method adds a {@link IReporter} to the test engine. Be aware that dependency injection to reporters is possible.
     *
     * @param clazz
     *         The reporter clazz to be instantiated to be added
     * @see IReporter
     */
    void addReporter(final Class<? extends IReporter> clazz);

    /**
     * Sets the output directory for all reports. If nothing is explicitly provided the default is taken from {@link
     * info.novatec.smoketest.core.SmokeTestConfiguration}.
     *
     * @param outputDirectory
     *         The outputDirectory directory
     */
    void setReportDirectory(final String outputDirectory);
}
