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

import info.novatec.smoketest.core.model.MetricTestResultSet;
import info.novatec.smoketest.core.service.collector.IMetricDataCollector;
import info.novatec.smoketest.core.service.collector.MetricDataCollectorException;
import info.novatec.smoketest.core.service.time.ITimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.google.common.base.Preconditions.checkNotNull;
import static info.novatec.smoketest.introscope.IntroscopeDataCollector.IntroscopeDataCollectorConstants.CONNECTION_TEMPLATE;
import static info.novatec.smoketest.introscope.IntroscopeDataCollector.IntroscopeDataCollectorConstants.PASSWORD_MARKER;
import static info.novatec.smoketest.introscope.IntroscopeDataCollector.IntroscopeDataCollectorConstants.JDBC_TEMPLATE;
import static info.novatec.smoketest.introscope.IntroscopeDataCollector.IntroscopeDataCollectorConstants.RS_ENTRY_AGENT_NAME;
import static info.novatec.smoketest.introscope.IntroscopeDataCollector.IntroscopeDataCollectorConstants.RS_ENTRY_METRIC_NAME;
import static info.novatec.smoketest.introscope.IntroscopeDataCollector.IntroscopeDataCollectorConstants.RS_ENTRY_RESOURCE;
import static info.novatec.smoketest.introscope.IntroscopeDataCollector.IntroscopeDataCollectorConstants.RS_ENTRY_VALUE;


/**
 * The Introscope implementation of {@link IMetricDataCollector}.
 *
 * @author Claudio Waldvogel
 */
@Singleton
public class IntroscopeDataCollector implements IMetricDataCollector<IntroscopeMetric, IntroscopeMetricTestResult> {

    /**
     * The slf4j logger.
     */
    private static Logger log = LoggerFactory.getLogger(IntroscopeDataCollector.class);

    /**
     * The {@link ITimeService}.
     */
    private ITimeService timeService;

    /**
     * The {@link IntroscopeConfiguration}.
     */
    private IntroscopeConfiguration configuration;

    /**
     * The JDBC/SQL connection class.
     */
    private Connection connection;

    /**
     * The Statement object to execute all queries.
     */
    private Statement statement;

    /**
     * Flag to indicate if service is already initialized.
     */
    private boolean initialized;

    //-------------------------------------------------------------
    // Constructors
    //-------------------------------------------------------------

    /**
     * Create a new IntroscopeDataCollector.
     *
     * @param configuration
     *         The {@link IntroscopeConfiguration}
     * @param service
     *         The {@link ITimeService}
     */
    @Inject
    public IntroscopeDataCollector(final IntroscopeConfiguration configuration,
                                   final ITimeService service) {
        this.configuration = checkNotNull(configuration);
        this.timeService = checkNotNull(service);
    }

    //-------------------------------------------------------------
    // Interface Implementation: IMetricDataCollector
    //-------------------------------------------------------------

    @PostConstruct
    @Override
    public void initialize() {
        log.info("Start initializing the IntroscopeDataCollector...");
        try {
            log.info("Loading JDBC Driver: {}", configuration.getJDBCDriver());
            //Ensure that the introscope jdbc driver is loaded
            Class.forName(configuration.getJDBCDriver());

            String urlNoPassword = String.format(CONNECTION_TEMPLATE,
                    configuration.getJDBCUser(),
                    PASSWORD_MARKER,
                    configuration.getJDBCHost(),
                    configuration.getJDBCPort());

            String connectionURL = urlNoPassword
                    .replace(PASSWORD_MARKER, configuration.getJDBCPassword());

            log.info("Connecting to: {}...", urlNoPassword);
            connection = DriverManager.getConnection(connectionURL);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            throw new MetricDataCollectorException("Failed to load JDBC Driver: " + configuration.getJDBCDriver(), e);
        } catch (SQLException e) {
            throw new MetricDataCollectorException("Connection failed!", e);
        }
        initialized = true;
        log.info("IntroscopeDataCollector initialized!");
    }

