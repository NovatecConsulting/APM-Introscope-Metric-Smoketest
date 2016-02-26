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

package info.novatec.smoketest.core.model.validation;

import info.novatec.smoketest.core.model.IMetricDefinition;
import info.novatec.smoketest.core.model.IMetricTestResult;
import info.novatec.smoketest.core.model.MetricTest;
import info.novatec.smoketest.core.model.MetricTestResultSet;
import info.novatec.smoketest.core.service.collector.IMetricDataCollector;

/**
 * Defines a rule to validate the results gathered by the IMetricDataCollector. A validation rule is one part of a {@link
 * MetricTest}.
 *
 * @param <IN>
 *         The type of the metric, the has an upper bound to {@link IMetricDefinition}
 * @param <OUT>
 *         The type of output, has an upper bound to {@link IMetricTestResult}
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 * @see MetricTest
 * @see IMetricDataCollector
 */
public interface IValidationRule<IN extends IMetricDefinition, OUT extends IMetricTestResult> {

    /**
     * Applies the rule to the {@link MetricTestResultSet} provided from the {@link IMetricDataCollector}.
     *
     * @param result
     *         The MetricTestResultSet
     * @return A new ValidationResult
     * @see ValidationResult
     * @see MetricTestResultSet
     */
    ValidationResult apply(final MetricTestResultSet<IN, OUT> result);
}
