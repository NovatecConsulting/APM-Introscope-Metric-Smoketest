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

import info.novatec.smoketest.core.model.TestLevel;
import info.novatec.smoketest.core.model.TimeRange;

/**
 * The StatisticsReport contains values which are either available from the {@link
 * info.novatec.smoketest.core.SmokeTestConfiguration} or from core service.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class StatisticsReport {

    /**
     * The {@link TestLevel}.
     */
    private TestLevel level;

    /**
     * The  {@link TestLevel}.
     */
    private TimeRange timeRange;

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

    /**
     * No-Args constructor.
     */
    public StatisticsReport() {
    }

    /**
     * Creates a StatisticsReport.
     *
     * @param level
     *         The TestLevel
     * @param timeRange
     *         The TimeRange
     * @param executedTests
     *         Count of executed tests
     * @param failedTests
     *         Count of failed tests
     * @param skippedTests
     *         Count of skipped tests
     * @param passedTests
     *         Count of passed tests
     */
    public StatisticsReport(TestLevel level,
                            TimeRange timeRange,
                            int executedTests,
                            int failedTests,
                            int skippedTests,
                            int passedTests) {
        this.level = level;
        this.timeRange = timeRange;
        this.executedTests = executedTests;
        this.failedTests = failedTests;
        this.skippedTests = skippedTests;
        this.passedTests = passedTests;
    }

    //-------------------------------------------------------------
    // Get / Set
    //-------------------------------------------------------------


    /**
     * Gets {@link #level}.
     *
     * @return {@link #level}
     */
    public TestLevel getLevel() {
        return level;
    }

    /**
     * Sets {@link #level}.
     *
     * @param level
     *         New value for {@link #level}
     */
    public void setLevel(TestLevel level) {
        this.level = level;
    }

    /**
     * Gets {@link #timeRange}.
     *
     * @return {@link #timeRange}
     */
    public TimeRange getTimeRange() {
        return timeRange;
    }

    /**
     * Sets {@link #timeRange}.
     *
     * @param timeRange
     *         New value for {@link #timeRange}
     */
    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
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
     * Sets {@link #executedTests}.
     *
     * @param executedTests
     *         New value for {@link #executedTests}
     */
    public void setExecutedTests(int executedTests) {
        this.executedTests = executedTests;
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
     * Sets {@link #failedTests}.
     *
     * @param failedTests
     *         New value for {@link #failedTests}
     */
    public void setFailedTests(int failedTests) {
        this.failedTests = failedTests;
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
     * Sets {@link #skippedTests}.
     *
     * @param skippedTests
     *         New value for {@link #skippedTests}
     */
    public void setSkippedTests(int skippedTests) {
        this.skippedTests = skippedTests;
    }

    /**
     * Gets {@link #passedTests}.
     *
     * @return {@link #passedTests}
     */
    public int getPassedTests() {
        return passedTests;
    }

    /**
     * Sets {@link #passedTests}.
     *
     * @param passedTests
     *         New value for {@link #passedTests}
     */
    public void setPassedTests(int passedTests) {
        this.passedTests = passedTests;
    }
}
