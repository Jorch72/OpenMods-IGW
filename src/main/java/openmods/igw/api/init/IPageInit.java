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
	 * Gets if the specified mod should register its wiki pages.
	 *
	 * <p>Usually you would want to check the various config
	 * options when this method is called.</p>
	 *
	 * @param modId
	 * 		The mod id.
	 * @return
	 * 		If the mod can register its wiki pages.
	 *
	 * @since 1.0
	 */
	boolean mustRegister(@Nonnull final String modId);

	/**
	 * Registers the specified wiki tab for the specified mod id.
	 *
	 * <p>You must also supply an event handler which will take
	 * care of every possible scenario which will normally cause
	 * a failure inside IGW-Mod code (such as unobtainable blocks).</p>
	 *
	 * @param modId
	 * 		The mod id.
	 * @param tabClass
	 * 		The tab class.
	 * @param eventHandlerClass
	 * 		The event handler class.
	 *
	 * @since 1.0
	 */
	void register(@Nonnull final String modId,
				  @Nonnull final Class<? extends igwmod.gui.tabs.IWikiTab> tabClass,
				  @Nonnull final Class<?> eventHandlerClass);

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
