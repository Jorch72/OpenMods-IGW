package openmods.igw.impl.config;

import openmods.Mods;
import openmods.config.properties.ConfigProperty;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.config.IConfig;

import java.lang.annotation.Annotation;

public class Config implements IConfig {

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

    public static boolean isEnabled(final String modId) {
        for (final java.lang.reflect.Field field : Config.class.getDeclaredFields()) {
            if (field.getAnnotations().length == 0) continue;

            Annotation annotation = field.getAnnotation(ConfigProperty.class);

            if (annotation == null) continue;

            ConfigProperty configProperty = (ConfigProperty) annotation;

            if (!configProperty.category().equals(MODS_WIKI_INTEGRATION_CATEGORY)) continue;
            if (!configProperty.name().equals(modId)) continue;

            try {
                return field.getBoolean(null);
            }
            catch (final Throwable throwable) {
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
