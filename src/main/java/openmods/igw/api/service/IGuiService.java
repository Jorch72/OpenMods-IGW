package openmods.igw.api.service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Identifies a service used to manage the various GUIs provided
 * in this mod.
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public interface IGuiService extends IService<IGuiService> {

	/**
	 * Enum used to hold a list of all the various GUIs available in
	 * this mod.
	 *
	 * @author TheSilkMiner
	 * @since 1.0
	 */
	enum GUIs {

		/**
		 * Identifies the GUI used to warn the user that IGW-Mod is
		 * not installed on their system.
		 *
		 * @since 1.0
		 */
		WARNING,
		/**
		 * Identifies the GUI used to warn the user that there are
		 * mismatching mod versions between the ones installed and
		 * the ones this wiki was developed for.
		 *
		 * @since 1.0
		 */
		MISMATCHING_MODS
	}

	/**
	 * Gets if the specified Gui should be shown.
	 *
	 * <p>This is the same as calling the same implementation
	 * method, but it is advised to use this method in
	 * order to rely less on implementation methods.</p>
	 *
	 * @param gui
	 * 		The GUI type.
	 * @return
	 * 		If the specified GUI should be shown.
	 *
	 * @since 1.0
	 */
	boolean shouldShow(@Nonnull final GUIs gui);

	/**
	 * Sets the specified GUI as "needs to be shown".
	 *
	 * <p>In other words, the specified GUI is called and
	 * added to the list of GUIs that have to be shown to
	 * the end user.</p>
	 *
	 * @param gui
	 * 		The GUI type.
	 *
	 * @since 1.0
	 */
	void show(@Nonnull final GUIs gui);

	/**
	 * Constructs reflectively the specified GUI with the
	 * eventually supplied parameters.
	 *
	 * @param gui
	 * 		The GUI's type.
	 * @param parameters
	 * 		The eventual parameters you need to supply to the constructor.
	 * @param <T>
	 *     	The type of the GUI. It is advised to use
	 *     	{@link net.minecraft.client.gui.GuiScreen} to not rely
	 *     	directly on OpenMods-IGW's implementation.
	 * @return
	 * 		The constructed GUI object.
	 *
	 * @since 1.0
	 */
	@Nonnull
	@SuppressWarnings("SpellCheckingInspection")
	<T> T construct(@Nonnull final GUIs gui, @Nullable final Object... parameters);
}
