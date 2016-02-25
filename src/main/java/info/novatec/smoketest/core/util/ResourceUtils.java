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

import com.google.common.io.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Claudio Waldvogel (claudio.waldvogel@novatec-gmbh.de)
 */
public final class ResourceUtils {

    /**
     * Must not be instantiated.
     */
    private ResourceUtils() {
    }

    /**
     * Opens an InputStream of a file.
     *
     * @param filePath
     *         The file path
     * @return A {@link InputStream}
     * @throws FileNotFoundException
     *         IF the file ist not available
     */
    public static InputStream openFileStream(String filePath) throws FileNotFoundException {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(filePath));
        } catch (Exception e) {
            try {
                inputStream = new FileInputStream(new File(Resources.getResource(filePath).toURI()));
            } catch (Exception ex) {
                inputStream = Thread.currentThread()
                        .getContextClassLoader().getResourceAsStream(filePath);
            }
        }
        if (inputStream == null) {
            throw new FileNotFoundException("Invalid file path: " + filePath);
        }
        return inputStream;
    }
}
