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

package example;


import info.novatec.smoketest.core.CoreBundle;
import info.novatec.smoketest.core.SmokeTest;
import info.novatec.smoketest.core.application.Environment;
import info.novatec.smoketest.core.application.Setup;
import info.novatec.smoketest.core.model.MetricTest;
import info.novatec.smoketest.core.model.TestLevel;
import info.novatec.smoketest.core.model.validation.ValidationRules;
import info.novatec.smoketest.core.service.testing.BaseTest;
import info.novatec.smoketest.core.service.testing.ITestExecutionService;
import info.novatec.smoketest.introscope.IntroscopeBundle;
import info.novatec.smoketest.introscope.IntroscopeConfiguration;
import info.novatec.smoketest.introscope.IntroscopeMetric;
import info.novatec.smoketest.introscope.IntroscopeMetricTestResult;
import info.novatec.smoketest.introscope.IntroscopeMetricTestBuilder;
import org.testng.annotations.Test;
import org.uncommons.reportng.HTMLReporter;

import javax.inject.Inject;
import java.util.Set;

/**
 * <strong>
 * To run this example it is mandatory that EPAgent.jar and IntroscopeJDBC are
 * available in the classpath! Currently it is tested with version 9.6.0.0.
 * </strong>
 * Example implementation of a smoke test.
 * <p>
 * This class defines two additional classes:<br>
 * <ul>
 * <li>MyConfiguration</li>
 * This class defines the {@link info.novatec.smoketest.core.application.configuration.Configuration} object
 * which is related to this smoke test.<br>
 * {@link MyConfiguration} extends {@link IntroscopeConfiguration} to enable the Introscope functionality.
 * It is mandatory to provide this class as generic type parameter to to {@link SmokeTest}:
 * <pre>
 *     {@code
 *      class MyIntroscopeSmokeTest extends SmokeTest<MyConfiguration>
 *     }
 * </pre>
 * <li>CheckMetrics</li>
 * This class defines the test to be executed by the smoke test.
 * <p>
 * </ul>
 *
 *
 *
 * @author Claudio Waldvogel
 */
public class MyIntroscopeSmokeTest extends SmokeTest<MyIntroscopeSmokeTest.MyConfiguration> {

    /**
     * The {@link info.novatec.smoketest.core.application.configuration.Configuration} related to this SmokeTests.
     */
    @Inject
    private MyConfiguration myInjectedConfiguration;

    /**
     * The {@link ITestExecutionService}.
     */
    @Inject
    private ITestExecutionService testExecutionService;

    //-------------------------------------------------------------
    // Methods: Override SmokeTest
    //-------------------------------------------------------------

    @Override
    public String getName() {
        return "My Introscope Smoke Test";
    }

    @Override
    public void setup(Setup.Builder<MyConfiguration> builder) {
        //Define all required bundles for this smoke test.
        builder.addBundle(new CoreBundle())
                .addBundle(new IntroscopeBundle());
    }

    @Override
    public void configure(Environment.Builder<MyConfiguration> builder) {
        //Define the tests to be executed
        builder.tests(CheckMetrics.class);
    }

    @Override
    public void run(Environment<MyConfiguration> environment) {
        //Here we check if we should enable the html reporting.
        if (myInjectedConfiguration.isHtmlReportEnabled()) {
            testExecutionService.addReporter(new HTMLReporter());
        }
        super.run(environment);
    }

    /**
     * Starts the smoke test.
     *
     * @param args
     *         The command line arguments
     */
    public static void main(String[] args) {
        new MyIntroscopeSmokeTest().start(args);
    }

    /**
     * The {@link info.novatec.smoketest.core.application.configuration.Configuration} for this smoke test.
     */
    public static class MyConfiguration extends IntroscopeConfiguration {

        /**
         * Flag to indicate if html reporter is enabled.
         */
        private boolean htmlReportEnabled = false;

        /**
         * Gets {#htmlReportEnabled}.
         *
         * @return {#htmlReportEnabled}
         */
        public boolean isHtmlReportEnabled() {
            return htmlReportEnabled;
        }

        /**
         * Sets {@link #htmlReportEnabled}.
         *
         * @param htmlReportEnabled
         *         New value for {@link #htmlReportEnabled}
         */
        public void setHtmlReportEnabled(boolean htmlReportEnabled) {
            this.htmlReportEnabled = htmlReportEnabled;
        }
    }


    /**
     * The actual test implementation.
     */
    @Test(testName = "MyTestToCheckSomeMetrics")
    public static class CheckMetrics extends BaseTest<IntroscopeMetric, IntroscopeMetricTestResult> {

        @Override
        protected Set<MetricTest<IntroscopeMetric, IntroscopeMetricTestResult>> getMetricTests() {
            return new IntroscopeMetricTestBuilder()
                    .single("path|subPath", "Metric1")
                    .single("path|subPath", "Metric2", TestLevel.LEVEL_0, ValidationRules.expectedOccurrences(1))
                    .single("path|subPath", "OutOfScope", TestLevel.LEVEL_1)
                    .build("MyAgent");
        }
    }
}



