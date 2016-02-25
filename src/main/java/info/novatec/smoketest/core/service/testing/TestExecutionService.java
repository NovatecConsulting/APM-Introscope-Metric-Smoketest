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

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Strings;
import com.google.inject.Injector;
import info.novatec.smoketest.core.SmokeTestConfiguration;
import info.novatec.smoketest.core.application.Environment;
import org.testng.IObjectFactory2;
import org.testng.IReporter;
import org.testng.TestNG;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * TestNG implementation of {@link ITestExecutionService}.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class TestExecutionService implements ITestExecutionService {

    /**
     * The very last fallback output directory name.
     */
    private static final String DEFAULT_OUTPUT_DIRECTORY = "test-results";

    /**
     * The Gucie Injector which is used to enable dependency injection to test classes.
     */
    private Injector injector;

    /**
     * The running {@link Environment}.
     */
    private Environment<?> environment;

    /**
     * The effective {@link SmokeTestConfiguration}.
     */
    private SmokeTestConfiguration configuration;

    /**
     * The {@link TestNG} instance.
     */
    private TestNG testNG;

    /**
     * The list of all applied {@link IReporter}s.
     */
    private List<IReporter> reporters;

    /**
     * The output directory name.
     */
    private String outputDirectory = DEFAULT_OUTPUT_DIRECTORY;


    /**
     * Creates a new  TestExecutionService.
     *
     * @param injector
     *         The Gucie {@link Injector}
     * @param environment
     *         The {@link Environment}
     * @param configuration
     *         The {@link SmokeTestConfiguration}
     */
    @Inject
    public TestExecutionService(final Injector injector,
                                final Environment environment,
                                final SmokeTestConfiguration configuration) {
        this.injector = injector;
        this.environment = environment;
        this.configuration = configuration;
        this.reporters = Lists.newArrayList();
    }

    //-------------------------------------------------------------
    // Interface Implementation: ITestExecutionService
    //-------------------------------------------------------------

    @Override
    public void execute() {
        execute(environment.getTests());
    }

    @Override
    public void execute(Class<?>... tests) {
        execute(Arrays.asList(tests));
    }

    @Override
    public void execute(List<Class<?>> tests) {
        initializeOutputDirectory();
        initializeTestNG();
        testNG.setTestClasses(tests.toArray(new Class[tests.size()]));
        testNG.run();
    }

    @Override
    public void addReporter(Class<? extends IReporter> clazz) {
        addReporter(environment.getInjector().getInstance(clazz), false);
    }

    @Override
    public void addReporter(IReporter reporter) {
        addReporter(reporter, true);
    }

    @Override
    public void setReportDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    //-------------------------------------------------------------
    // Methods: Internals
    //-------------------------------------------------------------

    /**
     * Actually adds the reporter. If the the reporter was not instantiated with the Injector, the dependency injection
     * are injected
     *
     * @param reporter
     *         The reporter to be added
     * @param performInjection
     *         Flag to indicate if dependencies should be injected
     */
    private void addReporter(final IReporter reporter,
                             boolean performInjection) {
        if (performInjection) {
            this.environment.getInjector().injectMembers(reporter);
        }
        this.reporters.add(reporter);
    }

    /**
     * Set the output directory.
     */
    private void initializeOutputDirectory() {
        if (Strings.isNullOrEmpty(outputDirectory)) {
            if (!Strings.isNullOrEmpty(configuration.getReportDirectory())) {
                outputDirectory = configuration.getReportDirectory();
            } else {
                outputDirectory = DEFAULT_OUTPUT_DIRECTORY;
            }
        }
    }

    /**
     * Initializes the TestNG test suite.
     */
    private void initializeTestNG() {
        if (testNG == null) {
            testNG = new TestNG(false);
            testNG.setDefaultSuiteName(environment.getName());
            testNG.setDefaultTestName("Unnamed (@Test missing)");
            testNG.setOutputDirectory(outputDirectory);
            //Add all reports as listener to the testNG engine
            reporters.forEach(reporter -> testNG.addListener(reporter));
            //We enable dependency injection to test classes by defining an IObjectFactory2
            //which instantiates all test classes by using the Gucie Injector
            testNG.setObjectFactory((IObjectFactory2) clazz -> this.injector.getInstance(clazz));
        }
    }

}
