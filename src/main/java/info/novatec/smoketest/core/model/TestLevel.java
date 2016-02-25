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

/**
 * Defines the level of a Test. Currently 2 levels are support: LEVEL_0 and LEVEL_1.
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public enum TestLevel {

    /**
     * This is the default level. If {@link MetricTest} is defined for level 0 it will always be executed.
     */
    LEVEL_0(0),

    /**
     * Top test level.
     */
    LEVEL_1(1);

    /**
     * The internal integer ordinal representing the test level. This is used to determine if one test level contains an
     * other one.
     */
    private int ordinal;

    /**
     * Create a new TestLevel.
     *
     * @param level
     *         The level ordinal.
     */
    TestLevel(int level) {
        this.ordinal = level;
    }

    /**
     * @return The ordinal
     */
    public int getOrdinal() {
        return ordinal;
    }

    /**
     * Checks if this level contains an other level.
     *
     * @param level
     *         The level to be checked.
     * @return true if this level contains the given one, false otherwise.
     */
    public boolean contains(TestLevel level) {
        return ordinal >= level.getOrdinal();
    }
}
