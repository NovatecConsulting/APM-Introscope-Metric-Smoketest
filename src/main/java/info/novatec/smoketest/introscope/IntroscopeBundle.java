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

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import info.novatec.smoketest.core.application.Bundle;
import info.novatec.smoketest.core.application.Environment;
import info.novatec.smoketest.core.model.IMetricDefinition;
import info.novatec.smoketest.core.model.IMetricTestResult;
import info.novatec.smoketest.core.service.query.IMetricDataCollector;

/**
 * Bundle implementation to provide Introscope specific implementations.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class IntroscopeBundle extends Bundle<IntroscopeConfiguration> {

    @Override
    public void configure(Environment.Builder<IntroscopeConfiguration> builder) {
        builder.services(new AbstractModule() {

            @Override
            @SuppressWarnings("unchecked")
            protected void configure() {
                //We have to bind the IntroscopeQueryService to two TypeLiterals to ensure a proper
                //injection on various levels.
                bind(new TypeLiteral<IMetricDataCollector<IMetricDefinition, IMetricTestResult>>() {
                }).to((Class) IntroscopeDataCollector.class);

                bind(new TypeLiteral<IMetricDataCollector<IntroscopeMetric, IntroscopeMetricTestResult>>() {
                }).to(IntroscopeDataCollector.class);
            }
        });
    }
}
