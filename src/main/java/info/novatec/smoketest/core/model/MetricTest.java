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

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import info.novatec.smoketest.core.model.validation.IValidationRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Definition to test one metric. The purpose of this class is to bundle one {@link IMetricDefinition} with an according
 * {@link TestLevel} and the information how the results should be validated. The validation is provided in form of
 * {@link IValidationRule}s. The test level defines if the this test definition will actually be executed while doing
 * the smoke test.
 *
 * @param <IN>  The type of the metric, the has an upper bound to {@link IMetricDefinition}
 * @param <OUT> The type of output, has an upper bound to {@link IMetricTestResult}
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class MetricTest<IN extends IMetricDefinition, OUT extends IMetricTestResult> {

    /**
     * The {@link IMetricDefinition}.
     */
    private IN metric;

    /**
     * The {@link TestLevel} of the test. Default is TestLevel.LEVEL_0.
     */
    private TestLevel level = TestLevel.LEVEL_0;

    /**
     * The list of {@link IValidationRule}s.
     */
    private List<IValidationRule<IN, OUT>> validations = Lists.newArrayList();

    /**
     * Creates a new MetricTest.
     *
     * @param metric The {@link IMetricDefinition}
     */
    public MetricTest(final IN metric) {
        this(metric, TestLevel.LEVEL_0);
    }

    /**
     * Creates a new MetricTest.
     *
     * @param metric The {@link IMetricDefinition}
     * @param level  The {@link TestLevel}
     */
    public MetricTest(final IN metric,
                      final TestLevel level) {
        this(metric, level, new ArrayList<IValidationRule<IN, OUT>>() {
        });
    }

    /**
     * Creates a new MetricTest.
     *
     * @param metric      The {@link IMetricDefinition}
     * @param level       The {@link TestLevel}
     * @param validations The {@link IValidationRule}s
     */
    public MetricTest(final IN metric,
                      final TestLevel level,
                      final List<IValidationRule<IN, OUT>> validations) {
        this.metric = metric;
        this.level = level;
        this.validations = validations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricTest<?, ?> that = (MetricTest<?, ?>) o;
        return Objects.equal(getMetric(), that.getMetric()) &&
                getLevel() == that.getLevel() &&
                Objects.equal(getValidations(), that.getValidations());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMetric(), getLevel(), getValidations());
    }

    /**
     * Gets The {@link TestLevel} of the test. Default is TestLevel.LEVEL_0..
     *
     * @return Value of The {@link TestLevel} of the test. Default is TestLevel.LEVEL_0..
     */
    public TestLevel getLevel() {
        return level;
    }

    /**
     * Gets The {@link IMetricDefinition}..
     *
     * @return Value of The {@link IMetricDefinition}..
     */
    public IN getMetric() {
        return metric;
    }

    /**
     * Gets The list of {@link IValidationRule}s.
     *
     * @return Value of The list of {@link IValidationRule}s.
     */
    public List<IValidationRule<IN, OUT>> getValidations() {
        return validations;
    }
}
