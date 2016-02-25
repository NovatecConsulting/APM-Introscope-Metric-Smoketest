package info.novatec.smoketest.core.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import info.novatec.smoketest.core.application.configuration.ConfigurationException;
import info.novatec.smoketest.core.application.configuration.ConfigurationFactory;
import info.novatec.smoketest.core.application.configuration.IConfigurationFactory;
import info.novatec.smoketest.support.ExtendedConfiguration;
import info.novatec.smoketest.support.TestConfiguration;
import net.sourceforge.argparse4j.inf.Namespace;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class ConfigurationFactoryTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void beforeClass() {
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Test
    public void testCreateDefaultConfiguration() {
        IConfigurationFactory<TestConfiguration> factory = new ConfigurationFactory<TestConfiguration>()
                .initialize(mapper, TestConfiguration.class);
        try {
            TestConfiguration config = factory.create();
            assertEquals(config.getTimeOffset(), 4);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateDefaultConfigurationFromFile() {
        IConfigurationFactory<TestConfiguration> factory = new ConfigurationFactory<TestConfiguration>()
                .initialize(mapper, TestConfiguration.class);
        try {
            TestConfiguration config = factory.create("defaultConfiguration.yml", null);
            assertEquals(config.getTimeOffset(), 3);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateDefaultConfigurationFromNamespace() {
        IConfigurationFactory<TestConfiguration> factory = new ConfigurationFactory<TestConfiguration>()
                .initialize(mapper, TestConfiguration.class);
        try {
            Map<String, Object> ns = new HashMap<>();
            ns.put("configuration", "defaultConfiguration.yml");
            ns.put("override", Collections.singletonList("timeOffset=24"));
            TestConfiguration config = factory.create(new Namespace(ns));
            assertEquals(config.getTimeOffset(), 24);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateExtendConfigurationFromNamespace() {
        IConfigurationFactory<ExtendedConfiguration> factory = new ConfigurationFactory<ExtendedConfiguration>()
                .initialize(mapper, ExtendedConfiguration.class);
        try {
            Map<String, Object> ns = new HashMap<>();
            ns.put("configuration", "extendedConfiguration.yml");
            ExtendedConfiguration config = factory.create(new Namespace(ns));
            assertEquals(config.getConfigurationValue(), "FromFile");
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }

    }
}

