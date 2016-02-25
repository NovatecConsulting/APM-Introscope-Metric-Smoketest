package info.novatec.smoketest.support;

import info.novatec.smoketest.core.model.IMetricDefinition;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class DummyMetric implements IMetricDefinition {

    private String name = "DummyMetric";

    public DummyMetric() {
    }

    public DummyMetric(String name) {
        this.name = name;
    }

    @Override
    public String getSimpleName() {
        return name;
    }

    @Override
    public String getFullQualifiedName() {
        return name;
    }

}
