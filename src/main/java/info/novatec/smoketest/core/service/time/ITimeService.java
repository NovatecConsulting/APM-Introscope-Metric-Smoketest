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

package info.novatec.smoketest.core.service.time;


import info.novatec.smoketest.core.model.TimeRange;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Service provides utility functions to work with a {@link TimeRange}. In addition it defines methods to format
 * ZonedDateTime/LocalDateTime objects to Strings.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public interface ITimeService {

    /**
     * Method returns the date to indicate where to start the smoke test.
     *
     * @return The form date
     */
    ZonedDateTime getFrom();

    /**
     * Method returns the date to indicate where to end the smoke test.
     *
     * @return The to date
     */
    ZonedDateTime getTo();

    /**
     * @return The {@link TimeRange} containing the form and to date
     */
    TimeRange getTimeRange();

    /**
     * Utility method to format a date with a provided data format.
     *
     * @param dateFormat
     *         The date format
     * @param date
     *         The ZonedDateTime
     * @return The formatted date string
     */
    String format(final String dateFormat,
                  final ZonedDateTime date);

    /**
     * Utility method to format a date with a provided data format.
     *
     * @param dateFormat
     *         The date format
     * @param date
     *         The LocalDateTime
     * @return The formatted date string
     */
    String format(final String dateFormat,
                  final LocalDateTime date);
}
