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

import java.sql.SQLException;

/**
 * Exception foo all errors occurring while querying a metric.
 *
 * @author Claudio Waldvogel
 */
public class MetricDataCollectorException extends RuntimeException {

    /**
     * Create a new  MetricDataCollectorException.
     *
     * @param message
     *         The exception text
     */
    public MetricDataCollectorException(String message) {
        super(message);
    }

    /**
     * Create a new  MetricDataCollectorException.
     *
     * @param cause
     *         The root cause
     */
    public MetricDataCollectorException(SQLException cause) {
        super(cause);
    }

    /**
     * Create a new  MetricDataCollectorException.
     *
     * @param message
     *         The error message
     * @param cause
     *         The root cause
     */
    public MetricDataCollectorException(String message, Throwable cause) {
        super(message, cause);
    }

}
