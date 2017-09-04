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
package openmods.igw.impl.integration;

import openmods.Mods;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.integration.IIntegrationProvider;
import openmods.igw.api.integration.IntegrationFailedException;
import openmods.igw.api.integration.IntegrationLoader;
import openmods.igw.impl.integration.openblocks.OpenBlocksIntegrationProvider;

public enum IntegrationHandler {
    IT;

    private final IntegrationLoader loader;

    IntegrationHandler() {
        this.loader = IntegrationLoader.construct();
    }

    public void register() {
        OpenModsIGWApi.get().log().info("Registering provided mod integrations");

        this.register(Mods.OPENBLOCKS, new OpenBlocksIntegrationProvider());
        // Add other mod support here

        OpenModsIGWApi.get().log().info("Successfully registered integrations");
    }

    @SuppressWarnings("SameParameterValue")
    private void register(final String modId, final IIntegrationProvider provider) {
        OpenModsIGWApi.get().log().info("Registering provider (class %s) for mod %s in loader %s", provider.getClass(), modId, this.loader);
        this.loader.registerIntegration(provider);
    }

    public void load() {
        OpenModsIGWApi.get().log().info("Loading external mod integrations");

        if (!this.loader.load()) {
            OpenModsIGWApi.get().log().info("Successfully loaded all integrations");
        } else {
            OpenModsIGWApi.get().log().warning("Some errors have occurred while registering integrations");

            for (final IntegrationFailedException e : this.loader.getThrownExceptions()) {
                OpenModsIGWApi.get().log().warning(e, e.getMessage());
            }
        }
    }
}
