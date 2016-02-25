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

package info.novatec.smoketest.core.util;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import info.novatec.smoketest.core.application.configuration.Configuration;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Utility class to work with {@link Configuration} objects.
 *
 * @author Claudio Waldvogel
 * @see Configuration
 */
public final class Configurations {

    /**
     * Private.
     */
    private Configurations() {
    }

    /**
     * Creates a String representation of a {@link Configuration}. Since configurations are inheritable this method
     * uses a generic approach to gather all key/value pairs.<pre><code>
     * MyConfiguration{
     *      prop1 = value
     *      prop2 = value
     *      inheritedProp1 = value
     *      inheritedProp2 = value
     * }
     * </code>
     * </pre>
     *
     * @param configuration
     *         The configuration to be printed
     * @param hidden
     *         List of field names which values must not be printed
     * @return String representation of the configuration.
     */
    public static String toString(final Configuration configuration,
                                  final String... hidden) {
        Class[] hierarchy = configurationHierarchy(configuration);
        StringBuilder stringBuilder = new StringBuilder("\n")
                .append(configuration.getClass().getSimpleName())
                .append("{")
                .append("\n");
        for (int i = hierarchy.length - 1; i >= 0; i--) {
            Class object = hierarchy[i];
            Field[] declaredFields = object.getDeclaredFields();
            for (Field field : declaredFields) {
                //store original accessible value
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                stringBuilder
                        .append("\t")
                        .append(field.getName())
                        .append(" = ");
                try {
                    if (Arrays.asList(hidden).contains(field.getName())) {
                        stringBuilder.append("****");
                    } else {
                        stringBuilder.append(field.get(configuration));
                    }
                } catch (IllegalAccessException e) {
                    // do not fail, but mark field as inaccessible
                    stringBuilder.append("INACCESSIBLE");
                } finally {
                    stringBuilder.append("\n");
                    //ensure field accessibility is restored
                    field.setAccessible(accessible);
                }
            }
        }
        return stringBuilder.append("}").toString();
    }


    /**
     * Utility to method to gather the type hierarchy of a configuration.
     *
     * @param configuration
     *         The IConfiguration for which toe hierarchy should be gathered.
     * @return Class arras representing the hierarchy
     * @throws NullPointerException
     *         if configuration is null
     */
    public static Class[] configurationHierarchy(final Configuration configuration) {
        Preconditions.checkNotNull(configuration, "Configuration must not be null");
        return TypeToken.of(configuration.getClass())
                .getTypes()
                .rawTypes()
                .stream()
                .filter(type ->
                        Configuration.class.isAssignableFrom(type)
                                && !type.isInterface())
                .toArray(Class[]::new);
    }

}
