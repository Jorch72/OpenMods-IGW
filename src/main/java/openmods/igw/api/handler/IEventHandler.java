/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Open Mods
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
package openmods.igw.api.handler;

import javax.annotation.Nonnull;

/**
 * Represents the basic event handler used by OpenMods-IGW.
 *
 * <p>Every event handler (proxies and main mod class excluded)
 * should implement this interface.</p>
 *
 * @since 1.0
 */
public interface IEventHandler {

    /**
     * Gets the mod id this event handler is for.
     *
     * @return
     *         The mod id.
     *
     * @since 1.0
     */
    @Nonnull
    String modId();

    /**
     * Gets the mod id this event handler is for.
     *
     * @return
     *         The mod id.
     *
     * @deprecated Use {@link #modId()} instead.
     *
     * @since 1.0
     */
    @Deprecated
    @Nonnull
    String getModId();

    /**
     * Gets the name of the class that holds all the various
     * configuration options for the mod specified by
     * {@link #modId()}.
     *
     * @return
     *         The name of the configuration class.
     *
     * @since 1.0
     */
    @Nonnull
    String configClass();

    /**
     * Gets the name of the class that holds all the various
     * configuration options for the mod specified by
     * {@link #modId()}.
     *
     * @return
     *         The name of the configuration class.
     *
     * @deprecated Use {@link #configClass()} instead.
     *
     * @since 1.0
     */
    @Deprecated
    @Nonnull
    String getConfigClass();
}
