package info.novatec.smoketest.introscope;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import info.novatec.smoketest.core.application.Bundle;
import info.novatec.smoketest.core.application.Environment;
import info.novatec.smoketest.core.model.IMetricDefinition;
import info.novatec.smoketest.core.model.IMetricTestResult;
import info.novatec.smoketest.core.model.MetricTestResultSet;
import info.novatec.smoketest.core.service.query.IMetricDataCollector;

/**
 * @author Claudio Waldvogel
 */
public class IntroscopeTestBundle extends Bundle<IntroscopeConfiguration> {


    @Override
    public void configure(Environment.Builder<IntroscopeConfiguration> builder) {
        builder.services(new AbstractModule() {

            final IMetricDataCollector<IntroscopeMetric, IntroscopeMetricTestResult> service =
                    definition -> new MetricTestResultSet<IntroscopeMetric, IntroscopeMetricTestResult>(definition)
                            .addResult(
                                    new IntroscopeMetricTestResult(
                                            definition.getAgentExpression(),
                                            definition.getAgentExpression(),
                                            definition.getResourceExpression(),
                                            definition.getMetricExpression(),
                                            null)
                            );

            @Override
            @SuppressWarnings("unchecked")
            protected void configure() {
                //We have to bind the IntroscopeQueryService to two TypeLiterals to ensure a proper
                //injection on various levels.
                bind(new TypeLiteral<IMetricDataCollector<IMetricDefinition, IMetricTestResult>>() {
                }).toInstance((IMetricDataCollector) service);

                bind(new TypeLiteral<IMetricDataCollector<IntroscopeMetric, IntroscopeMetricTestResult>>() {
                }).toInstance(service);
            }
        });
    }
}
