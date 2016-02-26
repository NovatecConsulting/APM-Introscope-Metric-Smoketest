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

package info.novatec.smoketest.core.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import info.novatec.smoketest.core.application.configuration.Configuration;
import info.novatec.smoketest.core.application.configuration.ConfigurationException;
import info.novatec.smoketest.core.application.configuration.ConfigurationFactory;
import info.novatec.smoketest.core.application.configuration.IConfigurationFactory;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p> A Setup is the core of each application. The purpose of the Setup is to collect and prepare all required bundles
 * of this application. Also it is the initial point to define essential components to properer setup and
 * initialize the IConfiguration. </p>
 * <p> To ease the creation of a Setup it provides an internal Builder class. The
 * builder provides a fluent API and encapsulates all required logic to prepare bundles and create configuration.
 * </p>
 *
 * @param <T>
 *         The type of the required configuration
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 * @see Configuration
 * @see ObjectMapper
 * @see IConfigurationFactory
 */
public class Setup<T extends Configuration> {

    /**
     * List of all {@link Bundle}s this Setup assembles.
     */
    private List<Bundle<T>> bundles;

    /**
     * The {@link Namespace} representing the command line arguments.
     */
    private Namespace namespace;

    /**
     * ObjectMapper to marshal/unmarshal YAML filesv.
     */
    private ObjectMapper objectMapper;

    /**
     * The {@link Configuration} of this application.
     */
    private T configuration;

    /**
     * The name of the application. The name is used to setup the command line parser.
     */
    private String name;

    /**
     * Creates a new Setup instance.
     *
     * @param name
     *         The application name
     * @param configuration
     *         The application configuration
     * @param namespace
     *         The Namespace representing the command line arguments
     * @param objectMapper
     *         The Jackson ObjectMapper
     * @param bundles
     *         All Bundles to setup the application
     * @see Bundle
     * @see ObjectMapper
     * @see Namespace
     */
    public Setup(String name,
                 T configuration,
                 Namespace namespace,
                 ObjectMapper objectMapper,
                 List<Bundle<T>> bundles) {
        this.name = name;
        this.configuration = configuration;
        this.namespace = namespace;
        this.objectMapper = objectMapper;
        //Set of bundles is now fixed
        this.bundles = ImmutableList.copyOf(bundles);
    }

    /**
     * Gets {@link #bundles}.
     *
     * @return {@link #bundles}
     */
    public List<Bundle<T>> getBundles() {
        return bundles;
    }

