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
package openmods.igw.api.service;

import openmods.igw.api.config.IConfig;
import openmods.igw.api.init.IInit;
import openmods.igw.api.proxy.IInitProxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Identifies a service used to provide various implementation-only
 * classes.
 *
 * <p>You should try to rely on this particular service before
 * reflecting in the implementation or (worse) calling methods
 * directly.</p>
 *
 * <p>If you feel like an important class link is missing here,
 * contact us on IRC or make a PR on GitHub.</p>
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public interface IClassProviderService extends IService<IClassProviderService> {

    /**
     * Gets the main mod class.
     *
     * <p>In order to not rely directly on implementation,
     * this method returns an Object. In order to use this
     * class you either need reflection to inspect the various
     * methods or rely directly on implementation.</p>
     *
     * <p>Before calling this method, please try to use
     * {@link #mainClassAsInit()} to obtain an instance
     * of the main class as a {@link IInit} interface
     * implementation.</p>
     *
     * @return
     *         The mod's main class.
     *
     * @since 1.0
     */
    @Nonnull
    Object mainClass();

    /**
     * Gets the main mod class as an instance of {@link IInit}.
     *
     * <p>You should try to use this method before relying on
     * {@link #mainClass()} or reflection.</p>
     *
     * @return
     *         The mod's main class.
     *
     * @since 1.0
     */
    @Nonnull
    IInit mainClassAsInit();

    /**
     * Gets the mod's proxy instance.
     *
     * <p>The instance is automatically populated by Forge
     * on mod initialization. Attempting to get it before
     * then is going to lead to a NullPointerException.</p>
     *
     * @return
     *         The mod's currently active proxy.
     *
     * @since 1.0
     */
    @Nullable
    IInitProxy proxy();

    /**
     * Gets the main configuration class.
     *
     * <p>At the moment, you need to rely on services and/or implementation
     * to do something with config values. Have a look at
     * {@link IConstantRetrieverService} for more information.</p>
     *
     * @return
     *         The mod's config class.
     *
     * @since 1.0
     */
    @Nonnull
    Class<? extends IConfig> config();
}
