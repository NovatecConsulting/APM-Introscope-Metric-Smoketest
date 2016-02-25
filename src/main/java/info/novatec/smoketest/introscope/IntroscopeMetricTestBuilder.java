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
import com.google.common.collect.Sets;
import info.novatec.smoketest.core.model.MetricTest;
import info.novatec.smoketest.core.model.TestLevel;
import info.novatec.smoketest.core.model.validation.IValidationRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class to ease building {@link MetricTest}s.
 *
 * @author Claudio Waldvogel
 */
public class IntroscopeMetricTestBuilder {

    /**
     * List of all {@link Builder}s.
     */
    private List<Builder> builders;

    /**
     * List of all created {@link MetricTest}s.
     */
    private Set<MetricTest<IntroscopeMetric, IntroscopeMetricTestResult>> definitions;

    /**
     * Create a new IntroscopeTestBuilder.
     */
    public IntroscopeMetricTestBuilder() {
        builders = new ArrayList<>();
        definitions = Sets.newHashSet();
    }

    //-------------------------------------------------------------
    // Methods
    //-------------------------------------------------------------

    /**
     * This method creates a new Builder to define a new metric.
     *
     * @return The internal Builder instance related to this MetricQueryDefinition
     * @see IntroscopeMetricTestBuilder#entry(String)
     */
    public Builder entry() {
        return entry(null);
    }

    /**
     * This method creates a new Builder to define a new metric.
     *
     * @param entryPoint
     *         Parameter is used to defined the entry point for all MetricQueryDefinition related to this builder. The
     *         purpose of the entryPoint is to simplify several metrics which are located underneath the the same node
     *         e.g. for the metrics <ul> <li>Root|Node1|Node2|Version|App1:Version</li>
     *         <li>Root|Node1|Node2|Version|App2:Version</li> </ul> Root|Node1|Node2|Version can be defined as entry and
     *         only App1 and App2 needs to be defined as resource path.
     * @return The internal Builder instance related to this MetricQ
     * @see Builder
     * @see Builder#resource(String...)
     */
    public Builder entry(String entryPoint) {
        Builder builder = new Builder(this, entryPoint);
        this.builders.add(builder);
        return builder;
    }

    /**
     * With this method a single MetricDefinition can be defined.
     *
     * @param resourceExpression
     *         The resource expression
     * @param metricExpression
     *         The metric expression
     * @return The IntroscopeTestBuilder
     */
    public IntroscopeMetricTestBuilder single(String resourceExpression,
                                              String metricExpression) {
        entry().resource(resourceExpression)
                .metric(metricExpression)
                .level(TestLevel.LEVEL_0)
                .validation();
        return this;
    }

    /**
     * With this method a single MetricDefinition can be defined.
     *
     * @param resourceExpression
     *         The resource expression
     * @param metricExpression
     *         The metric expression
     * @param validations
     *         The list of {@link IValidationRule}s
     * @return The IntroscopeTestBuilder
     */
    public IntroscopeMetricTestBuilder single(String resourceExpression,
                                              String metricExpression,
                                              IValidationRule... validations) {
        entry().resource(resourceExpression)
                .metric(metricExpression)
                .validation(validations);
        return this;
    }

    /**
     * With this method a single MetricDefinition can be defined.
     *
     * @param resourceExpression
     *         The resource expression
     * @param metricExpression
     *         The metric expression
     * @param level
     *         The level of the current executing test
     * @param validations
     *         The list of {@link IValidationRule}s
     * @return The IntroscopeTestBuilder
     */
    public IntroscopeMetricTestBuilder single(String resourceExpression,
                                              String metricExpression,
                                              TestLevel level,
                                              IValidationRule... validations) {
        entry().resource(resourceExpression)
                .metric(metricExpression)
                .level(level)
                .validation(validations);
        return this;
    }

    /**
     * Method starts building the {@link MetricTest}s.
     *
     * @param agentExpression
     *         The expression of the agent this MetricQueryDefinition should be queried.
     * @return Set of MetricQueryDefinition
     */
    public Set<MetricTest<IntroscopeMetric, IntroscopeMetricTestResult>> build(String agentExpression) {
        for (Builder builder : builders) {
            definitions.addAll(builder.build(agentExpression));
        }
        return definitions;
    }


    //-------------------------------------------------------------
    // Inner Classes
    //-------------------------------------------------------------

