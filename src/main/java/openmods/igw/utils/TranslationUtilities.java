package openmods.igw.utils;

import net.minecraft.util.StatCollector;

public final class TranslationUtilities {

	private TranslationUtilities() {}

	public static String translate(final String id) {

		// It would be better if this Mod ID would be stored in openmods.Mods
		// but this works for now
		return TranslationUtilities.translate(Constants.MOD_ID.toLowerCase().replace('-', '.'), id);
	}

	@SuppressWarnings("WeakerAccess")
	//@Explain("API method")
	public static String translate(final String mod, final String id) {

		return StatCollector.translateToLocal(String.format("%s.%s", mod, id.toLowerCase().trim()));
	}
}
