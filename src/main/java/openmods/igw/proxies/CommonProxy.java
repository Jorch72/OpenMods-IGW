package openmods.igw.proxies;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import openmods.Log;
import openmods.igw.utils.Constants;

@SuppressWarnings("unused")
public class CommonProxy implements IInitProxy {

	@Override
	public void preInit(final FMLPreInitializationEvent evt) {}

	@Override
	public void init(final FMLInitializationEvent evt) {}

	@Override
	public void postInit(final FMLPostInitializationEvent evt) {
		Log.warn("This mod (%s) is currently not needed on server side.", Constants.NAME);
		Log.info("You can install it to force people to use it, though.");
	}
}
