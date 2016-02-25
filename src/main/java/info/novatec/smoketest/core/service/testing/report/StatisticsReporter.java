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

package info.novatec.smoketest.core.service.testing.report;

import info.novatec.smoketest.core.SmokeTestConfiguration;
import info.novatec.smoketest.core.model.TestLevel;
import info.novatec.smoketest.core.service.time.ITimeService;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;

import javax.inject.Inject;
import java.util.List;

/**
 * Base class for reporters which are intended to provide reports as POJOs. The default report available created by this
 * reporter is {@link StatisticsReport}.
 *
 * @param <T>
 *         The configuration type. Must be subclass of {@link SmokeTestConfiguration}.
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 * @see StatisticsReport
 */
public class StatisticsReporter<T extends SmokeTestConfiguration> implements IReporter {

    /**
     * The {@link ITimeService}.
     */
    @Inject
    private ITimeService timeService;

    /**
     * The effective configuration of type <T>.
     */
    private T configuration;

    /**
     * List of all executed {@link XmlSuite}s.
     */
    private List<XmlSuite> xmlSuites;

    /**
     * List of all executed {@link ISuite}s.
     */
    private List<ISuite> suites;

    /**
     * The output directory.
     */
    private String outputDirectory;

    /**
     * Count of all executed tests.
     */
    private int executedTests;

    /**
     * Count of failed tests.
     */
    private int failedTests;

    /**
     * Count of skipped tests.
     */
    private int skippedTests;

    /**
     * Count of passed tests.
     */
    private int passedTests;

    @Override
    public final void generateReport(List<XmlSuite> xmlSuites,
                                     List<ISuite> suites,
                                     String outputDirectory) {
        this.xmlSuites = xmlSuites;
        this.suites = suites;
        this.outputDirectory = outputDirectory;
        for (ISuite suite : suites) {
            for (ISuiteResult iSuiteResult : suite.getResults().values()) {
                ITestContext testContext = iSuiteResult.getTestContext();
                executedTests++;
                failedTests += testContext.getFailedTests().size();
                skippedTests += testContext.getSkippedTests().size();
                passedTests += testContext.getPassedTests().size();
            }
        }
        report(createReport());
    }

    /**
     * Triggers subclasses to perform the report. Subclasses retrieve a StatisticsReport but are encourage to enhance
     * the report with additional information.
     *
     * @param statistics
     *         The {@link StatisticsReport}
     */
    protected void report(StatisticsReport statistics) {
        //To be overridden
    }

    /**
     * Creates a default {@link StatisticsReport}.
     *
     * @return The StatisticsReport
     */
    protected StatisticsReport createReport() {
        return new StatisticsReport(TestLevel.LEVEL_0,
                timeService.getTimeRange(),
                getExecutedTests(),
                getFailedTests(),
                getSkippedTests(),
                getPassedTests()
        );
    }

    /**
     * @return The {@link SmokeTestConfiguration}
     */
    public T getConfiguration() {
        return configuration;
    }

    //-------------------------------------------------------------
    // Getter
    //-------------------------------------------------------------

    @Inject
    @SuppressWarnings("unchecked")//Safe cast
    private void setConfiguration(final SmokeTestConfiguration configuration) {
        this.configuration = (T) configuration;
    }

    /**
     * Gets {@link #timeService}.
     *
     * @return {@link #timeService}
     */
    public ITimeService getTimeService() {
        return timeService;
    }

    /**
     * Gets {@link #xmlSuites}.
     *
     * @return {@link #xmlSuites}
     */
    public List<XmlSuite> getXmlSuites() {
        return xmlSuites;
    }

    /**
     * Gets {@link #suites}.
     *
     * @return {@link #suites}
     */
    public List<ISuite> getSuites() {
        return suites;
    }

    /**
     * Gets {@link #outputDirectory}.
     *
     * @return {@link #outputDirectory}
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * Gets {@link #executedTests}.
     *
     * @return {@link #executedTests}
     */
    public int getExecutedTests() {
        return executedTests;
    }

    /**
     * Gets {@link #failedTests}.
     *
     * @return {@link #failedTests}
     */
    public int getFailedTests() {
        return failedTests;
    }

    /**
     * Gets {@link #skippedTests}.
     *
     * @return {@link #skippedTests}
     */
    public int getSkippedTests() {
        return skippedTests;
    }

    /**
     * Gets {@link #passedTests}.
     *
     * @return {@link #passedTests}
     */
    public int getPassedTests() {
        return passedTests;
    }
}
