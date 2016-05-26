package openmods.igw;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import openmods.igw.api.init.IInit;
import openmods.igw.api.proxy.IInitProxy;
import openmods.igw.impl.utils.Constants; // Unable to be removed...

@Mod(modid = Constants.MOD_ID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPS,
		guiFactory = Constants.FACTORY_CLASS)
public final class OpenModsIGW implements IInit {

	@Mod.Instance(Constants.MOD_ID)
	public static OpenModsIGW instance;

	@SidedProxy(modId = Constants.MOD_ID, clientSide = Constants.CLIENT_PROXY, serverSide = Constants.COMMON_PROXY)
	private static IInitProxy proxy;

	@Mod.EventHandler
	@Override
	public void preInit(final FMLPreInitializationEvent evt) {
		proxy().preInit(evt);
	}

	@Mod.EventHandler
	@Override
	public void init(final FMLInitializationEvent evt) {
		proxy().init(evt);
	}

	@Mod.EventHandler
	@Override
	public void postInit(final FMLPostInitializationEvent evt) {
		proxy().postInit(evt);
	}

	public static IInitProxy proxy() {
		return proxy;
	}
}
