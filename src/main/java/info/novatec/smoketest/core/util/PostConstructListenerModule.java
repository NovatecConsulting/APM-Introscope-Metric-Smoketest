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

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility Guice Module to enable JSR250 @PostConstruct annotation handling. This module installs a InjectionsListener.
 * This InjectionListener is always invoked after the all dependencies are already injected to a type. At this point the
 * module scans the type for methods annotated with @PostConstruct
 *
 * @author Claudio Waldvogel
 * @see com.google.inject.Module
 * @see InjectionListener
 * @see PostConstruct
 */
public class PostConstructListenerModule extends AbstractModule implements TypeListener {

    /**
     * Scans a class for methods annotated with @PostConstruct. A valid @PostConstruct methods is public, has zero
     * parameters, and has void as return type.
     *
     * @param type
     *         The type to be examined
     * @return List of all methods annotated with @PostConstruct
     */
    private static List<Method> gatherPostConstructMethods(final Class<?> type) {
        return Arrays.asList(type.getMethods())
                .stream()
                .filter(method ->
                        method.isAnnotationPresent(PostConstruct.class)
                                && Modifier.isPublic(method.getModifiers())
                                && method.getParameters().length == 0
                                && void.class.equals(method.getReturnType())
                ).collect(Collectors.toList());
    }

    @Override
    protected void configure() {
        bindListener(Matchers.any(), this);
    }

    @Override
    public <I> void hear(final TypeLiteral<I> type,
                         final TypeEncounter<I> encounter) {
        encounter.register((InjectionListener<I>) injectee -> {
            for (Method method : gatherPostConstructMethods(injectee.getClass())) {
                try {
                    method.invoke(injectee);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Failed to invoke @PostConstruct method", e);
                }
            }
        });
    }
}
