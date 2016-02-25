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

import info.novatec.smoketest.core.application.configuration.Configuration;
import info.novatec.smoketest.core.model.TestLevel;
import info.novatec.smoketest.core.model.TimeRange;
import info.novatec.smoketest.core.util.Configurations;

/**
 * The default configuration object for smoke tests.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class SmokeTestConfiguration extends Configuration {

    /**
     * The {@link TestLevel} for this smoke test. The level defines which tests will be executed and which tests will be
     * skipped.
     */
    private TestLevel testLevel = TestLevel.LEVEL_0;

    /**
     * The {@link TimeRange} for this smoke test.
     */
    private TimeRange timeRange;

    /**
     * The offset in hours between start and end date of the smoke test. This property will only have an effect if
     * {@link #timeRange} is not or only partially provided. This means that only start date is provided the end date
     * will be calculated and vice versa.
     */
    private int timeOffset = 4;

    /**
     * The directory where reports are stored.
     */
    private String reportDirectory = "test-results";

    /**
     * The date time pattern to format dates. Be aware that if dateTimePattern is used in any case to create files, it
     * must not contain any invalid characters like ":".
     */
    private String dateTimePattern = "yyyyMMdd'T'HHmm";

    /**
     * No-Args Constructor.
     */
    public SmokeTestConfiguration() {
    }

    @Override
    public String toString() {
        return Configurations.toString(this);
    }

    /**
     * Gets {@link #testLevel}.
     *
     * @return {@link #testLevel}
     */
    public TestLevel getTestLevel() {
        return testLevel;
    }

    /**
     * Sets {@link #testLevel}.
     *
     * @param testLevel
     *         New value for {@link #testLevel}
     */
    public void setTestLevel(TestLevel testLevel) {
        this.testLevel = testLevel;
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
     * Gets {@link #timeOffset}.
     *
     * @return {@link #timeOffset}
     */
    public int getTimeOffset() {
        return timeOffset;
    }

    /**
     * Sets {@link #timeOffset}.
     *
     * @param timeOffset
     *         New value for {@link #timeOffset}
     */
    public void setTimeOffset(int timeOffset) {
        this.timeOffset = timeOffset;
    }

    /**
     * Gets {@link #reportDirectory}.
     *
     * @return {@link #reportDirectory}
     */
    public String getReportDirectory() {
        return reportDirectory;
    }

    /**
     * Sets {@link #reportDirectory}.
     *
     * @param reportDirectory
     *         New value for {@link #reportDirectory}
     */
    public void setReportDirectory(String reportDirectory) {
        this.reportDirectory = reportDirectory;
    }

    /**
     * Gets {@link #dateTimePattern}.
     *
     * @return {@link #dateTimePattern}
     */
    public String getDateTimePattern() {
        return dateTimePattern;
    }

    /**
     * Sets {@link #dateTimePattern}.
     *
     * @param dateTimePattern
     *         New value for {@link #dateTimePattern}
     */
    public void setDateTimePattern(String dateTimePattern) {
        this.dateTimePattern = dateTimePattern;
    }
}
