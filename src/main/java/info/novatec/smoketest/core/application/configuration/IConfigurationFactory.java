/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 NovaTec Consulting GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package info.novatec.smoketest.core.application.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.Map;

/**
 * Factory to create {@link Configuration}s.
 *
 * @param <T>
 *         The type of the {@link Configuration}.
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public interface IConfigurationFactory<T extends Configuration> {

    /**
     * Initializes the IConfigurationFactory with an {@link ObjectMapper} instance and the target clazz.
     *
     * @param mapper
     *         The Jackson ObjectMapper to be used to deserialize YAML to POJOs.
     * @param targetClazz
     *         The target class which kind of configuration object will be created.
     * @return Self for chaining methods
     * @throws NullPointerException
     *         if object mapper or target clazz is null
     * @throws IllegalArgumentException
     *         if the target class is not instantiable
     */
    IConfigurationFactory<T> initialize(final ObjectMapper mapper,
                                        final Class<T> targetClazz);

    /**
     * Creates a default configuration of the target class.
     *
     * @return The new {@link Configuration}
     * @throws ConfigurationException
     *         if creation fails
     */
    T create() throws ConfigurationException;

    /**
     * Creates a new {@link Configuration} based on a Namespace object. A Namespace object reflects the command lind
     * arguments of the application. Which and how the command line arguments are processed depends on the explicit
     * implementation.
     *
     * @param namespace
     *         The Namespace object
     * @return The new {@link Configuration}
     * @throws ConfigurationException
     *         if creation fails
     */
    T create(Namespace namespace) throws ConfigurationException;

    /**
     * Creates a new {@link Configuration} based on the target class, but overrides certain properties defined in a Map
     * as key/value pairs.
     *
     * @param overrides
     *         The Map which contains the key/value pairs to be added to the configuration.
     * @return The new {@link Configuration}
     * @throws ConfigurationException
     *         if creation fails
     */
    T create(Map<String, String> overrides) throws ConfigurationException;

    /**
     * Creates a new {@link Configuration} based on a YAML file.
     *
     * @param file
     *         The file to be deserialized to a {@link Configuration} of the target clazz.
     * @param overrides
     *         The Map which contains the key/value pairs to be added to the configuration.
     * @return The new {@link Configuration}
     * @throws ConfigurationException
     *         if creation fails
     */
    T create(String file,
             Map<String, String> overrides) throws ConfigurationException;
}
