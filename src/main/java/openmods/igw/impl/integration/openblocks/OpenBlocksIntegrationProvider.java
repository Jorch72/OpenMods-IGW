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
package openmods.igw.impl.integration.openblocks;

import openmods.Mods;
import openmods.igw.api.integration.IIntegrationExecutor;
import openmods.igw.prefab.integration.AbstractIntegrationProvider;
import openmods.igw.prefab.record.mod.ModEntry;

import javax.annotation.Nonnull;

/**
 * Provides integration for OpenBlocks.
 *
 * @since 1.0
 */
public final class OpenBlocksIntegrationProvider extends AbstractIntegrationProvider {

    private static final String SUPPORTED_OPEN_BLOCKS_VERSION = "1.6";

    public OpenBlocksIntegrationProvider() {
        super(ModEntry.of(Mods.OPENBLOCKS, SUPPORTED_OPEN_BLOCKS_VERSION));
    }

    @Nonnull
    @Override
    public IIntegrationExecutor getExecutor() {
        return new OpenBlocksIntegrationExecutor(this.getModEntry());
    }
}
