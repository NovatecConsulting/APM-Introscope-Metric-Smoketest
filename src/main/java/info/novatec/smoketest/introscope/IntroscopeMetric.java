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


import com.google.common.base.Strings;
import info.novatec.smoketest.core.model.IMetricDefinition;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Introscope specific implementation of {@link IMetricDefinition}.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 * @see IMetricDefinition
 */
public class IntroscopeMetric implements IMetricDefinition {

    /**
     * Regular expression describing the introscope agent name.
     */
    private final String agentExpression;

    /**
     * Regular expression which describes the resource path to the metric.
     */
    private final String resourceExpression;

    /**
     * Regular expression which describes the metric.
     */
    private final String metricExpression;

    /**
     * The full qualified name of this metric. This name combines {@link #agentExpression}, {@link #resourceExpression},
     * and {@link #metricExpression}.
     */
    private final String fullQualifiedName;

    /**
     * Creates a new IntroscopeMetric.
     *
     * @param agentExpression
     *         The agent expression. Must not be null or empty.
     * @param resourceExpression
     *         The resource expression. Null or empty is a valid value.
     * @param metricExpression
     *         The metric expression. Must not be null or empty.
     */
    public IntroscopeMetric(final String agentExpression,
                            final String resourceExpression,
                            final String metricExpression) {
        this.agentExpression = checkNotNull(Strings.emptyToNull(agentExpression),
                "The agent expression must not be null or empty");
        this.metricExpression = checkNotNull(Strings.emptyToNull(metricExpression),
                "The metric expression must not be null or empty");
        this.resourceExpression = resourceExpression;
        this.fullQualifiedName = IntroscopeUtils.generateFullQualifiedName(agentExpression, resourceExpression,
                metricExpression);
    }

    //-------------------------------------------------------------
    // Interface Implementation: IMetricDefinition
    //-------------------------------------------------------------

    @Override
    public String getSimpleName() {
        return getMetricExpression();
    }

    @Override
    public String getFullQualifiedName() {
        return fullQualifiedName;
    }

    //-------------------------------------------------------------
    // Accessors
    //-------------------------------------------------------------

    /**
     * Gets {@link #agentExpression}.
     *
     * @return {@link #agentExpression}
     */
    public String getAgentExpression() {
        return agentExpression;
    }

    /**
     * Gets {@link #resourceExpression}.
     *
     * @return {@link #resourceExpression}
     */
    public String getResourceExpression() {
        return resourceExpression;
    }

    /**
     * Gets {@link #metricExpression}.
     *
     * @return {@link #metricExpression}
     */
    public String getMetricExpression() {
        return metricExpression;
    }
}
