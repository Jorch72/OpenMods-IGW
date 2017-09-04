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
package openmods.igw.api.record.mod;

import javax.annotation.Nonnull;

/**
 * Represents an object containing all the necessary information
 * used to identify a specific mod in the current OpenMods-IGW
 * environment.
 *
 * <p>The caching of all instances of the implementations of this
 * interface must be avoided, because the entry may change during
 * its lifetime.</p>
 *
 * @since 1.0
 */
public interface IMutableModEntry extends IModEntry {

    /**
     * Sets the mod id of the current entry.
     *
     * @param modId
     *         The new mod id.
     *
     * @since 1.0
     */
    void modId(@Nonnull final String modId);

    /**
     * Sets the version of the current entry.
     *
     * @param version
     *         The new version.
     *
     * @since 1.0
     */
    void version(@Nonnull final String version);
}
