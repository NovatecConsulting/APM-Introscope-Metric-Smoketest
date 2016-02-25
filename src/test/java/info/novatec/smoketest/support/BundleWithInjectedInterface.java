package info.novatec.smoketest.support;

import info.novatec.smoketest.core.application.Bundle;
import info.novatec.smoketest.core.application.configuration.Configuration;

import javax.inject.Inject;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class BundleWithInjectedInterface<T extends Configuration> extends Bundle<T> {

    @Inject
    private IInterface injected;

    public IInterface getInjected() {
        return injected;
    }
}
