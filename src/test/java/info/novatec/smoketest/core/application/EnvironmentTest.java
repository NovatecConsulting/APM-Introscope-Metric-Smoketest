package info.novatec.smoketest.core.application;

import com.google.inject.Injector;
import info.novatec.smoketest.support.BundleProvidingModule2;
import info.novatec.smoketest.support.BundleWithInjectedInterface;
import info.novatec.smoketest.support.ExtendedConfiguration;
import info.novatec.smoketest.support.IInterface;
import info.novatec.smoketest.support.Impl2;
import info.novatec.smoketest.support.Impl3;
import info.novatec.smoketest.support.Modules;
import info.novatec.smoketest.support.TestConfiguration;
import info.novatec.smoketest.support.TestWithOneMetricOneResultOnLevel0;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class EnvironmentTest {

    @Test
    public void testDefaultEnvironment() {
        Setup<TestConfiguration> setup = new Setup.Builder<TestConfiguration>()
                .configurationClazz(TestConfiguration.class)
                .build();
        Environment<TestConfiguration> environment =
                new Environment.Builder<>(setup).build();

        assertNotNull(environment.getConfiguration());
        assertNotNull(environment.getInjector());
    }

    @Test
    public void testOverrideServices() {
        Setup<ExtendedConfiguration> setup =
                new Setup.Builder<ExtendedConfiguration>()
                        .configurationClazz(ExtendedConfiguration.class)
                        .build();

        Environment<ExtendedConfiguration> environment =
                new Environment.Builder<>(setup)
                        .services(new Modules.Module1())
                        .services(new Modules.Module2())
                        .services(new Modules.Module3())
                        .build();

        Injector injector = environment.getInjector();
        assertNotNull(environment.getConfiguration());

        IInterface instance = injector.getInstance(IInterface.class);
        assertTrue(instance instanceof Impl3);

        ExtendedConfiguration configuration = injector.getInstance(ExtendedConfiguration.class);
        assertEquals(configuration, setup.getConfiguration());
    }

    @Test
    public void testAddTestClasses() {
        Setup<TestConfiguration> setup = new Setup.Builder<TestConfiguration>()
                .configurationClazz(TestConfiguration.class)
                .build();

        Environment<TestConfiguration> environment = new Environment.Builder<>(setup)
                .tests(TestWithOneMetricOneResultOnLevel0.class)
                .build();

        assertEquals(environment.getTests().size(), 1);
        assertEquals(environment.getTests().get(0), TestWithOneMetricOneResultOnLevel0.class);
    }

    @Test
    public void testEnvironmentSetupFromBundleTwoBundles() {

        BundleWithInjectedInterface<ExtendedConfiguration> starterBundle
                = new BundleWithInjectedInterface<ExtendedConfiguration>() {
            @Override
            public String getName() {
                return "RootBundle";
            }
        };

        Setup<ExtendedConfiguration> setup = new Setup.Builder<>(starterBundle)
                .addBundle(new BundleProvidingModule2<>())
                .build();
        Environment<ExtendedConfiguration> environment = new Environment.Builder<>(setup)
                .build();

        assertTrue(environment.getInjector().getInstance(IInterface.class) instanceof Impl2);
        assertEquals(setup.getName(), "RootBundle");

        environment.run();

        assertNotNull(starterBundle.getInjected());
        assertTrue(starterBundle.getInjected() instanceof Impl2);
    }
}
