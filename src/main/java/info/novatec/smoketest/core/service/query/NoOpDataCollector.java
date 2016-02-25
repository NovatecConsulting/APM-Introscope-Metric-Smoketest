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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * No operation default implementation of {@link IMetricDataCollector}.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class NoOpDataCollector implements IMetricDataCollector<IMetricDefinition, IMetricTestResult> {

    /**
     * Thd slf4j logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NoOpDataCollector.class);

    /**
     * No-Args Constructor.
     */
    public NoOpDataCollector() {
        LOGGER.warn("No IMetricDataCollector defined. Using empty default implementation!");
    }

    @Override
    public MetricTestResultSet<IMetricDefinition, IMetricTestResult> query(IMetricDefinition definition)
            throws MetricDataCollectorException {
        //Simply return MetricTestResultSet with the definition
        return new MetricTestResultSet<>(definition);
    }
}
