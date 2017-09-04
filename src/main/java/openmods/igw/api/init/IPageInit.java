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
package openmods.igw.api.init;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Identifies an initializer for the various wiki pages.
 *
 * <p><strong><em>WARNING!</em></strong></p>
 *
 * <p>This class identifies client-only classes. Its usage
 * on a non-client environment is discouraged.</p>
 *
 * @since 1.0
 */
// TODO Use wrappers to allow usage even on servers
// TODO Move to IModEntry instead of String
public interface IPageInit {

    /**
     * Gets the wiki tab registered for the specified mod id.
     *
     * @param modId
     *         The mod id.
     * @return
     *         The specified wiki tab or {@code null} if no one has been registered.
     * @since 1.0
     */
    @Nullable
    igwmod.gui.tabs.IWikiTab getTabForModId(@Nonnull final String modId);

    /**
     * Registers the specified wiki tab for the specified mod id.
     *
     * @param modId
     *         The mod id. It must not be {@code null}.
     * @param tab
     *         The tab to register. It must not be {@code null}.
     *
     * @since 1.0
     */
    void addTabForModId(@Nonnull final String modId, @Nonnull final igwmod.gui.tabs.IWikiTab tab);
}
