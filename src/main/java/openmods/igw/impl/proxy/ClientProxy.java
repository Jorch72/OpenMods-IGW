package openmods.igw.impl.proxy;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import igwmod.api.WikiRegistry;
import igwmod.gui.tabs.IWikiTab;
import io.netty.util.internal.ThreadLocalRandom;

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
import openmods.igw.impl.integration.IntegrationHandler;
import openmods.igw.prefab.record.mod.MismatchingModEntry;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO Move some things to API (Remove direct implementation imports)
@SideOnly(Side.CLIENT)
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
        if (!net.minecraftforge.fml.common.Loader.isModLoaded(Mods.IGW)) this.abort = true;
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

        // Join beta program before handling registration: some integrations may, in fact, be supported
        // only in a beta stage
        this.attemptToJoinBetaProgram();

        // Register all the various integrations and get ready to load them
        IntegrationHandler.IT.register();
    }

    // This allows us to use return statements without the possibility to see our
    // code not run because of special conditions (aka Developer system)
    private void attemptToJoinBetaProgram() {
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
        this.handleModIntegrations();
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

    @Nullable
    @Override
    public IWikiTab getTabForModId(@Nonnull final String modId) {
        return this.currentTabs.get(modId);
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

    private void handleModIntegrations() {
        // Load all the integrations registered previously
        IntegrationHandler.IT.load();

        // Show mismatching mod screen if some integrations have reported mismatching versions
        this.checkForReportedMismatchingMods();
    }

    private void checkForReportedMismatchingMods() {
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

        final List<String> s = ImmutableList.of("0.1", "0.0", "1.0", "1.1", "stable", "beta", "", "$version$", "random", "-");
        final Random rng = ThreadLocalRandom.current();
        final int size = s.size();

        for (int i = 0; i < 10; ++i) {
            this.mismatchingMods.add(MismatchingModEntry.of("id" + i, s.get(rng.nextInt(size)), s.get(rng.nextInt(size))));
        }

        Collections.shuffle(this.mismatchingMods);
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
