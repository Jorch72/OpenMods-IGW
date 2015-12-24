package openmods.igw.proxies;

import openmods.Log;
import openmods.igw.utils.Constants;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * @author boq
 */
public class CommonProxy implements IInitProxy {

	@Override
	public void preInit(FMLPreInitializationEvent evt) { }

	@Override
	public void init(FMLInitializationEvent evt) { }

	@Override
	public void postInit(FMLPostInitializationEvent evt) {
		Log.warn("This mod (%s) is currently not needed on server side.", Constants.NAME);
		Log.warn("You can install it to force people to use it, though.");
	}
}
