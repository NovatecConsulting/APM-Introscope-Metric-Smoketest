package info.novatec.smoketest.support;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public interface IInterface {

    default public String getName() {
        return getClass().getName();
    }

}
