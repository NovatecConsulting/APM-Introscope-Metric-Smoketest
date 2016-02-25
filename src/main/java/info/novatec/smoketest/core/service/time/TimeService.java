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


import info.novatec.smoketest.core.SmokeTestConfiguration;
import info.novatec.smoketest.core.model.TimeRange;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link ITimeService}. This implementation ensures an always proper initialized {@link
 * TimeRange}. The service depends on the TimeRange defined in {SmokeTestConfiguration}.
 *
 * @author Claudio Waldvogel
 */
public class TimeService implements ITimeService {

    /**
     * The {@link TimeRange}.
     */
    private TimeRange timeRange;

    /**
     * The {@link SmokeTestConfiguration}.
     */
    private SmokeTestConfiguration configuration;

    /**
     * Mpa that caches {@link DateTimeFormatter}s to avoid recreation for each call to {@link #format(String,
     * LocalDateTime)} or {@link #format(String, ZonedDateTime)}.
     */
    private Map<String, DateTimeFormatter> formatCache;

    /**
     * Create a new TimeService instance.
     *
     * @param configuration
     *         The IConfigurationService
     */
    @Inject
    public TimeService(final SmokeTestConfiguration configuration) {
        this.formatCache = new HashMap<>();
        this.configuration = configuration;
        this.timeRange = initTimeRange(configuration.getTimeRange());
    }

    //-------------------------------------------------------------
    // Interface Implementation: ITimeService
    //-------------------------------------------------------------

    @Override
    public ZonedDateTime getFrom() {
        return timeRange.getFrom();
    }

    @Override
    public ZonedDateTime getTo() {
        return timeRange.getTo();
    }


    @Override
    public TimeRange getTimeRange() {
        return timeRange;
    }

    @Override
    public String format(final String dateFormat,
                         final ZonedDateTime date) {
        return getFormat(dateFormat).format(date);
    }

    @Override
    public String format(String dateFormat, LocalDateTime date) {
        return getFormat(dateFormat).format(date);
    }

    //-------------------------------------------------------------
    // Methods: Internal
    //-------------------------------------------------------------

    /**
     * Gets a DateTimeFormatter instance reflecting the dataFormat provided as input.
     *
     * @param dateFormat
     *         The output date format
     * @return A DateTimeFormatter instance.
     */
    private DateTimeFormatter getFormat(String dateFormat) {
        if (!formatCache.containsKey(dateFormat)) {
            formatCache.put(dateFormat, DateTimeFormatter.ofPattern(dateFormat));
        }
        return formatCache.get(dateFormat);
    }

    /**
     * Method initializes the TimeRange.
     *
     * @param timeRange
     *         The source TimeRange
     * @return A valid TimeRange
     */
    private TimeRange initTimeRange(final TimeRange timeRange) {
        ZonedDateTime from = null;
        ZonedDateTime to = null;
        if (timeRange != null) {
            from = timeRange.getFrom();
            to = timeRange.getTo();
        }

        if (to == null) {
            if (from == null) {
                to = ZonedDateTime.now();
            } else {
                to = ZonedDateTime.of(
                        from.plusHours(configuration.getTimeOffset()).toLocalDateTime(),
                        timeRange.getFrom().getZone()
                );
            }
        }
        if (from == null) {
            from = ZonedDateTime.of(
                    to.toLocalDateTime(),
                    to.getZone()
            ).minusHours(configuration.getTimeOffset());
        }
        return new TimeRange(from, to);
    }
}

