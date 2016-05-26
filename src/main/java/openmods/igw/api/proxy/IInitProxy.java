package openmods.igw.api.proxy;

import openmods.igw.api.init.IInit;
import openmods.igw.api.init.IPageInit;
import openmods.igw.api.record.mod.IMismatchingModEntry;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Identifies the proxy used in initialization.
 *
 * @since 1.0
 */
public interface IInitProxy extends IInit {

	/**
	 * Gets a list of all mismatching mods between the installed
	 * ones and the available ones at the time of developing.
	 *
	 * @return
	 * 		A list of all mismatching mods.
	 *
	 * @since 1.0
	 */
	@Nonnull
	List<IMismatchingModEntry> getMismatchingMods();

	/**
	 * Gets the current proxy instance as an instance of
	 * {@link IPageInit}, if applicable.
	 *
	 * @return
	 * 		This proxy as an {@link IPageInit}.
	 *
	 * @since 1.0
	 */
	@Nullable
	IPageInit asPageInit();
}
