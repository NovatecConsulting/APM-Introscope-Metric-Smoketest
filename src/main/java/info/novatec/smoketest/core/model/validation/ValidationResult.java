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

/**
 * Value object representing the result of a validation.
 *
 * @author Claudio Waldvogel
 */
public class ValidationResult {

    /**
     * Indicates if the validation failed or not.
     */
    private boolean valid;

    /**
     * Message why the validation failed.
     */
    private String message;

    /**
     * Creates a new ValidationResult.
     *
     * @param valid
     *         Indicator if valid or not
     * @param message
     *         The message why the validation failed
     */
    public ValidationResult(final boolean valid,
                            final String message) {
        this.valid = valid;
        this.message = message;
    }

    /**
     * Factory function to create valid validation results.
     *
     * @return A new ValidationResult
     */
    public static ValidationResult valid() {
        return new ValidationResult(true, null);
    }

    /**
     * Factory function to create invalid validation results.
     *
     * @param message
     *         Message why the validation failed.
     * @return A new ValidationResult
     */
    public static ValidationResult invalid(String message) {
        return new ValidationResult(false, message);
    }

    /**
     * @return Indicator if validation was successful
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @return The message why the validation failed.
     */
    public String getMessage() {
        return message;
    }
}