    /**
     * Gets {@link #namespace}.
     *
     * @return {@link #namespace}
     */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * Gets {@link #objectMapper}.
     *
     * @return {@link #objectMapper}
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Gets {@link #configuration}.
     *
     * @return {@link #configuration}
     */
    public T getConfiguration() {
        return configuration;
    }

    /**
     * Gets {@link #name}.
     *
     * @return {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * Internal Builder class to ease creation of new Setups.
     *
     * @param <T>
     *         The type of the related IConfiguration
     * @see Configuration
     */
    public static class Builder<T extends Configuration> {

        /**
         * The slf4j Logger.
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);

        /**
         * Map to collect added bundles.
         */
        private Map<String, Bundle<? super T>> bundleMap;

        /**
         * The {@link IConfigurationFactory}.
         */
        private IConfigurationFactory<T> configurationFactory;

        /**
         * The Jackson {@link ObjectMapper}.
         */
        private ObjectMapper objectMapper;

        /**
         * The root bundle.
         */
        private Bundle<T> rootBundle;

        /**
         * The class which is used to create the configuration. By default this extracted form the generic type
         * information.
         */
        private Class<T> configClazz;

        /**
         * The name of the application to be set up.
         */
        private String name;

        /**
         * The {@link Configuration}.
         */
        private T configuration;

        /**
         * The ArgumentParser is used to defined the command line interface of the application related to this Setup.
         * The ArgumentParser is available to all Bundles, so the CLI is modifiable from each bundle.
         */
        private ArgumentParser parser;

        /**
         * NoArgs Constructor.
         */
        public Builder() {
            this("SmokeTest");
        }

        /**
         * Create a new Builder.
         *
         * @param name
         *         The name of the application
         */
        public Builder(final String name) {
            this.init(name, null);
        }

        /**
         * Creates a new Builder.
         *
         * @param rootBundle
         *         Bundle is use to extract the application name as well as the Type of required configuration.
         * @throws NullPointerException
         *         if bundle is null
         */
        public Builder(final Bundle<T> rootBundle) {
            this.rootBundle = checkNotNull(rootBundle, "The bundle must not be null!");
            this.init(this.rootBundle.getName(), determineConfigurationClass(this.rootBundle.getClass()));
        }


        /**
         * Adds an already constructed {@link Configuration} to the Setup. Be aware that if a configuration is provided
         * the IConfigurationFactory and (if available) the configurationClazz are omitted.
         *
         * @param configuration
         *         The configuration to be used.
         * @return The builder itself for chaining
         * @see Configuration
         */
        public Builder<T> configuration(final T configuration) {
            this.configuration = configuration;
            return this;
        }

        /**
         * Sets the class which is needed by the IConfigurationFactory to properly set up the IConfiguration. Can be
         * omitted if bundle is provided to Constructor.
         *
         * @param clazz
         *         The configuration class
         * @return The Builder itself for chaining methods
         * @throws NullPointerException
         *         if clazz is null
         * @see IConfigurationFactory
         */
        public Builder<T> configurationClazz(final Class<T> clazz) {
            this.configClazz = checkNotNull(clazz, "Clazz must not be null!");
            return this;
        }

        /**
         * Sets the IConfigurationFactory.
         *
         * @param factory
         *         The factory instance.
         * @return The Builder itself for chaining method
         * @throws NullPointerException
         *         if factory is null
         */
        public Builder<T> configurationFactory(final IConfigurationFactory<T> factory) {
            this.configurationFactory = checkNotNull(factory, "IConfigurationFactory must not be null!");
            return this;
        }

        /**
         * Sets the ObjectMapper.
         *
         * @param mapper
         *         The ObjectMapper instance.
         * @return The Builder itself for chaining method
         * @throws NullPointerException
         *         if objectMapper is null
         */
        public Builder objectMapper(final ObjectMapper mapper) {
            this.objectMapper = checkNotNull(mapper, "ObjectMapper must not be null!");
            return this;
        }

        /**
         * Gets the ArgumentParser.
         *
         * @return The ArgumentParser
         */
        public ArgumentParser getParser() {
            return parser;
        }

        /**
         * Adds a new Bundle to the Setup. As soon as the bundle is added <code>Bundle#prepare</code> is invoked.
         *
         * @param bundle
         *         The
         * @return The Builder itself for chaining method
         * @throws NullPointerException
         *         if bundle is null
         * @see Bundle
         */
        @SuppressWarnings("unchecked")//We are safe to suppress this warnings.
        //It can not happen that incompatible configuration types are registered.
        public Builder<T> addBundle(final Bundle<? super T> bundle) {
            checkNotNull(bundle, "Bundle must not be null!");
            if (bundleMap.containsKey(bundle.getName())) {
                LOGGER.warn("Skipping Bundle: {}. Bundle already registered!", bundle.getName());
            } else {
                bundle.setup((Setup.Builder) this);
                bundleMap.put(bundle.getName(), bundle);
            }
            return this;
        }

        /**
         * Builds the Setup in three steps. <p> At first, if provided, the rootBundle is added to the list of bundles.
         * This means that the {@link Bundle#setup(Setup.Builder)} method of the rootBundle is invoked. If the
         * rootBundle itself adds more bundles, those will be prepared as well. </p> <p> As second step the Namespace
         * representing the command line arguments is created. In case of invalid or unknown arguments a help is printed
         * to the command line. Be aware that if Namespace could not be setup, no Setup is built and null is returned.
         * </p> <p> The final step while building the Setup is creating the {@link Configuration} via the
         * IConfigurationFactory. In case of a misconfiguration this will fail and build process is interrupted. </p>
         *
         * @param args
         *         The command line arguments used to setup the Namespace
         * @return A fully configured Setup instance, or null in case of errors.
         * @throws IllegalStateException
         *         if no configuration clazz is provided.
         */
        public Setup<T> build(final String... args) {
            if (rootBundle != null) {
                addBundle(rootBundle);
            }
            try {
                Namespace namespace = getParser().parseArgs(args);
                try {
                    if (configuration == null) {
                        if (configClazz == null) {
                            throw new IllegalStateException("Neither a configuration, nor a configuration type was "
                                    + "provided!");
                        }
                        configuration = configurationFactory
                                .initialize(objectMapper, configClazz)
                                .create(namespace);

                        LOGGER.info(configuration.toString());
                    }
                    @SuppressWarnings("unchecked")//It must not happen that IConfiguration types are incompatible
                            List<Bundle<T>> strongTyped = bundleMap.entrySet().stream()
                            .map(entry -> (Bundle<T>) entry.getValue())
                            .collect(Collectors.toList());

                    return new Setup<>(name,
                            configuration,
                            namespace,
                            objectMapper,
                            strongTyped);
                } catch (ConfigurationException e) {
                    throw new RuntimeException(e);
                }
            } catch (ArgumentParserException e) {
                parser.handleError(e);
            }
            return null;
        }

        //-------------------------------------------------------------
        // Methods: Internals
        //-------------------------------------------------------------

        /**
         * Initializes the Setup with default values.
         *
         * @param name
         *         The name of the application.
         * @param configurationClazz
         *         The type of the IConfiguration
         */
        private void init(final String name,
                          final Class<T> configurationClazz) {
            this.name = name;
            this.configClazz = configurationClazz;
            this.configurationFactory = new ConfigurationFactory<>();
            this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            this.parser = ArgumentParsers.newArgumentParser(this.name, true);
            this.bundleMap = Maps.newLinkedHashMap();
        }

        /**
         * Tries to extract the type of the required configuration out of the generic type parameters of a class.
         *
         * @param clazz
         *         The class to be inspected
         * @param <T>
         *         The requested type
         * @return The type of the configuration, or null if type could not be determined
         */
        @SuppressWarnings("unchecked")
        private static <T> Class<T> determineConfigurationClass(final Class<?> clazz) {
            Type type = clazz;
            //1. Step up the inheritance hierarchy, as long as super type is still generic
            while (type instanceof Class<?>) {
                type = ((Class) type).getGenericSuperclass();
            }

            if (type instanceof ParameterizedType) {
                for (Type parameter : ((ParameterizedType) type).getActualTypeArguments()) {
                    //parameter is Class<?> if the class is defined like
                    // public class MyClass<T extends IConfiguration>
                    if (parameter instanceof Class<?>) {
                        if (Configuration.class.isAssignableFrom((Class<?>) parameter)) {
                            return (Class<T>) parameter;
                        }
                    } else if (parameter instanceof TypeVariable) {
                        //parameter is TypeVariable if the class is defined like
                        // public class MyClass<T extends IConfiguration> extends Bundle<T>
                        //in this Case we need to handle the T as TypeVariable and operate on the bounds
                        for (Type bound : ((TypeVariable<?>) parameter).getBounds()) {
                            if (bound instanceof Class<?>) {
                                if (Configuration.class.isAssignableFrom((Class<?>) bound)) {
                                    return (Class<T>) bound;
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
    }
}
