package openmods.igw.proxies;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import igwmod.api.WikiRegistry;
import igwmod.gui.tabs.IWikiTab;

import openmods.Log;
import openmods.Mods;
import openmods.config.game.ModStartupHelper;
import openmods.config.properties.ConfigProcessing;
import openmods.igw.client.GuiOpenEventHandler;
import openmods.igw.client.MismatchingVersionsGui;
import openmods.igw.client.WarningGui;
import openmods.igw.common.OpenModsCommonTab;
import openmods.igw.common.OpenModsCommonHandler;
import openmods.igw.config.Config;
import openmods.igw.openblocks.OpenBlocksWikiTab;
import openmods.igw.openblocks.OpenBlocksEventHandler;
import openmods.igw.utils.Constants;
import openmods.igw.utils.IPageInit;
import openmods.igw.utils.ModEntry;
import openmods.igw.utils.PageRegistryHelper;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class ClientProxy implements IInitProxy, IPageInit {

	/**
	 * Keep this to {@code false} unless you want to activate debug mode for the
	 * mismatching versions GUI.
	 */
	private static final boolean MISMATCHING_GUI_DEBUG = false;

	private boolean abort;
	private Map<String, IWikiTab> currentTabs = Maps.newHashMap();
	private List<MismatchingVersionsGui.MismatchingModEntry> mismatchingMods = Lists.newArrayList();

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
		this.register(Mods.OPENBLOCKS, OpenBlocksWikiTab.class, OpenBlocksEventHandler.class);
		this.checkModVersions();
	}

	@Override
	@Nonnull
	public List<MismatchingVersionsGui.MismatchingModEntry> getMismatchingMods() {
		return ImmutableList.copyOf(this.mismatchingMods);
	}

	@Override
	public boolean mustRegister(final String modId) {
		return Config.isEnabled(modId);
	}

	@Override
	public void register(final String modId, //TODO Substitute with ModEntry
						 final Class<? extends igwmod.gui.tabs.IWikiTab> tabClass,
						 final Class<?> eventHandlerClass) {
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
				currentTabs.put(modId, tab);
			} catch (final NoSuchMethodException e) {
				Log.warn(e, "Unable to instantiate specified tab class. Invalid constructor!");
			} catch (final Exception e) {
				Log.warn(e, "Invalid constructor arguments.");
			}
		} else {
			Log.warn("Failed to find items, blocks and entities for " + modId);
		}

		try {
			MinecraftForge.EVENT_BUS.register(eventHandlerClass.getConstructor().newInstance());
		} catch (final NoSuchMethodException e) {
			Log.warn(e, "Unable to instantiate specified event handler class. Invalid constructor!");
		} catch (final Exception e) {
			Log.warn(e, "Invalid constructor arguments.");
		}
	}

	@Override
	public IWikiTab getTabForModId(final String modId) {
		if (Config.useUniqueWikiTab) return this.currentTabs.get("0");

		return this.currentTabs.get(modId);
	}

	private void handleUniqueWikiTab() {
		final List<Pair<String, String>> entitiesEntries = Lists.newArrayList();
		final List<Pair<String, ItemStack>> itemsBlocksEntries = Lists.newArrayList();
		final Map<String, ItemStack> allClaimedPages = Maps.newHashMap();
		final Map<String, Class<? extends net.minecraft.entity.Entity>> allClaimedEntities = Maps.newHashMap();

		for (final ModEntry entry : Constants.CURRENTLY_SUPPORTED_MODS) {
			final String modId = entry.modId();
			if (!this.mustRegister(modId)) continue;

			final PageRegistryHelper helper = new PageRegistryHelper();
			helper.loadItems();

			entitiesEntries.addAll(helper.claimEntities(modId));
			itemsBlocksEntries.addAll(helper.claimModObjects(modId));
			allClaimedEntities.putAll(helper.getAllClaimedEntitiesPages());
			allClaimedPages.putAll(helper.getAllClaimedPages());
		}

		final IWikiTab tab = new OpenModsCommonTab(itemsBlocksEntries, allClaimedPages, allClaimedEntities);
		currentTabs.put("0", tab);
		WikiRegistry.registerWikiTab(tab);

		MinecraftForge.EVENT_BUS.register(new OpenModsCommonHandler());
	}

	private void checkModVersions() {
		if (System.getProperty("openmods.igw.controls.modVersions.debug", "false").equals("true")
				|| MISMATCHING_GUI_DEBUG
				|| ("Windows 8.1".equals(System.getProperty("os.name"))
				    && "E:/GitHub/OpenMods-IGW/run".equals(System.getProperty("user.dir").replace('\\', '/')))
					&& System.getProperty("openmods.igw.controls.modVersions.userDebug", "true").equals("true")) {
			this.debug$checkModVersions();
			return;
		}

		boolean additionsSkipped = false;

		for (final ModEntry entry : Constants.CURRENTLY_SUPPORTED_MODS) {
			final Optional<ModContainer> optionalContainer = entry.modContainer();
			if (!optionalContainer.isPresent()) continue;
			final ModContainer container = optionalContainer.get();
			if (!container.getModId().equals(entry.modId())) continue;
			if (container.getVersion().equals(entry.version())) {
				Log.info("Mod %s found: version matches", entry.modId());
				continue;
			}
			Log.info("Identified mod %s, but gotten different version than expected (%s instead of %s)",
					entry.modId(), container.getVersion(), entry.version());
			if (container.getVersion().equals("$VERSION$")) {
				Log.info("The mod %s installed version (%s) equals the development environment string",
						container.getModId(), container.getVersion());
				Log.info("Skipping addition...");
				if (!additionsSkipped) additionsSkipped = true;
				continue;
			}
			Log.info("Adding to mismatching mod list.");
			final MismatchingVersionsGui.MismatchingModEntry mismatchingEntry = new MismatchingVersionsGui.
					MismatchingModEntry(entry, container.getVersion());
			this.mismatchingMods.add(mismatchingEntry);
			if (!MismatchingVersionsGui.shouldShow()) MismatchingVersionsGui.show();
		}

		if (this.mismatchingMods.isEmpty() && !additionsSkipped) Log.info("No mismatching mod versions found");
	}

	private void debug$checkModVersions() {
		MismatchingVersionsGui.show();
		this.mismatchingMods.add(new MismatchingVersionsGui.MismatchingModEntry(ModEntry.of("test1", "1.0"), "1.1"));
		this.mismatchingMods.add(new MismatchingVersionsGui.MismatchingModEntry(ModEntry.of("test2", "0.0"), "0.1"));
		this.mismatchingMods.add(new MismatchingVersionsGui.MismatchingModEntry(ModEntry.of("test3", "v1.0"), "v1.1"));
		this.mismatchingMods.add(new MismatchingVersionsGui.MismatchingModEntry(ModEntry.of("test4", "v1.0-stable"), "v1.0-beta"));
		this.mismatchingMods.add(new MismatchingVersionsGui.MismatchingModEntry(ModEntry.of("test5", ""), "0.7"));
		this.mismatchingMods.add(new MismatchingVersionsGui.MismatchingModEntry(ModEntry.of("test6", "v0.0-stable"), "v0.0-beta"));
		this.mismatchingMods.add(new MismatchingVersionsGui.MismatchingModEntry(ModEntry.of("test7", "v1.0-stable"), "v0.0-beta"));
	}
}
