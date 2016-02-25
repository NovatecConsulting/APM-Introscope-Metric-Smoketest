package info.novatec.smoketest.support;

import info.novatec.smoketest.core.application.Bundle;
import info.novatec.smoketest.core.application.Environment;
import info.novatec.smoketest.core.application.Setup;
import info.novatec.smoketest.core.application.configuration.Configuration;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class Support {


    @SafeVarargs
    public static <T extends Configuration> Environment<T> readyEnvironment(final Class<T> configClazz,
                                                                            final Bundle<T>... bundles) {
        Setup.Builder<T> builder = new Setup.Builder<T>()
                .configurationClazz(configClazz);
        for (Bundle<T> bundle : bundles) {
            builder.addBundle(bundle);
        }
        Setup<T> setup = builder.build();
        return new Environment.Builder<>(setup).build();
    }


}
