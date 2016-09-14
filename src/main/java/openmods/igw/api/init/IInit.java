package openmods.igw.api.init;

import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Identifies a class that initializes the mod.
 *
 * <p>Initialization identifies a class which allows the mod
 * to load through all the three normal stages of mod
 * initialization, namely pre-initialization, initialization
 * and post-initialization.</p>
 *
 * <p>For server related life cycles or other initialization
 * phases, refer to other classes in this package.</p>
 *
 * @since 1.0
 */
public interface IInit {

	/**
	 * Constructs the mod instance, before any other event is called.
	 *
	 * @param event
	 * 		The construction event provided by FML.
	 *
	 * @since 1.0
	 */
	void construct(final FMLConstructionEvent event);

	/**
	 * Loads the mod through the phase of pre-initialization.
	 *
	 * @param event
	 * 		The pre-initialization event provided by FML.
	 *
	 * @since 1.0
	 */
	void preInit(final FMLPreInitializationEvent event);

	/**
	 * Loads the mod through the phase of initialization.
	 *
	 * @param event
	 * 		The initialization event provided by FML.
	 *
	 * @since 1.0
	 */
	void init(final FMLInitializationEvent event);

	/**
	 * Loads the mod through the phase of post-initialization.
	 *
	 * @param event
	 * 		The post-initialization event provided by FML.
	 *
	 * @since 1.0
	 */
	void postInit(final FMLPostInitializationEvent event);
}
