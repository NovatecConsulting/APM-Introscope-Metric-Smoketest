package info.novatec.smoketest.core.service;

import com.beust.jcommander.internal.Maps;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import info.novatec.smoketest.core.SmokeTestConfiguration;
import info.novatec.smoketest.core.application.configuration.ConfigurationException;
import info.novatec.smoketest.core.application.configuration.ConfigurationFactory;
import info.novatec.smoketest.core.application.configuration.IConfigurationFactory;
import info.novatec.smoketest.core.model.TimeRange;
import info.novatec.smoketest.core.service.time.TimeService;
import info.novatec.smoketest.core.util.Configurations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class TimeServiceTest {

    IConfigurationFactory<SmokeTestConfiguration> factory;

    @BeforeClass
    public void prepareFactory() {
        factory = new ConfigurationFactory<SmokeTestConfiguration>()
                .initialize(new ObjectMapper().registerModule(new JavaTimeModule()), SmokeTestConfiguration.class);
    }

    @Test
    public void testWithEmptyRange() {
        SmokeTestConfiguration configuration = new SmokeTestConfiguration();
        TimeService timeService = new TimeService(configuration);
        TimeRange timeRange = timeService.getTimeRange();
        assertNotNull(timeRange);
        assertNotNull(timeRange.getFrom());
        assertNotNull(timeRange.getTo());
    }

    @Test
    public void testToCreation() {
        try {
            Map<String, String> overrides = Maps.newHashMap();
            overrides.put("timeRange.from", ZonedDateTime.now().toString());
            SmokeTestConfiguration configuration = factory.create(overrides);

            String s = Configurations.toString(configuration);
            System.out.println(s);

            TimeService timeService = new TimeService(configuration);
            TimeRange finalRange = timeService.getTimeRange();
            assertNotNull(finalRange);
            assertEquals(finalRange.getTo(), finalRange.getFrom().plusHours(configuration.getTimeOffset()));
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testFromCreation() {
        Map<String, String> overrides = Maps.newHashMap();
        overrides.put("timeRange.to", ZonedDateTime.now().toString());
        SmokeTestConfiguration configuration;
        try {
            configuration = factory.create(overrides);
            TimeService timeService = new TimeService(configuration);

            assertEquals(timeService.getTimeRange().getFrom(),
                    timeService.getTimeRange().getTo().minusHours(configuration.getTimeOffset()));
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFormat() {
        ZonedDateTime date = ZonedDateTime.of(2016, 2, 2, 0, 0, 1, 0, ZoneId.of("Europe/Berlin"));
        SmokeTestConfiguration cfg = new SmokeTestConfiguration();
        cfg.setLocale(Locale.GERMAN);
        TimeService timeService = new TimeService(cfg);
        assertEquals(timeService.format("EEE MMM dd HH:mm:ss zzz yyyy", date), "Di Feb 02 00:00:01 MEZ 2016");
        assertEquals(timeService.format("yyyy MM dd", date), "2016 02 02");
        assertEquals(timeService.format("yyyy/MM/dd", date), "2016/02/02");
        assertEquals(timeService.format("yyyy/MM/dd HH:mm:ss", date), "2016/02/02 00:00:01");
    }
}
