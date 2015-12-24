package openmods.igw.proxies;

import igwmod.api.WikiRegistry;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import openmods.Log;
import openmods.Mods;
import openmods.igw.PageRegistryHelper;
import openmods.igw.openblocks.OpenBlocksWikiTab;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IInitProxy {

	@Override
	public void preInit(FMLPreInitializationEvent evt) {}

	@Override
	public void init(FMLInitializationEvent evt) {}

	@Override
	public void postInit(FMLPostInitializationEvent evt) {
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
