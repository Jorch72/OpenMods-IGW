package openmods.igw;

import igwmod.api.WikiRegistry;

import java.util.List;

import net.minecraft.item.ItemStack;
import openmods.Log;
import openmods.Mods;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = OpenModsIGW.MODID, name = OpenModsIGW.NAME, version = OpenModsIGW.VERSION, dependencies = OpenModsIGW.DEPENDENCIES)
public class OpenModsIGW {
	public static final String MODID = "OpenMods-IGW";
	public static final String NAME = "OpenMods-IGW";
	public static final String VERSION = "$VERSION$";
	public static final String DEPENDENCIES = "required-after:OpenMods@[$LIB-VERSION$,$NEXT-LIB-VERSION$);required-after:IGWMod";

	@Instance(MODID)
	public static OpenModsIGW instance;

	public static interface IInitProxy {
		public void preInit(FMLPreInitializationEvent evt);

		public void init(FMLInitializationEvent evt);

		public void postInit(FMLPostInitializationEvent evt);
	}

	public static class ClientInit implements IInitProxy {

		@Override
		public void preInit(FMLPreInitializationEvent evt) {
			PageRegistryHelper registryHelper = new PageRegistryHelper();
			registryHelper.loadItems();

			registryHelper.claimEntities(Mods.OPENBLOCKS);
			final List<Pair<String, ItemStack>> openBlockEntries = registryHelper.claimModObjects(Mods.OPENBLOCKS);

			if (openBlockEntries != null) {
				final OpenBlocksWikiTab tab = new OpenBlocksWikiTab(openBlockEntries);
				WikiRegistry.registerWikiTab(tab);
			} else {
				Log.warn("Failed to find items and blocks for OpenBlocks");
			}
		}

		@Override
		public void init(FMLInitializationEvent evt) {}

		@Override
		public void postInit(FMLPostInitializationEvent evt) {}

	}

	public static class ServerInit implements IInitProxy {

		@Override
		public void preInit(FMLPreInitializationEvent evt) {
			Log.warn("This mod (%s) is currently not needed on server side", NAME);
		}

		@Override
		public void init(FMLInitializationEvent evt) {}

		@Override
		public void postInit(FMLPostInitializationEvent evt) {}

	}

	@SidedProxy(modId = MODID, clientSide = "openmods.igw.OpenModsIGW$ClientInit", serverSide = "openmods.igw.OpenModsIGW$ServerInit")
	public static IInitProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		proxy.preInit(evt);
	}

	@EventHandler
	public void init(FMLInitializationEvent evt) {
		proxy.init(evt);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		proxy.postInit(evt);
	}
}
