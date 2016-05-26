package openmods.igw.api.record.mod;

import javax.annotation.Nonnull;

/**
 * Represents a mismatching mod entry, used to hold all the
 * information the GUI needs to show.
 *
 * @since 1.0
 */
public interface IMismatchingModEntry extends IModEntry {

	/**
	 * Gets the mod entry associated with this mismatching object.
	 *
	 * @return
	 * 		The mod entry.
	 *
	 * @since 1.0
	 */
	@Nonnull
	IModEntry mod();

	/**
	 * Gets the current expected version.
	 *
	 * @return
	 * 		The expected version.
	 */
	@Nonnull
	String installedVersion();

}
