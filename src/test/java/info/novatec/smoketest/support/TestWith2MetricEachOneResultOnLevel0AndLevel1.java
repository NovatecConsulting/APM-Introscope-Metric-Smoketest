package info.novatec.smoketest.support;

import com.google.common.collect.Sets;
import info.novatec.smoketest.core.model.MetricTest;
import info.novatec.smoketest.core.model.TestLevel;
import info.novatec.smoketest.core.service.testing.BaseTest;

import java.util.Set;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class TestWith2MetricEachOneResultOnLevel0AndLevel1 extends BaseTest<DummyMetric, DummyMetricResult> {
    @Override
    protected Set<MetricTest<DummyMetric, DummyMetricResult>> getMetricTests() {
        Set<MetricTest<DummyMetric, DummyMetricResult>> set = Sets.newHashSet();
        set.add(new MetricTest<>(new DummyMetric("Metric1"), TestLevel.LEVEL_0));
        set.add(new MetricTest<>(new DummyMetric("Metric2"), TestLevel.LEVEL_1));
        return set;
    }
}
