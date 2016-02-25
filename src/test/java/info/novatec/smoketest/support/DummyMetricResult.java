package info.novatec.smoketest.support;

import info.novatec.smoketest.core.model.IMetricTestResult;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class DummyMetricResult implements IMetricTestResult {

    public String metricName;

    public DummyMetricResult(String metricName) {
        this.metricName = metricName;
    }

    @Override
    public String getFullQualifiedMetricName() {
        return metricName;
    }

    @Override
    public String getValue() {
        return "Empty";
    }
}
