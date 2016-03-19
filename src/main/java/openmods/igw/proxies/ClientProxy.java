package openmods.igw.proxies;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import igwmod.api.WikiRegistry;
import igwmod.gui.tabs.IWikiTab;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import openmods.Log;
import openmods.Mods;
import openmods.config.game.ModStartupHelper;
import openmods.config.properties.ConfigProcessing;
import openmods.igw.common.OpenModsCommonTab;
import openmods.igw.config.Config;
import openmods.igw.utils.Constants;
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

		new ModStartupHelper(Constants.MOD_ID) {
			@Override
			protected void populateConfig(final Configuration config) {
				ConfigProcessing.processAnnotations(Constants.MOD_ID, config, Config.class);
			}
		}.preInit(evt.getSuggestedConfigurationFile());
	}

	@Override
	public void init(final FMLInitializationEvent evt) {
		if (this.abort) WarningGui.markShow();
	}

	@Override
	public void postInit(final FMLPostInitializationEvent evt) {
		if (this.abort) return;
		if (Config.useUniqueWikiTab) {
			this.handleUniqueWikiTab();
			return;
		}
		this.register(Mods.OPENBLOCKS, OpenBlocksWikiTab.class);
	}

	@Override
	public boolean mustRegister(final String modId) {
		return Config.isEnabled(modId);
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
			} catch (final Exception e) {
				// So ReflectiveOperationException is only from Java 7 onwards...
				Log.warn(e, "Invalid constructor arguments."); // I guess
			}
		} else {
			Log.warn("Failed to find items, blocks and entities for " + modId);
		}
	}

	private void handleUniqueWikiTab() {
		final List<Pair<String, String>> entitiesEntries = Lists.newArrayList();
		final List<Pair<String, ItemStack>> itemsBlocksEntries = Lists.newArrayList();
		final Map<String, ItemStack> allClaimedPages = Maps.newHashMap();
		final Map<String, Class<? extends net.minecraft.entity.Entity>> allClaimedEntities = Maps.newHashMap();

		for (final String modId : Constants.CURRENTLY_SUPPORTED_MODS) {
			if (!this.mustRegister(modId)) continue;

			final PageRegistryHelper helper = new PageRegistryHelper();
			helper.loadItems();
			// Maybe this could be moved to outside foreach construct.

			entitiesEntries.addAll(helper.claimEntities(modId));
			itemsBlocksEntries.addAll(helper.claimModObjects(modId));
			allClaimedEntities.putAll(helper.getAllClaimedEntitiesPages());
			allClaimedPages.putAll(helper.getAllClaimedPages());
		}

		WikiRegistry.registerWikiTab(new OpenModsCommonTab(itemsBlocksEntries, allClaimedPages, allClaimedEntities));
	}
}
