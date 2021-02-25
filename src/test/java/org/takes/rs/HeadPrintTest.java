/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.rs;

import java.io.IOException;
import org.cactoos.iterable.IterableOf;
import org.cactoos.set.SetOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.hamcrest.object.HasToString;
import org.junit.Test;
import org.llorllale.cactoos.matchers.Assertion;

/**
 * Test case for {@link HeadPrint}.
 * @since 1.19
 */
public final class HeadPrintTest {

    /**
     * HeadPrint can fail on invalid chars.
     * @throws IOException If some problem inside
     */
    @Test(expected = IllegalArgumentException.class)
    public void failsOnInvalidHeader() throws IOException {
        new HeadPrint(new RsWithHeader("name", "\n\n\n")).asString();
    }

    @Test
    public void simple() throws IOException {
        MatcherAssert.assertThat(
            "must write head",
            new HeadPrint(
                new RsSimple(new SetOf<>("HTTP/1.1 500 Internal Server Error"), "")
            ).asString(),
            new HasToString<>(
                new IsEqual<>("HTTP/1.1 500 Internal Server Error\r\n\r\n")
            )
        );
    }

    /**
     * RFC 7230 says we shall support dashes in response first line.
     */
    @Test
    public void simpleWithDash() throws IOException {
        new Assertion<>(
            "must write head with dashes",
            new HeadPrint(
                new RsSimple(new IterableOf<>("HTTP/1.1 203 Non-Authoritative"), "")
            ).asString(),
            new HasToString<>(
                new IsEqual<>("HTTP/1.1 203 Non-Authoritative\r\n\r\n")
            )
        );
    }
}
