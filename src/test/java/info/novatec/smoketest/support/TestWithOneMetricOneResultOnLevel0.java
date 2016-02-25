package info.novatec.smoketest.support;

import com.beust.jcommander.internal.Sets;
import info.novatec.smoketest.core.model.MetricTest;
import info.novatec.smoketest.core.model.TestLevel;
import info.novatec.smoketest.core.service.testing.BaseTest;

import java.util.Set;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class TestWithOneMetricOneResultOnLevel0 extends BaseTest<DummyMetric, DummyMetricResult> {

    @Override
    protected Set<MetricTest<DummyMetric, DummyMetricResult>> getMetricTests() {
        Set<MetricTest<DummyMetric, DummyMetricResult>> set = Sets.newHashSet();
        set.add(new MetricTest<>(new DummyMetric(), TestLevel.LEVEL_0, null));
        return set;
    }

}
