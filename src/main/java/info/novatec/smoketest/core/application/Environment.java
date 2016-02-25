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

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.util.Modules;
import info.novatec.smoketest.core.application.configuration.Configuration;
import info.novatec.smoketest.core.util.PostConstructListenerModule;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.Arrays;
import java.util.List;

/**
 * An Environment represents an executable smoketest.
 *
 * @param <T>
 *         The type of the required IConfiguration
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 * @see Configuration
 */
public class Environment<T extends Configuration> {

    /**
     * The {@link Setup} which is used to create this Environment.
     */
    private Setup<T> setup;

    /**
     * The Guice Injector which is used to bind all Services/Components within an application.
     */
    private Injector injector;

    /**
     * All test which shall be executed by this SmokeTest.
     */
    private List<Class<?>> tests;

    /**
     * Creates a new Environment. Although this is a public constructor it is recommended to use the internal {@link
     * Builder} to create environments.
     *
     * @param setup
     *         The Setup
     * @param tests
     *         The test classes to be executed
     * @param modules
     *         The modules to create the guice injector
     */
    public Environment(final Setup<T> setup,
                       final List<Class<?>> tests,
                       final List<Module> modules) {
        this.initialize(setup, tests, modules);
    }

    /**
     * Gets {@link #setup#getName()}.
     *
     * @return {@link #setup#getName()()}
     */
    public String getName() {
        return setup.getName();
    }

    /**
     * Gets {@link #setup#getConfiguration()}.
     *
     * @return {@link #setup#getConfiguration()}
     */
    public T getConfiguration() {
        return setup.getConfiguration();
    }

    /**
     * Gets {@link #setup}.
     *
     * @return {@link #setup}
     */
    public Setup<T> getSetup() {
        return setup;
    }

    /**
     * Gets {@link #injector}.
     *
     * @return {@link #injector}
     */
    public Injector getInjector() {
        return injector;
    }

    /**
     * Gets {@link #tests}.
     *
     * @return {@link #tests}
     */
    public List<Class<?>> getTests() {
        return tests;
    }

    /**
     * <p> Runs the Environment. Running the environment basically means to call {@link Bundle#run(Environment)} on all
     * bundles. Before a bundle is executed it gets all its dependencies injected. </p> <p> After successful running
     * this environment it will not be destroyed. All service are sill available and can safely be accessed </p>
     *
     * @return The executed Environment
     */
    public Environment<T> run() {
        setup.getBundles()
                .forEach(bundle -> {
                    getInjector().injectMembers(bundle);
                    bundle.run(this);
                });
        return this;
    }

    //-------------------------------------------------------------
    // Methods: Internals
    //-------------------------------------------------------------

    /**
     * Initializes the environment by creating the Gucie Injector and the ServiceManager.
     *
     * @param setup
     *         The Setup providing all required bundles and the configuration.
     * @param tests
     *         The test classes to be executed
     * @param modules
     *         The modules to create the Injector
     */
    private void initialize(Setup<T> setup,
                            List<Class<?>> tests,
                            List<Module> modules) {
        this.setup = setup;
        this.tests = tests;
        this.injector = createInjector(modules);
    }

    /**
     * Create a new Guice Injector. The injector is created form all Modules provided by all bundles. Additionally,
     * Namespace, IConfiguration and Environment are made available via the Injector.
     *
     * @param modules
     *         The modules to seed the Injector
     * @return New Injector
     */
    private Injector createInjector(List<Module> modules) {
        Module runtimeModule = new AbstractModule() {
            @Override
            protected void configure() {
                install(new PostConstructListenerModule());
                bind(Environment.class).toInstance(Environment.this);
                bind(Namespace.class).toInstance(setup.getNamespace());
                bind(Configuration.class).toInstance(setup.getConfiguration());

                //At this point the one and only configuration is bound to all classes up the inheritance hierarchy.
                //So we can assure that it is possible to injected the same configuration instance in several bundles
                //with the correct type.
                //It is a safe cast since Setup will never accept configurations not
                //implementing IConfiguration interface
                @SuppressWarnings("unchecked")
                Class<? super T> configClazz = ((Class<? super T>) setup.getConfiguration().getClass());
                while (configClazz != null && Configuration.class.isAssignableFrom(configClazz)) {
                    bind(configClazz).toInstance(setup.getConfiguration());
                    configClazz = configClazz.getSuperclass();
                }
            }
        };

        //Since bundles are encouraged to override service, we construct one final Module by
        //overriding all modules.
        for (Module override : modules) {
            runtimeModule = Modules.override(runtimeModule).with(override);
        }
        return Guice.createInjector(Stage.PRODUCTION, runtimeModule);
    }


    /**
     * Internal builder providing convenience functions to setup environments. It is strongly recommended to use the
     * builder to create environments.
     *
     * @param <T>
     *         The type of the related configuration
     */
    public static class Builder<T extends Configuration> {

        /**
         * All tests to be executed by this SmokeTest.
         */
        private List<Class<?>> testClasses;

        /**
         * All {@link Module}s which are are use to create the Guice Injector.
         */
        private List<Module> modules;

        /**
         * The related {@link Setup} this Environment bases on.
         */
        private Setup<T> setup;

        /**
         * Create a new Builder.
         *
         * @param setup
         *         the relate Setup.
         */
        public Builder(final Setup<T> setup) {
            this.setup = setup;
            this.modules = Lists.newLinkedList();
            this.testClasses = Lists.newArrayList();
        }

        /**
         * Method can be used to provide an additional guice Module to override certain service.
         *
         * @param module
         *         The module containing custom service implementations
         * @return The Builder itself for chaining
         * @see Module
         */
        public Builder<T> services(final Module module) {
            this.modules.add(module);
            return this;
        }

        /**
         * Adds new tests to the Environment.
         *
         * @param tests
         *         The tests
         * @return The Builder itself for chaining
         */
        public Builder<T> tests(final Class<?>... tests) {
            this.tests(Arrays.asList(tests));
            return this;
        }

        /**
         * Adds a list of tests to the environment.
         *
         * @param tests
         *         The tests to be executed
         * @return The Builder itself for chaining
         */
        public Builder<T> tests(final List<Class<?>> tests) {
            this.testClasses.addAll(tests);
            return this;
        }

        /**
         * @return The Namespace
         */
        public Namespace getNamespace() {
            return setup.getNamespace();
        }

        /**
         * @return The related configuration object
         */
        public T getConfiguration() {
            return setup.getConfiguration();
        }

        /**
         * Builds the Environment in two steps. <p> As first step all available bundles get the change to add additional
         * configurations to the Environment. Therefor {@link Bundle#configure(Environment.Builder)} is invoked in each
         * bundle. Be aware that the bundles are called in the same order as them were added to the Setup. This is
         * especially import if service bindings should be overwritten by the use of Modules. </p> <p> As second step
         * the Guice injector is created. The resulting injector depends on all modules provided by all bundles. In
         * addition a Module providing Namespace, Setup, IConfiguration and Environment is added. This means that all
         * those components are valid to be injected in tests and bundles. Afterwards the Environment is created and
         * initialized. </p>
         *
         * @return The new Environment
         */
        public Environment<T> build() {
            setup.getBundles().forEach(bundle -> bundle.configure(this));
            return new Environment<>(setup, testClasses, modules);
        }
    }
}
