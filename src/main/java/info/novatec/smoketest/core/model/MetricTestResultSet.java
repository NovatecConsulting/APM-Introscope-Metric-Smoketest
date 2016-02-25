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

package info.novatec.smoketest.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all {@link IMetricTestResult} for one {@link IMetricDefinition}.
 *
 * @param <IN>
 *         The type of IMetricDefinition
 * @param <OUT>
 *         The type of IMetricTestResult
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 * @see IMetricDefinition
 * @see IMetricTestResult
 */
public class MetricTestResultSet<IN extends IMetricDefinition, OUT extends IMetricTestResult> {

    /**
     * The {@link IMetricDefinition}.
     */
    private IN definition;

    /**
     * List of all results ({@link IMetricTestResult}) which were gathered based on the the definition.
     */
    private List<OUT> results;

    /**
     * The {@link TimeRange} of this result set. This range defines start and stop of collecting data for the defined
     * {@link IMetricDefinition}.
     */
    private TimeRange timeRange;

    /**
     * Creates a new MetricTestResultSet.
     *
     * @param definition
     *         The {@link IMetricDefinition}
     */
    public MetricTestResultSet(final IN definition) {
        this(definition, null);
    }

    /**
     * Creates a new MetricTestResultSet.
     *
     * @param definition
     *         The {@link IMetricDefinition}
     * @param timeRange
     *         The {@link TimeRange} of this
     */
    public MetricTestResultSet(final IN definition,
                               final TimeRange timeRange) {
        this(definition, timeRange, new ArrayList<>());
    }

    /**
     * Creates a new MetricTestResultSet.
     *
     * @param definition
     *         The {@link IMetricDefinition}
     * @param timeRange
     *         The {@link TimeRange} of this
     * @param results
     *         The List of {@link IMetricTestResult}s
     */
    public MetricTestResultSet(final IN definition,
                               final TimeRange timeRange,
                               final List<OUT> results) {
        this.definition = definition;
        this.timeRange = timeRange;
        this.results = results != null ? results : new ArrayList<>();
    }

    /**
     * Adds a new IMetricTestResult to the result set.
     *
     * @param result
     *         The {@link IMetricTestResult} to be added
     * @return The MetricTestResultSet itself for chaining.
     */
    public MetricTestResultSet<IN, OUT> addResult(final OUT result) {
        results.add(result);
        return this;
    }

    /**
     * Checks if this result set has results.
     *
     * @return true if results are available, false otherwise.
     */
    public boolean hasResults() {
        return getResults().size() > 0;
    }

    /**
     * @return The {@link IMetricDefinition}
     */
    public IN getDefinition() {
        return definition;
    }

    /**
     * @return The {@link TimeRange}
     */
    public TimeRange getTimeRange() {
        return timeRange;
    }

    /**
     * @return The list of  {@link IMetricTestResult}s
     **/
    public List<OUT> getResults() {
        return results;
    }
}