    @Override
    public MetricTestResultSet<IntroscopeMetric, IntroscopeMetricTestResult> collect(IntroscopeMetric definition)
            throws MetricDataCollectorException {
        if (!initialized) {
            //Fail if service is not yet initialized
            throw new RuntimeException("Not initialized!");
        }
        String queryString = buildQueryString(definition);
        log.debug(queryString);

        try (ResultSet resultSet = statement.executeQuery(queryString)) {
            MetricTestResultSet<IntroscopeMetric, IntroscopeMetricTestResult> metricTestResultSet =
                    new MetricTestResultSet<>(definition);
            //In this loop each row of the result set is transformed into a MetricTestResultSet
            while (resultSet.next()) {
                dumpResult(resultSet);
                metricTestResultSet.addResult(
                        new IntroscopeMetricTestResult(
                                queryString,
                                resultSet.getString(RS_ENTRY_AGENT_NAME),
                                resultSet.getString(RS_ENTRY_RESOURCE),
                                resultSet.getString(RS_ENTRY_METRIC_NAME),
                                resultSet.getString(RS_ENTRY_VALUE)
                        )
                );
            }
            return metricTestResultSet;
        } catch (SQLException e) {
            throw new MetricDataCollectorException("Failed to execute collect: " + queryString, e);
        }
    }

    /**
     * Builds the sql collect string depending on the given {@link IntroscopeMetric}.
     *
     * @param definition
     *         The {@link IntroscopeMetric}
     * @return The collect string
     */
    private String buildQueryString(final IntroscopeMetric definition) {
        return String.format(JDBC_TEMPLATE,
                definition.getAgentExpression(),
                IntroscopeUtils.generateMetricPath(definition.getResourceExpression(),
                        definition.getMetricExpression()),
                timeService.format(configuration.getJDBCDateFormat(), timeService.getFrom()),
                timeService.format(configuration.getJDBCDateFormat(), timeService.getTo()));
    }

    /**
     * Writes all values of a ResultSet to the logger.
     *
     * @param resultSet
     *         The ResultSet to be logged
     */
    private void dumpResult(ResultSet resultSet) {
        StringBuilder builder = new StringBuilder("\n");
        try {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                builder.append(resultSet.getMetaData().getColumnName(i))
                        .append("->")
                        .append(resultSet.getString(i))
                        .append("\n");
            }
            log.debug(builder.toString());
        } catch (SQLException e) {
            throw new MetricDataCollectorException(e);
        }
    }


    /**
     * Internal class holding constants.
     */
    public static class IntroscopeDataCollectorConstants {

        /**
         * Defines the template for all Introscope JDBC queries.
         */
        public static final String JDBC_TEMPLATE = "select * from metric_data where agent='%s' and metric='%s' and "
                + "timestamp between '%s' and '%s' aggregateall";

        /**
         * Defines a placeholder for passwords.
         */
        public static final String PASSWORD_MARKER = "*****";

        /**
         * Defines the introscope JDBC connection template. Connections are defined like: {@code
         * jdbc:introscope:net//{username}{:password}@{host}:{em port}}
         */
        public static final String CONNECTION_TEMPLATE = "jdbc:introscope:net//%s:%s@%s:%s";

        /**
         * The Domain value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_DOMAIN = "Domain";

        /**
         * The Host value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_HOST = "Host";

        /**
         * The Process value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_PROCESS = "Process";
        /**
         * The AgentName value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_AGENT_NAME = "AgentName";

        /**
         * The Resource value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_RESOURCE = "Resource";

        /**
         * The MetricName value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_METRIC_NAME = "MetricName";

        /**
         * The Record_Type value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_RECORD_TYPE = "Record_Type";

        /**
         * The Period value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_PERIOD = "Period";

        /**
         * The Intended_End_Timestamp value of the designated column in the current row of an Introscope
         * <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_INTENDED_END_TIMESTAMP = "Intended_End_Timestamp";

        /**
         * The Actual_Start_Timestamp value of the designated column in the current row of an Introscope
         * <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_ACTUAL_START_TIMESTAMP = "Actual_Start_Timestamp";

        /**
         * The Actual_End_Timestamp value of the designated column in the current row of an Introscope
         * <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_ACTUAL_END_TIMESTAMP = "Actual_End_Timestamp";

        /**
         * The Count value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_COUNT = "Count";

        /**
         * The Type value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_TYPE = "Type";

        /**
         * The Value value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_VALUE = "Value";

        /**
         * The Min value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_MIN = "Min";

        /**
         * The Max value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_MAX = "Max";

        /**
         * The String_Value value of the designated column in the current row of an Introscope <code>ResultSet</code>.
         */
        public static final String RS_ENTRY_STRING_VALUE = "String_Value";
    }
}
