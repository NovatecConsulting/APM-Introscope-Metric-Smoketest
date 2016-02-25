package info.novatec.smoketest.core.service;

import com.google.inject.Inject;
import info.novatec.smoketest.core.CoreBundle;
import info.novatec.smoketest.core.SmokeTestConfiguration;
import info.novatec.smoketest.core.application.Bundle;
import info.novatec.smoketest.core.application.Environment;
import info.novatec.smoketest.core.service.testing.ITestExecutionService;
import info.novatec.smoketest.core.service.testing.report.StatisticsReport;
import info.novatec.smoketest.core.service.testing.report.StatisticsReporter;
import info.novatec.smoketest.support.DummyTechnologyBundle;
import info.novatec.smoketest.support.Support;
import info.novatec.smoketest.support.TestWith2MetricEachOneResultOnLevel0AndLevel1;
import info.novatec.smoketest.support.TestWithOneMetricOneResultOnLevel0;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class TestExecutionServiceTest {

    @Test
    public void testTestExecution() {
        Environment<SmokeTestConfiguration> environment =
                Support.readyEnvironment(SmokeTestConfiguration.class,
                        new CoreBundle(),
                        new DummyTechnologyBundle<>(),
                        new Bundle<SmokeTestConfiguration>() {

                            @Inject
                            private ITestExecutionService executionService;

                            @Override
                            public void configure(Environment.Builder<SmokeTestConfiguration> builder) {
                                builder.tests(TestWithOneMetricOneResultOnLevel0.class);
                            }

                            @Override
                            public void run(Environment<SmokeTestConfiguration> environment) {
                                executionService.execute(environment.getTests());
                            }
                        });

        environment.run();
    }

    @Test
    public void testReporter() {
        final StatisticsReport[] report = new StatisticsReport[1];

        Bundle<SmokeTestConfiguration> reporterTestBundle = new Bundle<SmokeTestConfiguration>() {

            @Inject
            private ITestExecutionService executionService;

            private StatisticsReporter<SmokeTestConfiguration> reporter
                    = new StatisticsReporter<SmokeTestConfiguration>() {
                @Override
                protected void report(StatisticsReport defaultReport) {
                    report[0] = defaultReport;
                }
            };

            @Override
            public void configure(Environment.Builder<SmokeTestConfiguration> builder) {
                builder.tests(TestWithOneMetricOneResultOnLevel0.class);
            }

            @Override
            public void run(Environment<SmokeTestConfiguration> environment) {
                executionService.addReporter(reporter);
                executionService.execute(environment.getTests());
            }
        };


        Environment<SmokeTestConfiguration> environment =
                Support.readyEnvironment(SmokeTestConfiguration.class,
                        new CoreBundle(),
                        new DummyTechnologyBundle<>(),
                        reporterTestBundle);

        environment.run();
        assertNotNull(report);
        assertTrue(report.length == 1);
        assertEquals(report[0].getExecutedTests(), 1);
        assertEquals(report[0].getPassedTests(), 1);
    }

    @Test
    public void testLevelExecution() {
        StatisticsReporter<SmokeTestConfiguration> reporter = new StatisticsReporter<>();

        Environment<SmokeTestConfiguration> environment =
                Support.readyEnvironment(SmokeTestConfiguration.class,
                        new CoreBundle(),
                        new DummyTechnologyBundle<>(),
                        new Bundle<SmokeTestConfiguration>() {

                            @Inject
                            private ITestExecutionService executionService;

                            @Override
                            public void configure(Environment.Builder<SmokeTestConfiguration> builder) {
                                builder.tests(TestWith2MetricEachOneResultOnLevel0AndLevel1.class);
                            }

                            @Override
                            public void run(Environment<SmokeTestConfiguration> environment) {
                                executionService.addReporter(reporter);
                                executionService.execute(environment.getTests());
                            }
                        });

        environment.run();
        assertEquals(reporter.getExecutedTests(), 1);
        assertEquals(reporter.getSkippedTests(), 1);
    }


}
