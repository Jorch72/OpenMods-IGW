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
	 * 		The mod id.
	 * @return
	 * 		The specified wiki tab or {@code null} if no one has been registered.
	 * @since 1.0
	 */
	@Nullable
	igwmod.gui.tabs.IWikiTab getTabForModId(@Nonnull final String modId);

	/**
	 * Registers the specified wiki tab for the specified mod id.
	 *
	 * @param modId
	 * 		The mod id. It must not be {@code null}.
	 * @param tab
	 * 		The tab to register. It must not be {@code null}.
	 *
	 * @since 1.0
	 */
	void addTabForModId(@Nonnull final String modId, @Nonnull final igwmod.gui.tabs.IWikiTab tab);
}
