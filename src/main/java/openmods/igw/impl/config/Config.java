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
package openmods.igw.impl.config;

import openmods.Mods;
import openmods.config.properties.ConfigProperty;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.config.IConfig;

import java.lang.annotation.Annotation;

@SuppressWarnings({"checkstyle:visibilitymodifier", "checkstyle:explicitinitialization"})
public final class Config implements IConfig {

    private static final String MODS_WIKI_INTEGRATION_CATEGORY = "integration.mods";
    private static final String TESTING_CATEGORY = "testing";
    private static final String WARNINGS_CATEGORY = "warnings";

    @ConfigProperty(name = "missingIgwMod",
            category = WARNINGS_CATEGORY,
            comment = "Enable warning when no IGW Mod is found.")
    public static boolean enableMissingModWarningMessage = true;

    @ConfigProperty(name = Mods.OPENBLOCKS,
            category = MODS_WIKI_INTEGRATION_CATEGORY,
            comment = "Enables the Wiki integration for OpenBlocks")
    public static boolean enableOpenBlocksIntegration = true;

    @ConfigProperty(name = "joinBetaProgram",
            category = TESTING_CATEGORY,
            comment = "Set to \"true\" to access various untested features. WARNING: They may contain bugs and/or crash the game")
    public static boolean joinBetaProgram = false;

    private Config() {}

    public static boolean isEnabled(final String modId) {
        for (final java.lang.reflect.Field field : Config.class.getDeclaredFields()) {
            if (field.getAnnotations().length == 0) continue;

            final Annotation annotation = field.getAnnotation(ConfigProperty.class);

            if (annotation == null) continue;

            final ConfigProperty configProperty = (ConfigProperty) annotation;

            if (!configProperty.category().equals(MODS_WIKI_INTEGRATION_CATEGORY)) continue;
            if (!configProperty.name().equals(modId)) continue;

            try {
                return field.getBoolean(null);
            } catch (final Throwable throwable) {
                OpenModsIGWApi.get().log().warning("Field %s is not a boolean. Please check your call!", field.getName());
                // Dereference of "SneakyThrower.sneakyThrow(throwable)" may produce java.lang.NullPointerException
                throw com.google.common.base.Preconditions.checkNotNull(openmods.utils.SneakyThrower.sneakyThrow(throwable));
            }
        }

        OpenModsIGWApi.get().log().warning(new Throwable("Thread stack"),
                "Attempted call with un-recognized mod ID %s. Please check your call.",
                modId);
        return false;
    }
}
