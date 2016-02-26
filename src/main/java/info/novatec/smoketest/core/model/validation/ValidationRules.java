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

import com.google.common.base.Strings;
import info.novatec.smoketest.core.model.IMetricDefinition;
import info.novatec.smoketest.core.model.IMetricTestResult;
import info.novatec.smoketest.core.model.MetricTestResultSet;

import java.util.List;

/**
 * Utility class to provide default {@link IValidationRule}s.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public final class ValidationRules {

    /**
     * Must not be instantiated.
     */
    private ValidationRules() {
    }

    /**
     * Validates the amount of results contained in a {@link MetricTestResultSet}. This for example useful to check if
     * an application is deployed on several severs to check if all servers are available.
     *
     * @param expectedOccurrences
     *         How many results are expected
     * @return A valid {@link ValidationResult} if expectedOccurrences matches the amount of results, else invalid.
     */
    public static IValidationRule<IMetricDefinition, IMetricTestResult> expectedOccurrences(final int
                                                                                                    expectedOccurrences) {
        return resultSet -> {
            String template = "Expected metrics: %s. Actual metrics: %s";
            int actualOccurrences = resultSet != null ? resultSet.getResults().size() : 0;

            if (expectedOccurrences == actualOccurrences) {
                return ValidationResult.valid();
            }
            return invalid(String.format(template, expectedOccurrences, actualOccurrences));
        };
    }

    /**
     * Checks if all results of an {@link MetricTestResultSet} a neither null nor empty. <strong>It is not checked if
     * the results are greater than 0.</strong>
     *
     * @return A valid {@link ValidationResult} if all results are not empty/null, else invalid.
     */
    public static IValidationRule<IMetricDefinition, IMetricTestResult> notEmpty() {
        return resultSet -> {
            String template = "One result is empty!";
            List<IMetricTestResult> results = resultSet.getResults();
            if (!(results.size() == 0)) {
                for (IMetricTestResult result : results) {
                    if (Strings.isNullOrEmpty(result.getValue())) {
                        return invalid(template, resultSet.getDefinition().
                                getFullQualifiedName());
                    }
                }
                return ValidationResult.valid();
            }
            return invalid(template, resultSet.getDefinition().getFullQualifiedName());
        };
    }

    /**
     * @return A valid {@link ValidationResult} if all results are not zero, else invalid.
     */
    public static IValidationRule<IMetricDefinition, IMetricTestResult> notZero() {
        return resultSet -> {
            String template = "One result is zero!";
            List<IMetricTestResult> results = resultSet.getResults();
            if (!(results.size() == 0)) {
                for (IMetricTestResult result : results) {
                    try {
                        if (Double.valueOf(result.getValue()) == 0) {
                            return invalid(template, resultSet.getDefinition().getFullQualifiedName());
                        }
                    } catch (NumberFormatException ex) {
                        return invalid(template, resultSet.getDefinition()
                                .getFullQualifiedName());
                    }
                }
                return ValidationResult.valid();
            }
            return invalid(template, resultSet.getDefinition().getFullQualifiedName());
        };
    }

    /**
     * Creates an invalid {@link ValidationResult}.
     *
     * @param template
     *         The message template
     * @param args
     *         The list of arguments to be replaced in the template
     * @return An invalid ValidationResult
     */
    private static ValidationResult invalid(final String template,
                                            final String... args) {
        return ValidationResult.invalid(msg(template, args));
    }

    /**
     * Generates a message from a template and values to be replaced in the and.
     *
     * @param template
     *         The template
     * @param args
     *         The arguments to be replaced.
     * @return String message.
     */
    private static String msg(String template, String... args) {
        return String.format(template, args);
    }
}
