package openmods.igw.impl.proxy;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import igwmod.api.WikiRegistry;
import igwmod.gui.tabs.IWikiTab;

import openmods.Mods;
import openmods.config.game.ModStartupHelper;
import openmods.config.properties.ConfigProcessing;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.init.IPageInit;
import openmods.igw.api.proxy.IInitProxy;
import openmods.igw.api.record.mod.IMismatchingModEntry;
import openmods.igw.api.record.mod.IModEntry;
import openmods.igw.api.service.IClassProviderService;
import openmods.igw.api.service.IConstantRetrieverService;
import openmods.igw.api.service.IGuiService;
import openmods.igw.api.service.IService;
import openmods.igw.api.service.ISystemIdentifierService;
import openmods.igw.impl.client.GuiOpenEventHandler;
import openmods.igw.impl.common.OpenModsCommonTab;
import openmods.igw.impl.common.OpenModsCommonHandler;
import openmods.igw.impl.openblocks.OpenBlocksWikiTab;
import openmods.igw.impl.openblocks.OpenBlocksEventHandler;
import openmods.igw.prefab.init.PageRegistryHelper;
import openmods.igw.prefab.record.mod.MismatchingModEntry;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO Move some things to API (Remove direct implementation imports)
public class ClientProxy implements IInitProxy, IPageInit {

	/**
	 * Keep this to {@code false} unless you want to activate debug mode for the
	 * mismatching versions GUI.
	 */
	private static final boolean MISMATCHING_GUI_DEBUG = false;

	private static final String DEVELOPMENT_ENVIRONMENT_VERSION = "$VERSION$";

	private boolean abort;
	private final Map<String, IWikiTab> currentTabs = Maps.newHashMap();
	private final List<IMismatchingModEntry> mismatchingMods = Lists.newArrayList();

	@Override
	public void construct(final FMLConstructionEvent event) {
		// Hot load services
		this.constantService();
	}

	@Override
	public void preInit(final FMLPreInitializationEvent evt) {
		if (!cpw.mods.fml.common.Loader.isModLoaded(Mods.IGW)) this.abort = true;
		MinecraftForge.EVENT_BUS.register(new GuiOpenEventHandler());

		new ModStartupHelper(this.constantService().<String>getConstant("MOD_ID").orNull()) {
			@Override
			@SuppressWarnings("ConstantConditions")
			protected void populateConfig(final Configuration cfg) {
				ConfigProcessing.processAnnotations(
						ClientProxy.this.constantService().<String>getConstant("MOD_ID").orNull(),
						cfg,
						OpenModsIGWApi.get().obtainService(IClassProviderService.class).get().cast().config()
				);
			}
		}.preInit(evt.getSuggestedConfigurationFile());

		if (this.constantService().getBooleanConfigConstant("joinBetaProgram").get()) {
			final ISystemIdentifierService service = Preconditions.checkNotNull(
					OpenModsIGWApi.get().serviceManager().obtainAndCastService(ISystemIdentifierService.class)
			);

			// If the current system is a developer one, skip the addition: that would be pretty dumb otherwise
			if (service.getSystemType() == ISystemIdentifierService.SystemType.DEVELOPER) return;

			service.switchCurrentType(ISystemIdentifierService.SystemType.BETA_TESTER);
			OpenModsIGWApi.get().log().info("Successfully joined beta program for OpenMods-IGW");
		}
	}

	@Override
	public void init(final FMLInitializationEvent evt) {
		if (this.abort) this.guiService().show(IGuiService.GUIs.WARNING);
	}

	@Override
	public void postInit(final FMLPostInitializationEvent evt) {
		if (this.abort) return;

		this.registerOwnWikiTab();

		if (this.constantService().getBooleanConfigConstant("useUniqueWikiTab").get()) {
			this.handleUniqueWikiTab();
			return;
		}
		this.register(Mods.OPENBLOCKS, OpenBlocksWikiTab.class, OpenBlocksEventHandler.class);
		this.checkModVersions();
	}

	@Nonnull
	@Override
	public List<IMismatchingModEntry> getMismatchingMods() {
		return ImmutableList.copyOf(this.mismatchingMods);
	}

	@Nullable
	@Override
	public IPageInit asPageInit() {
		return this;
	}

	@Override
	public void addMismatchingMod(@Nonnull final IMismatchingModEntry entry) {
		this.mismatchingMods.add(entry);
	}

	@Override
	public boolean mustRegister(@Nonnull final String modId) {
		return this.constantService().isEnabled(modId);
	}

