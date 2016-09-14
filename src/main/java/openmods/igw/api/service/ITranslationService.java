package openmods.igw.api.service;

import javax.annotation.Nonnull;

/**
 * Identifies a service used for translations.
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public interface ITranslationService extends IService<ITranslationService> {

	/**
	 * Indicates to report the error to the user, with
	 * a default message.
	 *
	 * @since 1.0
	 */
	boolean REPORT_ERROR = true;

	/**
	 * Indicates to ignore translation errors, returning
	 * the untranslated string.
	 *
	 * @since 1.0
	 */
	boolean IGNORE_ERROR = false;

	/**
	 * Attempts to translate the given translation ID.
	 *
	 * <p>This method should add to the translation ID a default
	 * mod ID, either visible to the end-user or only available
	 * internally.</p>
	 *
	 * @param translationId
	 * 		The ID of the translation.
	 * @return
	 * 		A String containing the translated keyword or the given
	 * 		{@code translationId} if none is available.
	 *
	 * @since 1.0
	 */
	// TODO Default implementation (Java 8 needed)
	@Nonnull
	String translate(@Nonnull final String translationId);

	/**
	 * Attempts to translate the given translation ID, prefixing it
	 * with the given mod ID.
	 *
	 * @param modId
	 * 		The mod ID to use.
	 * @param translationId
	 * 		The ID of the translation.
	 * @return
	 * 		A String containing the translated keyword or the given
	 * 		{@code translationId} if none is available.
	 *
	 * @since 1.0
	 */
	// TODO Default implementation (Java 8 needed)
	@Nonnull
	String translate(@Nonnull final String modId, @Nonnull final String translationId);

	/**
	 * Attempts to translate the given translation ID, prefixing it
	 * with the given mod ID and returning an error message if the
	 * translation fails.
	 *
	 * @param modId
	 * 		The mod ID to use.
	 * @param translationId
	 * 		The ID of the translation.
	 * @param report
	 * 		Whether to return an error message or not.
	 * 		It should either be {@link #IGNORE_ERROR} or
	 * 		{@link #REPORT_ERROR}.
	 * @return
	 * 		A String containing the translated keyword or the given
	 * 		{@code translationId} if none is available and the
	 * 		{@code report} flag is {@link #IGNORE_ERROR} or an error
	 * 		message if it is {@link #REPORT_ERROR}.
	 *
	 * @since 1.0
	 */
	@Nonnull
	String translate(@Nonnull final String modId, @Nonnull final String translationId, final boolean report);
}
