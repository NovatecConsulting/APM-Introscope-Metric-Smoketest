package info.novatec.smoketest.support;

import com.google.inject.AbstractModule;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class Modules {

    public static class Module1 extends AbstractModule {

        @Override
        protected void configure() {
            bind(IInterface.class).to(Imp1.class);
        }
    }

    public static class Module2 extends AbstractModule {

        @Override
        protected void configure() {
            bind(IInterface.class).to(Impl2.class);
        }
    }

    public static class Module3 extends AbstractModule {

        @Override
        protected void configure() {
            bind(IInterface.class).to(Impl3.class);
        }
    }

}
