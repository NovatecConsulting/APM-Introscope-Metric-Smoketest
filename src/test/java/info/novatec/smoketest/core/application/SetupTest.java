package info.novatec.smoketest.core.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import info.novatec.smoketest.support.ExtendedConfiguration;
import info.novatec.smoketest.support.TestConfiguration;
import net.sourceforge.argparse4j.inf.Namespace;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class SetupTest {

    @Test
    public void testSimpleSetup() {
        Setup<TestConfiguration> setup = new Setup.Builder<TestConfiguration>()
                .configurationClazz(TestConfiguration.class).build();

        assertNotNull(setup.getObjectMapper());
        assertNotNull(setup.getConfiguration());
        assertNotNull(setup.getName());
        assertNotNull(setup.getBundles());
        assertNotNull(setup.getNamespace());
    }

    @Test
    public void testSetupWithArguments() {
        Setup.Builder<TestConfiguration> builder = new Setup.Builder<TestConfiguration>()
                .configurationClazz(TestConfiguration.class);
        builder.getParser()
                .addArgument("-a", "--argument")
                .nargs("?")
                .help("An argument");

        builder.getParser()
                .addArgument("-b", "--arguments")
                .nargs("+")
                .help("A list of arguments");

        Setup<TestConfiguration> setup = builder.build("--argument", "first", "-b", "second", "third");
        Namespace namespace = setup.getNamespace();
        assertEquals(namespace.getAttrs().size(), 2);
        assertEquals(namespace.get("argument"), "first");
        assertEquals(namespace.get("arguments"), Arrays.asList("second", "third"));
    }

    @Test
    public void testSetupConfiguredFromFileAndArguments() {
        Setup.Builder<TestConfiguration> builder = new Setup.Builder<TestConfiguration>()
                .configurationClazz(TestConfiguration.class);
        builder.getParser()
                .addArgument("-c", "--configuration")
                .nargs("?")
                .help("The configuration file.");
        builder.getParser()
                .addArgument("-o", "--override")
                .nargs("+")
                .help("Values to be overwritten in configuration!");

        Setup<TestConfiguration> setup = builder.build("-c", "defaultConfiguration.yml", "-o",
                "reportDirectory=custom");
        assertEquals(setup.getConfiguration().getTimeOffset(), 3);
        assertEquals(setup.getConfiguration().getReportDirectory(), "custom");
    }


    @Test
    public void testSetupOverrideFromBundle() {
        ObjectMapper mapper = new ObjectMapper();

        Setup.Builder<ExtendedConfiguration> builder = new Setup.Builder<>(
                new Bundle<ExtendedConfiguration>() {
                    @Override
                    public String getName() {
                        return "MySmokeTest";
                    }

                    @Override
                    public void setup(Setup.Builder<ExtendedConfiguration> builder) {
                        builder.addBundle(new Bundle<TestConfiguration>() {
                            @Override
                            public void setup(Setup.Builder<TestConfiguration> setup) {
                                setup.objectMapper(mapper);
                            }
                        });
                    }
                });

        Setup<ExtendedConfiguration> setup = builder.build();
        assertEquals(mapper, setup.getObjectMapper());
        assertEquals(setup.getBundles().size(), 2);
        assertEquals(setup.getName(), "MySmokeTest");
    }

    @Test
    public void testSetupWithExtendedConfiguration() {
        Setup<ExtendedConfiguration> setup = new Setup.Builder<ExtendedConfiguration>()
                .configurationClazz(ExtendedConfiguration.class)
                .build();
        assertEquals(setup.getConfiguration().configurationValue, ExtendedConfiguration.TEST_STRING_VALUE);
    }

    @Test
    public void testBundleExecutionOrder() {
        List<String> prepareOrder = new ArrayList<>();

        Setup.Builder<TestConfiguration> builder = new Setup.Builder<>(new Bundle<TestConfiguration>() {
            @Override
            public void setup(Setup.Builder<TestConfiguration> setup) {
                prepareOrder.add("B0");
            }
        });
        builder.addBundle(new Bundle<TestConfiguration>() {
            @Override
            public void setup(Setup.Builder<TestConfiguration> setup) {
                prepareOrder.add("B1");
            }
        });
        builder.addBundle(new Bundle<TestConfiguration>() {
            @Override
            public void setup(Setup.Builder<TestConfiguration> setup) {
                setup.addBundle(new Bundle<TestConfiguration>() {
                    @Override
                    public void setup(Setup.Builder<TestConfiguration> setup) {
                        prepareOrder.add("B2.Inner1");
                    }
                });
                prepareOrder.add("B2");
            }
        });

        builder.build();
        assertEquals(prepareOrder, new ArrayList<>(Arrays.asList("B1", "B2.Inner1", "B2", "B0")));
    }

}
