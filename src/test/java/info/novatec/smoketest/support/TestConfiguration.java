package info.novatec.smoketest.support;

import info.novatec.smoketest.core.application.configuration.Configuration;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class TestConfiguration extends Configuration {

    private int timeOffset = 4;
    private String reportDirectory = "test-results";

    public int getTimeOffset() {
        return timeOffset;
    }

    public String getReportDirectory() {
        return reportDirectory;
    }
}
