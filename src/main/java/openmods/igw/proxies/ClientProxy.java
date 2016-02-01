package openmods.igw.proxies;

import igwmod.api.WikiRegistry;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;
import openmods.Log;
import openmods.Mods;
import openmods.igw.PageRegistryHelper;
import openmods.igw.client.GuiOpenEventHandler;
import openmods.igw.client.WarningGui;
import openmods.igw.openblocks.OpenBlocksWikiTab;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IInitProxy {

	private boolean shallLoad;

	@Override
	public void preInit(final FMLPreInitializationEvent evt) {
		if (cpw.mods.fml.common.Loader.isModLoaded(Mods.IGW)) this.shallLoad = true;
		MinecraftForge.EVENT_BUS.register(new GuiOpenEventHandler());
	}

	@Override
	public void init(final FMLInitializationEvent evt) {}

	@Override
	public void postInit(final FMLPostInitializationEvent evt) {
		if(!this.shallLoad) {
			WarningGui.markShow();
			return;
		}

		PageRegistryHelper registryHelper = new PageRegistryHelper();
		registryHelper.loadItems();

		registryHelper.claimEntities(Mods.OPENBLOCKS);
		final List<Pair<String, ItemStack>> openBlockEntries = registryHelper.claimModObjects(Mods.OPENBLOCKS);

		final Map<String, ItemStack> allClaimedPages = registryHelper.getAllClaimedPages();

		if (openBlockEntries != null) {
			WikiRegistry.registerWikiTab(new OpenBlocksWikiTab(openBlockEntries, allClaimedPages));
		} else {
			Log.warn("Failed to find items and blocks for OpenBlocks");
		}
	}
}
