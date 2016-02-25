package info.novatec.smoketest.support;

import info.novatec.smoketest.core.application.Bundle;
import info.novatec.smoketest.core.application.Environment;
import info.novatec.smoketest.core.application.configuration.Configuration;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class BundleProvidingModule2<T extends Configuration> extends Bundle<T> {
    @Override
    public void configure(Environment.Builder<T> builder) {
        builder.services(new Modules.Module2());
    }
}
