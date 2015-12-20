package openmods.igw.proxies;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import openmods.Log;
import openmods.Mods;
import openmods.igw.openblocks.OpenBlocksWikiTab;
import openmods.igw.PageRegistryHelper;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import igwmod.api.WikiRegistry;

import java.util.List;

/**
 * @author boq
 */
public class ClientProxy implements IInitProxy {

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
	public void init(FMLInitializationEvent evt) { }

	@Override
	public void postInit(FMLPostInitializationEvent evt) { }
}
