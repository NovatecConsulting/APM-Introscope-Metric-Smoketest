package info.novatec.smoketest.support;

import info.novatec.smoketest.core.model.MetricTest;
import info.novatec.smoketest.core.service.testing.BaseTest;
import info.novatec.smoketest.introscope.IntroscopeMetric;
import info.novatec.smoketest.introscope.IntroscopeMetricTestResult;
import info.novatec.smoketest.introscope.IntroscopeMetricTestBuilder;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * @author Claudio Waldvogel
 */
@Test(testName = "Introscope Dummy Test")
public class IntroscopeDummyTest extends BaseTest<IntroscopeMetric, IntroscopeMetricTestResult> {

    @Override
    protected Set<MetricTest<IntroscopeMetric, IntroscopeMetricTestResult>> getMetricTests() {
        return new IntroscopeMetricTestBuilder()
                .single("/resource/path", "Metric1")
                .single("/resource/path", "Metric2")
                .build("Agent");
    }
}
