package openmods.igw;

import openmods.igw.proxies.IInitProxy;
import openmods.igw.utils.Constants;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * @author boq
 */
@Mod(modid = Constants.MOD_ID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPS)
public class OpenModsIGW {

	@Mod.Instance(Constants.MOD_ID)
	public static OpenModsIGW instance;

	@SidedProxy(modId = Constants.MOD_ID, clientSide = Constants.CLIENT_PROXY, serverSide = Constants.COMMON_PROXY)
	public static IInitProxy proxy;

	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent evt) {
		proxy.preInit(evt);
	}

	@Mod.EventHandler
	public void init(final FMLInitializationEvent evt) {
		proxy.init(evt);
	}

	@Mod.EventHandler
	public void postInit(final FMLPostInitializationEvent evt) {
		proxy.postInit(evt);
	}
}
