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
package openmods.igw.api.proxy;

import openmods.igw.api.init.IInit;
import openmods.igw.api.init.IPageInit;
import openmods.igw.api.record.mod.IMismatchingModEntry;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Identifies the proxy used in initialization.
 *
 * @since 1.0
 */
public interface IInitProxy extends IInit {

    /**
     * Gets a list of all mismatching mods between the installed
     * ones and the available ones at the time of developing.
     *
     * @return
     *         A list of all mismatching mods.
     *
     * @since 1.0
     */
    @Nonnull
    List<IMismatchingModEntry> getMismatchingMods();

    /**
     * Adds the given {@code entry} to the list of mismatching
     * mods. See {@link IMismatchingModEntry} and
     * {@link #getMismatchingMods()} for more information.
     *
     * @param entry
     *         The entry to add. It must not be {@code null}.
     *
     * @since 1.0
     */
    void addMismatchingMod(@Nonnull final IMismatchingModEntry entry);

    /**
     * Gets the current proxy instance as an instance of
     * {@link IPageInit}, if applicable.
     *
     * @return
     *         This proxy as an {@link IPageInit}.
     *
     * @since 1.0
     */
    @Nullable
    IPageInit asPageInit();
}
