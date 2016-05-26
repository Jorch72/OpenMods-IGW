package openmods.igw.impl.config;

import openmods.Mods;
import openmods.config.properties.ConfigProperty;
import openmods.igw.api.config.IConfig;

import java.lang.annotation.Annotation;

@SuppressWarnings("unused")
// Access via reflection
public class Config implements IConfig {

	private static final String MODS_WIKI_INTEGRATION_CATEGORY = "integration.wiki.mods";
	private static final String TAB_WIKI_INTEGRATION_CATEGORY = "integration.wiki.tabs";
	private static final String MOD_INTEGRATION_CATEGORY = "integration.mods";
	private static final String WARNINGS_CATEGORY = "warnings";

	@ConfigProperty(name = "missingIgwMod",
			category = WARNINGS_CATEGORY,
			comment = "Enable warning when no IGW Mod is found.")
	public static boolean enableMissingModWarningMessage = true;

	@ConfigProperty(name = Mods.OPENBLOCKS,
			category = MODS_WIKI_INTEGRATION_CATEGORY,
			comment = "Enables the Wiki integration for OpenBlocks")
	public static boolean enableOpenBlocksIntegration = true;

	@ConfigProperty(name = "uniqueWikiTab",
			category = TAB_WIKI_INTEGRATION_CATEGORY,
			comment = "Set to \"true\" to display only one wiki tab for all the mods")
	public static boolean useUniqueWikiTab = false;

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

				openmods.Log.warn("Field %s is not a boolean. Please check your call!", field.getName());
				throw openmods.utils.SneakyThrower.sneakyThrow(throwable);
			}
		}

		openmods.Log.warn(new Throwable("Thread stack"),
				"Attempted call with un-recognized mod ID %s. Please check your call.",
				modId);
		return false;
	}
}
