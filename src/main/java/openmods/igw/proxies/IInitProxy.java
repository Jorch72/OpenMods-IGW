package openmods.igw.proxies;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * @author boq
 */
public interface IInitProxy {

	void preInit(FMLPreInitializationEvent evt);

	void init(FMLInitializationEvent evt);

	void postInit(FMLPostInitializationEvent evt);
}
