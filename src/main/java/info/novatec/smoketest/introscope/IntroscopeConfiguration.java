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

import com.fasterxml.jackson.annotation.JsonProperty;
import info.novatec.smoketest.core.SmokeTestConfiguration;
import info.novatec.smoketest.core.util.Configurations;

/**
 * The Introscope specific configuration. The configuration contains configuration values to enable a JDBC connection to
 * an Introscope Enterprise Manager. The jdbc URL is as follows jdbc:introscope:net//{username}{:password}@{host}:{em
 * port}
 *
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class IntroscopeConfiguration extends SmokeTestConfiguration {

    /**
     * The JDBC user name. Must not be null or empty.
     */
    @JsonProperty(value = "JDBCUser")
    private String JDBCUser = "";

    /**
     * The JDBC password. Might be null,
     */
    @JsonProperty(value = "JDBCPassword")
    private String JDBCPassword = "";

    /**
     * The JDBC user name. Must not be null or empty.
     */
    @JsonProperty(value = "JDBCHost")
    private String JDBCHost = "";

    /**
     * The JDBC connection port. Must not be null or empty.
     */
    @JsonProperty(value = "JDBCPort")
    private String JDBCPort = "";

    /**
     * The JDBC driver class. Must not be null or empty.
     */
    @JsonProperty(value = "JDBCDriver")
    private String JDBCDriver = "com.wily.introscope.jdbc.IntroscopeDriver";

    /**
     * The date format which is used to for date in JDCB queries.
     */
    @JsonProperty(value = "JDBCDateFormat")
    private String JDBCDateFormat = "EEE MMM dd HH:mm:ss zzz yyyy";

    @Override
    public String toString() {
        return Configurations.toString(this, "JDBCPassword");
    }

    //-------------------------------------------------------------
    // Get/Set
    //-------------------------------------------------------------

    /**
     * Gets {@link #JDBCUser}.
     *
     * @return {@link #JDBCUser}
     */
    public String getJDBCUser() {
        return JDBCUser;
    }

    /**
     * Sets {@link #JDBCUser}.
     *
     * @param JDBCUser
     *         New value for {@link #JDBCUser}
     */
    public void setJDBCUser(String JDBCUser) {
        this.JDBCUser = JDBCUser;
    }

    /**
     * Gets {@link #JDBCPassword}.
     *
     * @return {@link #JDBCPassword}
     */
    public String getJDBCPassword() {
        return JDBCPassword;
    }

    /**
     * Sets {@link #JDBCPassword}.
     *
     * @param JDBCPassword
     *         New value for {@link #JDBCPassword}
     */
    public void setJDBCPassword(String JDBCPassword) {
        this.JDBCPassword = JDBCPassword;
    }

    /**
     * Gets {@link #JDBCHost}.
     *
     * @return {@link #JDBCHost}
     */
    public String getJDBCHost() {
        return JDBCHost;
    }

    /**
     * Sets {@link #JDBCHost}.
     *
     * @param JDBCHost
     *         New value for {@link #JDBCHost}
     */
    public void setJDBCHost(String JDBCHost) {
        this.JDBCHost = JDBCHost;
    }

    /**
     * Gets {@link #JDBCPort}.
     *
     * @return {@link #JDBCPort}
     */
    public String getJDBCPort() {
        return JDBCPort;
    }

    /**
     * Sets {@link #JDBCPort}.
     *
     * @param JDBCPort
     *         New value for {@link #JDBCPort}
     */
    public void setJDBCPort(String JDBCPort) {
        this.JDBCPort = JDBCPort;
    }

    /**
     * Gets {@link #JDBCDriver}.
     *
     * @return {@link #JDBCDriver}
     */
    public String getJDBCDriver() {
        return JDBCDriver;
    }

    /**
     * Sets {@link #JDBCDriver}.
     *
     * @param JDBCDriver
     *         New value for {@link #JDBCDriver}
     */
    public void setJDBCDriver(String JDBCDriver) {
        this.JDBCDriver = JDBCDriver;
    }

    /**
     * Gets {@link #JDBCDateFormat}.
     *
     * @return {@link #JDBCDateFormat}
     */
    public String getJDBCDateFormat() {
        return JDBCDateFormat;
    }

    /**
     * Sets {@link #JDBCDateFormat}.
     *
     * @param JDBCDateFormat
     *         New value for {@link #JDBCDateFormat}
     */
    public void setJDBCDateFormat(String JDBCDateFormat) {
        this.JDBCDateFormat = JDBCDateFormat;
    }
}
