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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import info.novatec.smoketest.core.util.ResourceUtils;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the {@link IConfigurationFactory} interface.
 *
 * @param <T>
 *         The type of the {@link Configuration} this factory will create.
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 * @see IConfigurationFactory
 */
public class ConfigurationFactory<T extends Configuration> implements IConfigurationFactory<T> {

    /**
     * The Jackson ObjectMapper to serialize/deserialize {@link Configuration}s.
     */
    private ObjectMapper mapper;

    /**
     * The target class of type T this factory creates.
     */
    private Class<T> clazz;

    /**
     * The YAMLFactory the parse YAML files.
     */
    private YAMLFactory factory;

    /**
     * No-Args Constructor.
     */
    public ConfigurationFactory() {
        factory = new YAMLFactory();
    }

    //-------------------------------------------------------------
    // Interface Implementation: IConfigurationFactory
    //-------------------------------------------------------------

    @Override
    public IConfigurationFactory<T> initialize(final ObjectMapper mapper,
                                               final Class<T> targetClazz) {
        this.mapper = Preconditions.checkNotNull(mapper, "The ObjectMapper must not be null");
        this.clazz = Preconditions.checkNotNull(targetClazz, "The target clazz must not be null");
        boolean isInstantiable = (!clazz.isInterface()) && (!Modifier.isAbstract(clazz.getModifiers()));
        Preconditions.checkArgument(isInstantiable, targetClazz.getName() + " is not instantiable!");
        return this;
    }

    @Override
    public T create(final Namespace namespace) throws ConfigurationException {
        String configurationFile = namespace.getString("configuration");
        Map<String, String> overrides = new HashMap<>();
        if (namespace.getList("override") != null) {
            for (String override : namespace.<String>getList("override")) {
                String[] parts = override.split("=");
                if (parts.length == 2) {
                    overrides.put(parts[0], parts[1]);
                } else if (parts.length == 1) {
                    //Reset
                    overrides.put(parts[0], "");
                } else {
                    throw new ConfigurationException("Invalid override: " + override);
                }
            }
        }
        if (Strings.isNullOrEmpty(configurationFile)) {
            try {
                return build(mapper.valueToTree(clazz.newInstance()), overrides);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ConfigurationException("Could not instantiate class: " + clazz.getName());
            }
        }
        return create(configurationFile, overrides);
    }

    @Override
    public T create() throws ConfigurationException {
        return create(new HashMap<>());
    }

    @Override
    public T create(Map<String, String> overrides) throws ConfigurationException {
        try {
            return build(mapper.valueToTree(clazz.newInstance()), overrides);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ConfigurationException("Could not instantiate class: " + clazz.getName());
        }
    }

    @Override
    public T create(final String file,
                    final Map<String, String> overrides) throws ConfigurationException {
        InputStream inputStream;
        try {
            inputStream = ResourceUtils.openFileStream(file);
            return build(mapper.readTree(factory.createParser(inputStream)), overrides);
        } catch (IOException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }


    //-------------------------------------------------------------
    // Methods: Internal
    //-------------------------------------------------------------

    /**
     * Creates the requested configuration.
     *
     * @param root
     *         The top level JsonNode
     * @param overrides
     *         The list of overrides to be applied
     * @return A new configuration object
     * @throws ConfigurationException
     *         if creation fails
     */
    private T build(final JsonNode root,
                    final Map<String, String> overrides) throws ConfigurationException {
        if (overrides != null) {
            ObjectNode current = (ObjectNode) root;
            for (String key : overrides.keySet()) {
                String[] paths = key.split("\\.");
                JsonNode childNode;
                for (int i = 0; i < paths.length; i++) {
                    boolean isLast = i == paths.length - 1;
                    String pathPart = paths[i];
                    if (current.has(pathPart) && isLast) {
                        current.put(pathPart, overrides.get(key));
                        break;
                    } else {
                        childNode = current.get(pathPart);
                        if (childNode == null || childNode instanceof NullNode) {
                            childNode = current.objectNode();
                            current.set(pathPart, childNode);
                        }
                    }
                    if (isLast) {
                        current.put(pathPart, overrides.get(key));
                    }
                    current = ((ObjectNode) childNode);
                }
            }
        }
        try {
            return mapper.readValue(new TreeTraversingParser(root), clazz);
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }
    }
}
