package openmods.igw.impl.service;

import net.minecraft.util.StatCollector;

import openmods.Log;
import openmods.igw.api.service.IService;
import openmods.igw.api.service.ITranslationService;
import openmods.igw.impl.utils.Constants;

import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility class representing the default translation service provided
 * by this mod.
 */
public final class TranslationService implements ITranslationService {

	private static final TranslationService IT = new TranslationService();

	private String defaultModId;

	private TranslationService() {}

	public static TranslationService get() {
		return IT;
	}

	@Nonnull
	@Override
	public String translate(@Nonnull final String translationId) {
		return this.translate(this.defaultModId, translationId);
	}

	@Nonnull
	@Override
	public String translate(@Nonnull final String modId, @Nonnull final String translationId) {
		return this.translate(modId, translationId, IGNORE_ERROR);
	}

	@Nonnull
	@Override
	public String translate(@Nonnull final String modId, @Nonnull final String translationId, final boolean report) {
		final String translation = StatCollector.translateToLocal(
				String.format("%s.%s", modId, translationId.toLowerCase(Locale.ENGLISH).trim())
		);
		if (report && translation.equals(translationId)) {
			try {
				return this.translate("service.translation.missing");
			} catch (final StackOverflowError error) {
				// In case of broken implementation, return untranslated string
				Log.severe(error, "Unable to translate internal error message. Broken implementation?");
				return "An error has occurred while trying to translate the specified string.";
			}
		}
		return translation;
	}

	@Nonnull
	@Override
	public ITranslationService cast() {
		return this;
	}

	@Override
	public void onRegisterPre(@Nullable final IService<ITranslationService> previous) {}

	@Override
	public void onRegisterPost() {
		this.defaultModId = Constants.MOD_ID.toLowerCase().replace('-', '.');
	}

	@Override
	public void onUnregister() {
		this.defaultModId = null; // Free up memory
	}
}
