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

package info.novatec.smoketest.core.service.query;


import info.novatec.smoketest.core.model.IMetricDefinition;
import info.novatec.smoketest.core.model.IMetricTestResult;
import info.novatec.smoketest.core.model.MetricTestResultSet;

/**
 * The service interface to query information about metrics.
 *
 * @param <IN>
 *         The {@link IMetricDefinition} type to be queried
 * @param <OUT>
 *         The {link IMetricTestResult} type the service returns
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public interface IMetricDataCollector<IN extends IMetricDefinition, OUT extends IMetricTestResult> {

    /**
     * Start initializing the IMetricQueryService.
     */
    default void initialize() {
        //No-OP
    }

    /**
     * Executes a query for the provided IMetricDefinition. The actual execution is completely implementation depended.
     *
     * @param definition
     *         The {@link IMetricDefinition} to be queried
     * @return A {@link MetricTestResultSet} containing all results related to this IMetricDefinition. If the service
     * could not fetch any results it returns an empty MetricTestResultSet. Null must not be returned.
     * @throws MetricQueryServiceException
     *         something fails
     */
    MetricTestResultSet<IN, OUT> query(final IN definition) throws MetricQueryServiceException;


}
