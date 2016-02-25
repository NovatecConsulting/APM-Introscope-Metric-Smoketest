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

import com.google.common.base.MoreObjects;

import java.time.ZonedDateTime;

/**
 * Utility class to represent a time Range.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class TimeRange {

    /**
     * The {@link ZonedDateTime} representing the from date.
     */
    private ZonedDateTime from;

    /**
     * The {@link ZonedDateTime} representing the to date.
     */
    private ZonedDateTime to;

    /**
     * No-Args Constructor.
     */
    public TimeRange() {
    }

    /**
     * Creates a new TimeRange.
     *
     * @param from
     *         The from date
     * @param to
     *         The to date
     */
    public TimeRange(ZonedDateTime from,
                     ZonedDateTime to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("from", from)
                .add("to", to)
                .toString();
    }

    /**
     * Gets {@link #from}.
     *
     * @return {@link #from}
     */
    public ZonedDateTime getFrom() {
        return from;
    }

    /**
     * Gets {@link #to}.
     *
     * @return {@link #to}
     */
    public ZonedDateTime getTo() {
        return to;
    }
}
