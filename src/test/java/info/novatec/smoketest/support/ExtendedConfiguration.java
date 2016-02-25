package info.novatec.smoketest.support;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public class ExtendedConfiguration extends TestConfiguration {

    public static String TEST_STRING_VALUE = "configurationValue";

    public String configurationValue = "configurationValue";

    public String getConfigurationValue() {
        return configurationValue;
    }

}