	@Override
	public void register(@Nonnull final String modId,
						 @Nonnull final Class<? extends igwmod.gui.tabs.IWikiTab> tabClass,
						 @Nonnull final Class<?> eventHandlerClass) {
		if (!this.mustRegister(modId)) return;

		final PageRegistryHelper helper = new PageRegistryHelper();
		helper.loadItems();

		final List<Pair<String, String>> entitiesEntries = helper.claimEntities(modId);
		final List<Pair<String, ItemStack>> itemsBlocksEntries = helper.claimModObjects(modId);

		final Map<String, ItemStack> allClaimedPages = helper.getAllClaimedPages();
		final Map<String, Class<? extends net.minecraft.entity.Entity>> allClaimedEntities = helper
				.getAllClaimedEntitiesPages();

		if (!entitiesEntries.isEmpty() && !itemsBlocksEntries.isEmpty()) {

			try {
				Constructor<?> constructor = tabClass.getConstructor(List.class, Map.class, Map.class);
				Object tabInstance = constructor.newInstance(itemsBlocksEntries, allClaimedPages, allClaimedEntities);
				IWikiTab tab = IWikiTab.class.cast(tabInstance);
				WikiRegistry.registerWikiTab(tab);
				currentTabs.put(modId, tab);
			} catch (final NoSuchMethodException e) {
				OpenModsIGWApi.get().log().warning(e, "Unable to instantiate specified tab class. Invalid constructor!");
			} catch (final Exception e) {
				OpenModsIGWApi.get().log().warning(e, "Invalid constructor arguments.");
			}
		} else {
			OpenModsIGWApi.get().log().warning("Failed to find items, blocks and entities for %s", modId);
		}

		try {
			MinecraftForge.EVENT_BUS.register(eventHandlerClass.getConstructor().newInstance());
		} catch (final NoSuchMethodException e) {
			OpenModsIGWApi.get().log().warning(e, "Unable to instantiate specified event handler class. Invalid constructor!");
		} catch (final Exception e) {
			OpenModsIGWApi.get().log().warning(e, "Invalid constructor arguments.");
		}

		OpenModsIGWApi.get().log().info("Successfully loaded integration for mod " + modId);
	}

	@Nullable
	@Override
	public IWikiTab getTabForModId(@Nonnull final String modId) {
		final IWikiTab tab = this.currentTabs.get(modId);

		if (tab == null && this.constantService().getBooleanConfigConstant("useUniqueWikiTab").orElseThrow()) {
			return this.currentTabs.get("0");
		}

		return tab;
	}

	@Override
	public void addTabForModId(@Nonnull final String modId, @Nonnull final IWikiTab tab) {
		Preconditions.checkNotNull(modId, "Mod ID must not be null");
		Preconditions.checkNotNull(tab, "Tab must not be null");
		this.currentTabs.put(modId, tab);
	}

	private void registerOwnWikiTab() {
		final IWikiTab tab = new openmods.igw.impl.client.wiki.OpenModsIgwWikiTab();
		this.currentTabs.put(this.constantService().<String>getConstant("MOD_ID").orElseThrow(), tab);
		WikiRegistry.registerWikiTab(tab);
	}

