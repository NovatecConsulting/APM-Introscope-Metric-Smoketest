package info.novatec.smoketest.support;

import info.novatec.smoketest.core.model.MetricTestResultSet;
import info.novatec.smoketest.core.service.query.IMetricDataCollector;
import info.novatec.smoketest.core.service.query.MetricQueryServiceException;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class DummyMetricDataCollector implements IMetricDataCollector<DummyMetric, DummyMetricResult> {

    @Override
    public MetricTestResultSet<DummyMetric, DummyMetricResult> query(DummyMetric definition) throws
            MetricQueryServiceException {
        MetricTestResultSet<DummyMetric, DummyMetricResult> resultSet = new MetricTestResultSet<>(definition);
        resultSet.addResult(new DummyMetricResult(definition.getFullQualifiedName()));

        return resultSet;
    }
}
