package info.novatec.smoketest.introscope;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import info.novatec.smoketest.core.CoreBundle;
import info.novatec.smoketest.core.SmokeTestConfiguration;
import info.novatec.smoketest.core.application.Bundle;
import info.novatec.smoketest.core.application.Environment;
import info.novatec.smoketest.core.application.Setup;
import info.novatec.smoketest.core.service.collector.IMetricDataCollector;
import info.novatec.smoketest.core.service.testing.ITestExecutionService;
import info.novatec.smoketest.core.service.testing.report.StatisticsReporter;
import info.novatec.smoketest.support.IntroscopeDummyTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class IntroscopeIntegrationTest {


    @org.testng.annotations.Test
    public void testIntroscopeIntegration() {

        Setup<IntroscopeConfiguration> setup = new Setup
                .Builder<IntroscopeConfiguration>()
                .configurationClazz(IntroscopeConfiguration.class)
                .addBundle(new CoreBundle())
                .addBundle(new IntroscopeTestBundle())
                .addBundle(new Bundle<IntroscopeConfiguration>() {

                    @Inject
                    private ITestExecutionService executionService;

                    @Override
                    public void run(Environment<IntroscopeConfiguration> environment) {
                        StatisticsReporter<SmokeTestConfiguration> reporter = new StatisticsReporter<>();
                        executionService.addReporter(reporter);
                        executionService.execute(environment.getTests());
                        assertEquals(reporter.getPassedTests(), 2);
                    }
                })
                .build("-o", "JDBCUser=Admin",
                        "JDBCPassword=Admin", "JDBCHost=localhost", "JDBCPort=5000",
                        "JDBCDriver=com.wily.introscope.jdbc.IntroscopeDriver",
                        "JDBCDateFormat=EEE MMM dd HH:mm:ss zzz yyyy");

        Environment<IntroscopeConfiguration> environment = new Environment.Builder<>(setup)
                .tests(IntroscopeDummyTest.class)
                .build();

        IntroscopeConfiguration configuration = environment.getConfiguration();
        assertEquals(configuration.getJDBCUser(), "Admin");
        assertEquals(configuration.getJDBCPassword(), "Admin");
        assertEquals(configuration.getJDBCPort(), "5000");
        assertEquals(configuration.getJDBCDriver(), "com.wily.introscope.jdbc.IntroscopeDriver");
        assertEquals(configuration.getJDBCDateFormat(), "EEE MMM dd HH:mm:ss zzz yyyy");


        IMetricDataCollector<IntroscopeMetric, IntroscopeMetricTestResult> collectorService =
                environment.getInjector().getInstance(
                        Key.get(new TypeLiteral<IMetricDataCollector<IntroscopeMetric, IntroscopeMetricTestResult>>() {
                        })
                );

        assertNotNull(collectorService);
        environment.run();
    }


}