	private void handleUniqueWikiTab() {
		//final List<Pair<String, String>> entitiesEntries = Lists.newArrayList();
		final List<Pair<String, ItemStack>> itemsBlocksEntries = Lists.newArrayList();
		final Map<String, ItemStack> allClaimedPages = Maps.newHashMap();
		final Map<String, Class<? extends net.minecraft.entity.Entity>> allClaimedEntities = Maps.newHashMap();
		final IModEntry[] currentlySupportedMods = this.constantService().
				<IModEntry[]>getConstant("CURRENTLY_SUPPORTED_MODS").orElseThrow();

		for (final IModEntry entry : currentlySupportedMods) {
			final String modId = entry.modId();
			if (!this.mustRegister(modId)) continue;

			final PageRegistryHelper helper = new PageRegistryHelper();
			helper.loadItems();

			//entitiesEntries.addAll(helper.claimEntities(modId));
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
		boolean additionsSkipped = false;
		final IModEntry[] currentlySupportedMods = this.constantService().
				<IModEntry[]>getConstant("CURRENTLY_SUPPORTED_MODS").orElseThrow();

		for (final IModEntry entry : currentlySupportedMods) {
			final Optional<ModContainer> optionalContainer = entry.modContainer();

			if (!optionalContainer.isPresent()) continue;

			final ModContainer container = optionalContainer.get();

			if (!container.getModId().equals(entry.modId())) continue;

			if (container.getVersion().equals(entry.version())) {
				OpenModsIGWApi.get().log().info("Mod %s found: version matches", entry.modId());
				continue;
			}

			OpenModsIGWApi.get().log().info("Identified mod %s, but got different version than expected (%s instead of %s)",
					entry.modId(), container.getVersion(), entry.version());

			if (DEVELOPMENT_ENVIRONMENT_VERSION.equals(container.getVersion())) {
				OpenModsIGWApi.get().log().info("The mod %s installed version (%s) equals the development environment string",
						container.getModId(), container.getVersion());
				OpenModsIGWApi.get().log().info("Skipping addition...");

				if (!additionsSkipped) additionsSkipped = true;

				continue;
			}

			if (container.getMod().getClass().getAnnotation(IMismatchingModEntry.VersionProvider.class) != null) {
				OpenModsIGWApi.get().log().info("Mod provides @VersionProvider annotation. Analyzing data...");
				IMismatchingModEntry.VersionProvider provider = container
						.getMod()
						.getClass()
						.getAnnotation(IMismatchingModEntry.VersionProvider.class);

				if (provider.value().equals(entry.version())) {
					OpenModsIGWApi.get().log().info("The mod %s tells us its version is equivalent to the expected %s.",
							container.getModId(), entry.version());
					OpenModsIGWApi.get().log().info("This usually means that the version consists only of bug fixes");
					OpenModsIGWApi.get().log().info("Since we trust the mod's developer, we skip the addition");

					if (!additionsSkipped) additionsSkipped = true;

					continue;
				}

				OpenModsIGWApi.get().log().info("Provided version was not the one we expected (%s instead of %s)",
						provider.value(), entry.version());
				OpenModsIGWApi.get().log().info("As such, we add the mod to the list anyway.");
			} else {
				OpenModsIGWApi.get().log().info("No alternative version provided.");
			}

			OpenModsIGWApi.get().log().info("Adding to mismatching mod list.");

			final IMismatchingModEntry mismatchingEntry = MismatchingModEntry.of(entry, container.getVersion());
			this.mismatchingMods.add(mismatchingEntry);

			if (!this.guiService().shouldShow(IGuiService.GUIs.MISMATCHING_MODS)) {
				this.guiService().show(IGuiService.GUIs.MISMATCHING_MODS);
			}
		}

		if (this.mismatchingMods.isEmpty() && !additionsSkipped) OpenModsIGWApi.get().log().info("No mismatching mod versions found");

		// Make sure to open the GUI if we are running in debug mode
		// And also, let's add some more entries to the list.
		final Optional<IService<ISystemIdentifierService>> id = OpenModsIGWApi.get()
				.obtainService(ISystemIdentifierService.class);
		if (!id.isPresent()) throw new IllegalStateException("ISystemIdentifierService");
		final ISystemIdentifierService it = id.get().cast();
		if (MISMATCHING_GUI_DEBUG || it.isSystemLevelEnough(it.populate(), ISystemIdentifierService.SystemType.DEVELOPER)) {
			this.debugModVersionsCheck();
			OpenModsIGWApi.get().log().warning("Added debug entries to Mismatching Mods GUI"); // So nobody freaks out (and why would a dev?)
		}
	}

	private void debugModVersionsCheck() {
		if (!this.guiService().shouldShow(IGuiService.GUIs.MISMATCHING_MODS)) {
			this.guiService().show(IGuiService.GUIs.MISMATCHING_MODS);
		}
		this.mismatchingMods.add(MismatchingModEntry.of("test1", "1.0", "1.1"));
		this.mismatchingMods.add(MismatchingModEntry.of("test2", "0.0", "0.1"));
		this.mismatchingMods.add(MismatchingModEntry.of("test3", "v1.0", "v1.1"));
		this.mismatchingMods.add(MismatchingModEntry.of("test4", "v1.0-stable", "v1.0-beta"));
		this.mismatchingMods.add(MismatchingModEntry.of("test5", "", "0.7"));
		this.mismatchingMods.add(MismatchingModEntry.of("test6", "v0.0-stable", "v0.0-beta"));
		this.mismatchingMods.add(MismatchingModEntry.of("test7", "v1.0-stable", "v0.0-beta"));
		this.mismatchingMods.add(MismatchingModEntry.of("test8", "$version$", "$version$"));
	}

	@Nonnull
	private IConstantRetrieverService constantService() {
		return Preconditions.checkNotNull(
				OpenModsIGWApi.get().serviceManager().obtainAndCastService(IConstantRetrieverService.class)
		);
	}

	@Nonnull
	private IGuiService guiService() {
		return Preconditions.checkNotNull(
				OpenModsIGWApi.get().serviceManager().obtainAndCastService(IGuiService.class)
		);
	}
}
