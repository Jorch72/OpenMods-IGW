package openmods.igw.config;

import openmods.Mods;
import openmods.config.properties.ConfigProperty;

import java.lang.annotation.Annotation;

@SuppressWarnings("unused")
// Access via reflection
public class Config {

	private static final String WIKI_INTEGRATION_CATEGORY = "integration.wiki";
	private static final String WARNINGS_CATEGORY = "warnings";

	@ConfigProperty(name = "missingIgwMod",
			category = WARNINGS_CATEGORY,
			comment = "Enable warning when no IGW Mod is found.")
	public static boolean enableMissingModWarningMessage = true;

	@ConfigProperty(name = Mods.OPENBLOCKS,
			category = WIKI_INTEGRATION_CATEGORY,
			comment = "Enables the Wiki integration for OpenBlocks")
	public static boolean enableOpenBlocksIntegration = true;

	/*
	 * Just a little convenience method.
	 * It uses pure reflection to guarantee compatibility.
	 * Speed is not really important here: 1 or 2 more seconds
	 * during mod loading is not a problem (right?).
	 */
	public static boolean isEnabled(final String modId) {

		for (final java.lang.reflect.Field field : Config.class.getDeclaredFields()) {

			if (field.getAnnotations().length == 0) continue;

			Annotation annotation = field.getAnnotation(ConfigProperty.class);

			if (annotation == null) continue;

			ConfigProperty configProperty = (ConfigProperty) annotation;

			if (!configProperty.category().equals(WIKI_INTEGRATION_CATEGORY)) continue;
			if (!configProperty.name().equals(modId)) continue;

			try {

				return field.getBoolean(null);
			}
			catch (final Throwable throwable) {

				openmods.Log.warn("Field %s is not a boolean. What?", field.getName());
				throw openmods.utils.SneakyThrower.sneakyThrow(throwable);
			}
			/*
			finally {

				return false; // Do we want this?
			}
			*/
		}

		openmods.Log.warn(new Throwable("Thread stack"),
				"Attempted call with un-recognized mod ID %s. Please check your call.",
				modId);
		return false;
	}
}
