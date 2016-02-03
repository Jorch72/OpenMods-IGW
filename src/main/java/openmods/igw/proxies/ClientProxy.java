package openmods.igw.proxies;

import igwmod.api.WikiRegistry;
import igwmod.gui.tabs.IWikiTab;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;
import openmods.Log;
import openmods.Mods;
import openmods.igw.utils.IPageInit;
import openmods.igw.utils.PageRegistryHelper;
import openmods.igw.client.GuiOpenEventHandler;
import openmods.igw.client.WarningGui;
import openmods.igw.openblocks.OpenBlocksWikiTab;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IInitProxy, IPageInit {

	private boolean abort;

	@Override
	public void preInit(final FMLPreInitializationEvent evt) {
		if (!cpw.mods.fml.common.Loader.isModLoaded(Mods.IGW)) this.abort = true;
		MinecraftForge.EVENT_BUS.register(new GuiOpenEventHandler());
	}

	@Override
	public void init(final FMLInitializationEvent evt) {
		if (this.abort) WarningGui.markShow();
	}

	@Override
	public void postInit(final FMLPostInitializationEvent evt) {
		if (this.abort) return;

		this.register(Mods.OPENBLOCKS, OpenBlocksWikiTab.class);
	}

	@Override
	public boolean mustRegister(final String modId) {
		// TODO Config options because mod pack makers are crazy
		return true;
	}

	@Override
	public void register(final String modId, final Class<? extends igwmod.gui.tabs.IWikiTab> tabClass) {
		if (!this.mustRegister(modId)) return;

		final PageRegistryHelper helper = new PageRegistryHelper();
		helper.loadItems();

		final List<Pair<String, String>> entitiesEntries = helper.claimEntities(modId);
		final List<Pair<String, ItemStack>> itemsBlocksEntries = helper.claimModObjects(modId);

		final Map<String, ItemStack> allClaimedPages = helper.getAllClaimedPages();
		final Map<String, Class<? extends net.minecraft.entity.Entity>> allClaimedEntities = helper
				.getAllClaimedEntitiesPages();

		if (entitiesEntries != null
				&& itemsBlocksEntries != null
				&& !entitiesEntries.isEmpty()
				&& !itemsBlocksEntries.isEmpty()) {

			try {
				Constructor<?> constructor = tabClass.getConstructor(List.class, Map.class, Map.class);
				Object tabInstance = constructor.newInstance(itemsBlocksEntries, allClaimedPages, allClaimedEntities);
				IWikiTab tab = IWikiTab.class.cast(tabInstance);
				WikiRegistry.registerWikiTab(tab);
			} catch (final NoSuchMethodException e) {
				Log.warn(e, "Unable to instantiate specified tab class. Invalid constructor!");
			} catch (final ReflectiveOperationException e) {
				Log.warn(e, "Invalid constructor arguments.");
			}
		} else {
			Log.warn("Failed to find items, blocks and entities for " + modId);
		}
	}
}
