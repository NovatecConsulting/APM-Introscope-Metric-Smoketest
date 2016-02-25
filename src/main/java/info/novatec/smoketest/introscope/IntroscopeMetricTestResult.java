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

package info.novatec.smoketest.introscope;

import com.google.common.base.Objects;
import info.novatec.smoketest.core.model.IMetricTestResult;

/**
 * Introscope implementation of {@link IMetricTestResult}.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class IntroscopeMetricTestResult implements IMetricTestResult {

    /**
     * The initial executed query.
     */
    private String query;

    /**
     * The name of the agent which returned the result.
     */
    private String agentName;

    /**
     * The resource path.
     */
    private String resource;

    /**
     * The name of the metric.
     */
    private String metricName;

    /**
     * The actual value of the metric.
     */
    private String value;

    /**
     * No-Args Constructor.
     */
    public IntroscopeMetricTestResult() {
    }

    /**
     * Create a new IntroscopeMetricTestResult.
     *
     * @param query
     *         The query which produced this result
     */
    public IntroscopeMetricTestResult(String query) {
        this.query = query;
    }

    /**
     * Creates a new IntroscopeMetricTestResult.
     *
     * @param query
     *         The related query string
     * @param agentName
     *         The name of the agent
     * @param resource
     *         The resource path
     * @param metricName
     *         The metric name
     * @param value
     *         The value of the metric
     */
    public IntroscopeMetricTestResult(String query,
                                      String agentName,
                                      String resource,
                                      String metricName,
                                      String value) {
        this.query = query;
        this.agentName = agentName;
        this.resource = resource;
        this.metricName = metricName;
        this.value = value;
    }

    @Override
    public String getFullQualifiedMetricName() {
        return IntroscopeUtils.generateFullQualifiedName(agentName, resource, metricName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntroscopeMetricTestResult that = (IntroscopeMetricTestResult) o;
        return Objects.equal(getQuery(), that.getQuery()) &&
                Objects.equal(getAgentName(), that.getAgentName()) &&
                Objects.equal(getResource(), that.getResource()) &&
                Objects.equal(getMetric(), that.getMetric()) &&
                Objects.equal(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getQuery(), getAgentName(), getResource(), getMetric(), getValue());
    }

    //-------------------------------------------------------------
    // Get/ Set
    //-------------------------------------------------------------

    /**
     * Gets {@link #query}.
     *
     * @return {@link #query}
     */
    public String getQuery() {
        return query;
    }

    /**
     * Sets {@link #query}.
     *
     * @param query
     *         New value for {@link #query}
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Gets {@link #agentName}.
     *
     * @return {@link #agentName}
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     * Sets {@link #agentName}.
     *
     * @param agentName
     *         New value for {@link #agentName}
     */
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    /**
     * Gets {@link #resource}.
     *
     * @return {@link #resource}
     */
    public String getResource() {
        return resource;
    }

    /**
     * Sets {@link #resource}.
     *
     * @param resource
     *         New value for {@link #resource}
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * Gets {@link #metricName}.
     *
     * @return {@link #metricName}
     */
    public String getMetric() {
        return metricName;
    }

    /**
     * Sets {@link #metricName}.
     *
     * @param metricName
     *         New value for {@link #metricName}
     */
    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    /**
     * Gets {@link #value}.
     *
     * @return {@link #value}
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Sets {@link #value}.
     *
     * @param value
     *         New value for {@link #value}
     */
    public void setValue(String value) {
        this.value = value;
    }
}