    /**
     * Internal Builder to create {@link MetricTest}.
     */
    public static final class Builder {

        /**
         * Defines the path delimiter.
         */
        private static final String ESCAPED_PATH_DELIMITER = "\\|";

        /**
         * List of resource paths. Each entry of this list is postfixed by the {@link #metricExpression}. Each of this
         * combination results in one {@link MetricTest}.
         */
        private String[] resources;

        /**
         * The parent {@link IntroscopeMetricTestBuilder}.
         */
        private IntroscopeMetricTestBuilder parent;

        /**
         * Defines the entry resource path. This can be considered as prefix for all further resource entries.
         */
        private String root;

        /**
         * The actual metric expression.
         */
        private String metricExpression;

        /**
         * Assigns the {@link MetricTest} to an execution level.
         */
        private TestLevel level = TestLevel.LEVEL_0;

        /**
         * List of all {@link IValidationRule}s to validate this MetricTest.
         */
        private IValidationRule<IntroscopeMetric, IntroscopeMetricTestResult>[] validations;

        /**
         * Create a new instance of the Builder.
         *
         * @param parent
         *         The parent Builder
         * @param entry
         *         The entry point
         */
        private Builder(IntroscopeMetricTestBuilder parent,
                        String entry) {
            this.parent = parent;
            setEntry(entry);
            resource("");
        }

        /**
         * Methods attaches an unlimited list of resource expressions to the entry point.
         *
         * @param resourceExpressions
         *         The list of resource expressions
         * @return The Builder itself
         */
        public Builder resource(String... resourceExpressions) {
            this.resources = resourceExpressions;
            return this;
        }

        /**
         * Method attaches the metric expression to all resource expressions.
         *
         * @param metricExpression
         *         The metric expression
         * @return The Builder itself
         * @see info.novatec.smoketest.core.model.IMetricDefinition
         */
        public Builder metric(String metricExpression) {
            this.metricExpression = metricExpression;
            return this;
        }

        /**
         * Define the level of this MetricQueryDefinitions.
         *
         * @param level
         *         The level of the current executing test
         * @return The Builder itself
         * @see TestLevel
         */
        public Builder level(TestLevel level) {
            this.level = level;
            return this;
        }

        /**
         * Defines the unlimited list of {@link IValidationRule}s to which are used to validate the results. This method
         * finished the building process and returns the IntroscopeTestBuilder
         *
         * @param validations
         *         The IValidationRule
         * @return The IntroscopeTestBuilder
         */
        @SuppressWarnings("unchecked")
        public IntroscopeMetricTestBuilder validation(IValidationRule... validations) {
            this.validations = validations;
            return parent;
        }

        //-------------------------------------------------------------
        // Methods: Internals
        //-------------------------------------------------------------

        /**
         * Sets the root entry. If this entry is not yet postfixed with a <code>|</code>, <code>|</code> will be added.
         *
         * @param root
         *         The root entry
         * @return The postfixed root entry
         */
        private Builder setEntry(String root) {
            this.root = root;
            if (!Strings.isNullOrEmpty(root)
                    && !root.endsWith(ESCAPED_PATH_DELIMITER)) {
                this.root = root + ESCAPED_PATH_DELIMITER;
            }
            return this;
        }

        /**
         * Creates the {@link MetricTest}s.
         *
         * @param agent
         *         The agent expression related to this MetricTest
         * @return Set of {@link MetricTest}s
         */
        private Set<MetricTest<IntroscopeMetric, IntroscopeMetricTestResult>> build(String agent) {
            //sanity check
            checkArgument(!Strings.isNullOrEmpty(agent), "The agent must not be null or empty");
            checkArgument(!Strings.isNullOrEmpty(metricExpression), "The metric must not be null or empty");
            checkNotNull(validations, "No validations provided!");

            Set<MetricTest<IntroscopeMetric, IntroscopeMetricTestResult>> definitions = Sets.newHashSet();
            for (String resourceExpression : resources) {
                if (!Strings.isNullOrEmpty(root)) {
                    resourceExpression = root + resourceExpression;
                }
                IntroscopeMetric definition = new IntroscopeMetric(agent, resourceExpression, metricExpression);
                definitions.add(new MetricTest<>(definition, level, Arrays.asList(validations)));
            }
            return definitions;
        }
    }
}

