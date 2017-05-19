package openmods.igw.impl.proxy;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import openmods.igw.impl.integration.IntegrationHandler;

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
import openmods.igw.api.service.IClassProviderService;
import openmods.igw.api.service.IConstantRetrieverService;
import openmods.igw.api.service.IGuiService;
import openmods.igw.api.service.IService;
import openmods.igw.api.service.ISystemIdentifierService;
import openmods.igw.impl.client.GuiOpenEventHandler;
import openmods.igw.prefab.record.mod.MismatchingModEntry;

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

		// Register all the various integrations and get ready to load them
		IntegrationHandler.IT.register();

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

		this.handleRegistration();
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
		throw new UnsupportedOperationException("Use IntegrationHandler instead");
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

	private void handleRegistration() {
		// Load all the integrations registered previously
		IntegrationHandler.IT.load();

		this.checkModVersions();
	}

	private void checkModVersions() {
		if (this.mismatchingMods.isEmpty()) OpenModsIGWApi.get().log().info("No mismatching mod versions found");
		else if (!this.guiService().shouldShow(IGuiService.GUIs.MISMATCHING_MODS)) this.guiService().show(IGuiService.GUIs.MISMATCHING_MODS);

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
