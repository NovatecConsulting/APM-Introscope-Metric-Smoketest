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

import info.novatec.smoketest.core.SmokeTestConfiguration;
import info.novatec.smoketest.core.application.configuration.Configuration;
import info.novatec.smoketest.core.model.*;
import info.novatec.smoketest.core.model.MetricTest;
import info.novatec.smoketest.core.model.validation.IValidationRule;
import info.novatec.smoketest.core.model.validation.ValidationException;
import info.novatec.smoketest.core.model.validation.ValidationResult;
import info.novatec.smoketest.core.service.collector.IMetricDataCollector;
import info.novatec.smoketest.core.service.collector.MetricDataCollectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.Set;

/**
 * Generic abstract base class for all smoke test classes. It is not mandatory but test developers are encouraged to
 * extend this class for building the smoke test classes.
 *
 * @param <IN>
 *         The type of IMetricDefinition
 * @param <OUT>
 *         The type of IMetricTestResult
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public abstract class BaseTest<IN extends IMetricDefinition, OUT extends IMetricTestResult> {

    /**
     * The slf4j logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

    /**
     * Constant to access the {@link info.novatec.smoketest.core.model.MetricTestResultSet} within the attributes of a
     * the TestNG reporter.
     */
    private static final String METRIC_TEST_RESULT_SET_REPORTER_ENTRY = "metricTestResultSet";

    /**
     * Constant defines the name of the TestNG data provider.
     */
    private static final String DATA_PROVIDER_NAME = "smokeTestData";

    /**
     * The {@link IMetricDataCollector} which is used to collect for metrics.
     */
    @Inject
    private IMetricDataCollector<IN, OUT> collector;

    /**
     * The {@link Configuration} for this test. This is at least of type {@link SmokeTestConfiguration}
     */
    @Inject
    private SmokeTestConfiguration configuration;

    /**
     * The TestNG test context.
     */
    private ITestContext context;

    /**
     * No-args Constructor.
     */
    public BaseTest() {
    }

    //-------------------------------------------------------------
    // Methods: Test execution
    //-------------------------------------------------------------

    /**
     * Retrieves the {@link ITestContext} from TestNG. This is possible because TestNG supports injecting
     * <code>Method</code>,<code>ITestContext</code>,<code>XmlTest</code> and <code>ITestResult</code>.
     *
     * @param context
     *         The TestNG ITestContext
     * @see ITestContext
     */
    @BeforeTest
    public void injectContext(ITestContext context) {
        this.context = context;
    }

    /**
     * <p> Executes one MetricTest. To avoid writing test for each metric, this test is served by a data
     * provider method ({@link #dataProvider(ITestContext)}. Thus it is possible to do all tests using this method. </p>
     * <p> The method first check if the {@link MetricTest} has the correct level {@link
     * MetricTest#getLevel()} to be executed by invoking {@link #isInScope(TestLevel)}. If not, the test is
     * marked as skipped by throwing a SkipException.<br> If it is in scope the {@link IMetricDataCollector} is utilized
     * to collect the required information.<br> As last step the provided {@link IValidationRule}s are used to validate
     * the collect results. </p>
     *
     * @param metricTest
     *         The {@link MetricTest} to be executed
     * @throws ValidationException
     *         if validation fails
     * @throws SkipException
     *         if the test is out of scope
     * @see MetricTest
     * @see IValidationRule
     * @see TestLevel
     */
    @Test(dataProvider = DATA_PROVIDER_NAME)
    public final void testMetric(final MetricTest<IN, OUT> metricTest) {
        if (isInScope(metricTest.getLevel())) {
            MetricTestResultSet<IN, OUT> resultSet;
            try {
                //Start the data collection
                resultSet = collector.collect(metricTest.getMetric());
            } catch (MetricDataCollectorException ex) {
                //Since we are running as TestNG Test this exceptions won't be logged.
                //This is only visible in the test result html output. But if no HTML reporter us defined
                //this exception is swallowed.
                //Practically it is bad practice to log exceptions in catch block but in this special case
                //it might be okay
                //LOGGER.warn(Throwables.getStackTraceAsString(ex));
                throw ex;
            }
            //Provide the result set as attribute to the testNG reporter to make it available to reporters
            Reporter.getCurrentTestResult().setAttribute(METRIC_TEST_RESULT_SET_REPORTER_ENTRY, resultSet);
            //Next step is to validate the result set,
            if (metricTest.getValidations() != null && metricTest.getValidations().size() > 0) {
                for (IValidationRule<IN, OUT> rule : metricTest.getValidations()) {
                    ValidationResult validationResult = rule.apply(resultSet);
                    if (!validationResult.isValid()) {
                        LOGGER.warn("Failed: \"{}\" Reason: {}", metricTest.getMetric().
                                getFullQualifiedName(), validationResult.getMessage());
                        throw new ValidationException(validationResult.getMessage());
                    }
                }
                LOGGER.info("Passed: \"{}\"", metricTest.getMetric().getFullQualifiedName());
            } else {
                LOGGER.info("Passed (No validation!): \"{}\"", metricTest.getMetric().getFullQualifiedName());
            }
        } else {
            //Although out of scope, provide an empty result set to the reporter.
            Reporter.getCurrentTestResult().setAttribute(METRIC_TEST_RESULT_SET_REPORTER_ENTRY,
                    new MetricTestResultSet<>(metricTest.getMetric()));
            LOGGER.info("Skipped: \"{}\"", metricTest.getMetric()
                    .getFullQualifiedName());
            throw new SkipException("Test ist out of scope!");
        }
    }

    /**
     * <b> Creates the dataProvider for the {@link #testMetric(MetricTest)} method. A data provider is always
     * a 2 dimensional Arrays. The first dimension is the count how often the test method will be invoked and the second
     * dimension are the actual input parameters. </b> <b> To ease the usage of data providers the {@link
     * #toDataProvider(Set)} utility method is provided. </b>
     *
     * @param testContext
     *         The TestNG ITestContext
     * @return A two dimensional Object array.
     */
    @DataProvider(name = DATA_PROVIDER_NAME)
    protected Object[][] dataProvider(final ITestContext testContext) {
        return toDataProvider(getMetricTests());
    }

    /**
     * Provides the set of {@link MetricTest}s which this test should execute. This method is used to provide
     * initialize the data provider which is used by the dataProvider method.
     *
     * @return Set of MetricTest
     */
    protected abstract Set<MetricTest<IN, OUT>> getMetricTests();

    /**
     * Utility method to transform a set of {@link MetricTest}s into an Object array. This is needed to
     * utilize the DataProvider capabilities of Junit/TestNG.
     *
     * @param metricTests
     *         The queries which shall be used test input
     * @return A data provider conform object array.
     */
    protected Object[][] toDataProvider(final Set<MetricTest<IN, OUT>> metricTests) {
        int counter = 0;
        Object[][] dataProvider = new Object[metricTests.size()][];
        LOGGER.debug("{} will collect: ", getTestName());
        for (MetricTest test : metricTests) {
            LOGGER.debug(test.getMetric().getFullQualifiedName());
            dataProvider[counter++] = new Object[]{test};
        }
        return dataProvider;
    }

    /**
     * @return The name of the current test
     */
    protected String getTestName() {
        return context.getName();
    }

    /**
     * @return The current valid {@link ITestContext}
     */
    protected ITestContext getTestContext() {
        return context;
    }

    /**
     * Checks if current test is in scope of the smoke test configuration.
     *
     * @param level
     *         The {@link TestLevel}
     * @return true if level is contains in the level set in the configuration, false otherwise
     */
    private boolean isInScope(TestLevel level) {
        return configuration.getTestLevel().contains(level);
    }
}