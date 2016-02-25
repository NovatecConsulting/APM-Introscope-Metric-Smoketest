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

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * @author Claudio Waldvogel
 */
public final class IntroscopeUtils {

    /**
     * Private Class.
     */
    private IntroscopeUtils() {
    }

    /**
     * Defines the path delimiter.
     */
    public static final String ESCAPED_PATH_DELIMITER = "\\|";

    /**
     * Combines resource expression and metric expression to a full qualified metric path, for example
     * <code>/path/subPath:Metric</code>.
     *
     * @param resourceExpression
     *         The resource expression
     * @param metricExpression
     *         The metric expression
     * @return The full qualified metric path.
     */
    public static String generateMetricPath(final String resourceExpression,
                                            final String metricExpression) {
        String path;
        if (Strings.isNullOrEmpty(resourceExpression)) {
            path = metricExpression;
        } else {
            path = String.format("%s:%s", resourceExpression, metricExpression);
        }
        return path;
    }

    /**
     * Generates a full qualified metric name (e.g. agent|path|sub|metric)
     *
     * @param agentExpression
     *         The agent expression
     * @param metric
     *         The metric expression
     * @return The full qualified name
     */
    public static String generateFullQualifiedName(final String agentExpression,
                                                   final String metric) {
        return generateFullQualifiedName(agentExpression, null, metric);
    }

    /**
     * Generates a full qualified metric name (e.g. agent|path|sub|metric)
     *
     * @param agentExpression
     *         The agent expression
     * @param resource
     *         The resource path
     * @param metric
     *         The metric expression
     * @return The full qualified name
     */
    public static String generateFullQualifiedName(final String agentExpression,
                                                   final String resource,
                                                   final String metric) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(agentExpression), "Agent mut not be null!");
        String metricPath = generateMetricPath(resource, metric);
        return agentExpression + ESCAPED_PATH_DELIMITER + metricPath;
    }

}
