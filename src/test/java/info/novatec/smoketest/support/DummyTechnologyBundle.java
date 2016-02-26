package info.novatec.smoketest.support;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import info.novatec.smoketest.core.SmokeTestConfiguration;
import info.novatec.smoketest.core.application.Bundle;
import info.novatec.smoketest.core.application.Environment;
import info.novatec.smoketest.core.service.collector.IMetricDataCollector;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class DummyTechnologyBundle<T extends SmokeTestConfiguration> extends Bundle<T> {
    @Override
    public void configure(Environment.Builder<T> builder) {
        builder.services(new AbstractModule() {
            @Override
            protected void configure() {

                TypeLiteral<IMetricDataCollector<DummyMetric, DummyMetricResult>> collectorServiceTypeLiteral
                        = new TypeLiteral<IMetricDataCollector<DummyMetric, DummyMetricResult>>() {
                };

                bind(collectorServiceTypeLiteral).to(DummyMetricDataCollector.class).in(Singleton.class);
            }
        });
    }
}
