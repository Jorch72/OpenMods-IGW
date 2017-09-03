package openmods.igw.prefab.integration;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.integration.IIntegrationExecutor;
import openmods.igw.api.integration.IIntegrationProvider;
import openmods.igw.api.proxy.IInitProxy;
import openmods.igw.api.record.mod.IMismatchingModEntry;
import openmods.igw.api.record.mod.IModEntry;
import openmods.igw.api.service.IClassProviderService;
import openmods.igw.api.service.IConstantRetrieverService;
import openmods.igw.prefab.record.mod.MismatchingModEntry;

import javax.annotation.Nonnull;

/**
 * Provides a skeletal implementation of the {@link IIntegrationProvider}
 * interface to minimize effort required to implement this interface when
 * referring to OpenMods-IGW "native" methods.
 *
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail. Each of these methods may be overridden.</p>
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public abstract class AbstractIntegrationProvider implements IIntegrationProvider {

    private static final String MOD_NOT_FOUND = "Mod %s could not be found: integration won't be loaded";
    private static final String CONFIG_DISABLED = "Mod %s integration has been disabled in config file";
    private static final String UNMET_CONDITION = "A condition needed to load mod %s integration was not met";
    private static final String MOD_CONTAINER_NOT_FOUND = "Container for mod %s could not be found: this is a serious error. Ignoring for now";
    private static final String MATCHING_MOD_AND_VERSION = "Found matching mod and version (%s, version %s)";
    private static final String MISMATCHING_VERSIONS = "Mod %s identified, but version doesn't match (got %s, expected %s)";
    private static final String VERSION_IS_IN_DEV = "Version matches development environment version: loading anyway";
    private static final String MISMATCHING_PROVIDED_VERSION = "Mod identified, but annotation-provided version doesn't match (got %s, expected %s)";
    private static final String INTEGRATION_FAILED_DUE_TO_EXCEPTION = "Mod %s integration loading failed due to an exception (%s: %s). Stacktrace follows";

    private static final String DEVELOPMENT_ENVIRONMENT_VERSION = "$VERSION$";

    private final IModEntry mod;

    private String foundVersion;

    /**
     * Constructs an instance of this integration provider.
     *
     * <p>The passed in {@code IModEntry} should specify the mod ID this
     * integration is for and the expected version. Refer to the
     * {@link IModEntry}'s documentation for more information.</p>
     *
     * @param mod
     *         The entry which represents the mod this integration is for.
     *         It must not be {@code null}.
     *
     * @since 1.0
     */
    protected AbstractIntegrationProvider(@Nonnull final IModEntry mod) {
        this.mod = Preconditions.checkNotNull(mod);
        this.foundVersion = null;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation first checks, using {@link Loader FML's Loader} if
     * the given mod is loaded. If it isn't then the integration reports an error.
     * If the mod is loaded, the method checks if the integration itself is enabled
     * in the configuration file. If it is, then it asks the implementation of this
     * class id any special condition is met. After that, the version of the mod
     * present in the environment is checked. If the version installed matches
     * the expected one or the {@link #DEVELOPMENT_ENVIRONMENT_VERSION}, then
     * loading proceeds. Otherwise, the {@link IMismatchingModEntry.VersionProvider}
     * annotation is checked, if present. If the provided version matched the one
     * we are looking for, then loading can take place. Otherwise, an
     * {@link IIntegrationProvider.ScanResult#MAY_CAUSE_ERRORS} return value is
     * used. In any other cases, {@link IIntegrationProvider.ScanResult#UNSAFE_TO_LOAD}
     * is returned.</p>
     *
     * <p>Every step is logged, so that the entire process can be traced.</p>
     *
     * @return
     *         {@inheritDoc}
     *
     * @since 1.0
     */
    @Nonnull
    @Override
    public final ScanResult getAbilityToLoadIntegration() {
        OpenModsIGWApi.get().log().info("Looking for mod %s", this.mod.modId());
        return this.scanEnvironment();
    }

    @Nonnull
    @Override
    public abstract IIntegrationExecutor getExecutor();

    @Nonnull
    private ScanResult refuseWithMessage(@Nonnull final String message) {
        OpenModsIGWApi.get().log().info(message, this.mod.modId());
        return ScanResult.UNSAFE_TO_LOAD;
    }

    @Nonnull
    private ScanResult scanEnvironment() {
        if (!Loader.isModLoaded(this.mod.modId())) return this.refuseWithMessage(MOD_NOT_FOUND);

        if (!Preconditions.checkNotNull(
                OpenModsIGWApi.get().serviceManager().obtainAndCastService(IConstantRetrieverService.class)
            ).isEnabled(this.mod.modId())) {
            return this.refuseWithMessage(CONFIG_DISABLED);
        }

        final ScanResult customScan = this.checkSpecialConditions();
        if (customScan == ScanResult.UNSAFE_TO_LOAD) return this.refuseWithMessage(UNMET_CONDITION);

        final ScanResult modVersionCheck = this.checkModVersion();

        return customScan == ScanResult.SAFE_TO_LOAD? modVersionCheck : customScan;
    }

    /**
     * Checks for some special conditions that are not covered by default,
     * as explained in {@link #getAbilityToLoadIntegration()}.
     *
     * <p>Refer to the linked method's documentation to see what is already
     * checked.</p>
     *
     * <p>This method must not, by contract, throw exceptions voluntarily or
     * return a {@code null} value.</p>
     *
     * <p>By default, this method's implementation returns
     * {@link IIntegrationProvider.ScanResult#SAFE_TO_LOAD} without any additional
     * checks, i.e. no special conditions are present.</p>
     *
     * @return
     *         The ability for the current environment to load the integration,
     *         according to the conditions specified in this method itself.
     *
     * @since 1.0
     */
    @Nonnull
    @SuppressWarnings("WeakerAccess")
    protected ScanResult checkSpecialConditions() {
        return ScanResult.SAFE_TO_LOAD;
    }

    @Nonnull
    private ScanResult checkModVersion() {
        final Optional<ModContainer> optionalContainer = this.mod.modContainer();
        if (!optionalContainer.isPresent()) return this.refuseWithMessage(MOD_CONTAINER_NOT_FOUND);
        final ModContainer container = optionalContainer.get();
        if (!container.getModId().equals(this.mod.modId())) return this.refuseWithMessage(MOD_CONTAINER_NOT_FOUND);

        if (container.getVersion().equals(this.mod.version())) {
            OpenModsIGWApi.get().log().info(MATCHING_MOD_AND_VERSION, this.mod.modId(), this.mod.version());
            return ScanResult.SAFE_TO_LOAD;
        }

        OpenModsIGWApi.get().log().info(MISMATCHING_VERSIONS, this.mod.modId(), container.getVersion(), this.mod.version());

        if (DEVELOPMENT_ENVIRONMENT_VERSION.equals(container.getVersion())) {
            OpenModsIGWApi.get().log().info(VERSION_IS_IN_DEV);
            return ScanResult.SAFE_TO_LOAD;
        }

        if (container.getMod().getClass().getAnnotation(IMismatchingModEntry.VersionProvider.class) != null) {
            return this.analyzeAnnotationData(container.getMod().getClass().getAnnotation(IMismatchingModEntry.VersionProvider.class));
        }

        OpenModsIGWApi.get().log().info("Version mismatch identified");
        this.foundVersion = container.getVersion();
        return ScanResult.MAY_CAUSE_ERRORS;
    }

    @Nonnull
    private ScanResult analyzeAnnotationData(final IMismatchingModEntry.VersionProvider provider) {
        OpenModsIGWApi.get().log().info("Mod provides @IMismatchingModEntry.VersionProvider annotation: analyzing data");

        if (!provider.value().equals(this.mod.version())) {
            OpenModsIGWApi.get().log().info(MISMATCHING_PROVIDED_VERSION, provider.value(), this.mod.version());
            return ScanResult.MAY_CAUSE_ERRORS;
        }

        OpenModsIGWApi.get().log().info("Annotation-provided version matches expectations: loading anyway");
        return ScanResult.SAFE_TO_LOAD;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation does nothing.</p>
     *
     * @since 1.0
     */
    @Override
    public void onLoaded() {}

    /**
     * {@inheritDoc}
     *
     * <p>This implementation logs the mismatch and tells OpenMods-IGW
     * to add it to the list of mismatching mod versions.</p>
     *
     * <p>If a version is present, then the mod uses that, otherwise an
     * error string is stored instead. Then, a {@link IMismatchingModEntry}
     * is constructed and registered to the proxy, iff it is available
     * and no errors are thrown in the process. If any part of the chain
     * of method calls returns {@code null} the process is going to fail
     * "silently" (no exceptions: just log messages).</p>
     *
     * @since 1.0
     */
    @Override
    public void onEnvironmentMismatch() {
        if (this.foundVersion == null) {
            OpenModsIGWApi.get().log().severe("Mismatch found but no mismatching version given! THIS IS A SERIOUS PROGRAMMING ERROR!");
            this.foundVersion = "~~ERROR: java.lang.NullPointerException~~";
        }

        final IMismatchingModEntry entry = MismatchingModEntry.of(this.mod, this.foundVersion);
        final IClassProviderService service = OpenModsIGWApi.get().serviceManager().obtainAndCastService(IClassProviderService.class);
        if (service == null) {
            OpenModsIGWApi.get().log().warning("Unable to add mod to mismatching list");
            return;
        }

        final IInitProxy proxy = service.proxy();
        if (proxy == null) {
            OpenModsIGWApi.get().log().severe("Integration loading attempted too early: THIS IS A SEROUS PROGRAMMING ERROR!");
            return;
        }

        proxy.addMismatchingMod(entry);
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation tells OpenMods-IGW to log the exception to
     * the log file.</p>
     *
     * @param e
     *         {@inheritDoc}
     *
     * @since 1.0
     */
    @Override
    public void onException(@Nonnull final Exception e) {
        OpenModsIGWApi.get().log().warning(e, INTEGRATION_FAILED_DUE_TO_EXCEPTION, this.mod.modId(), e.getClass(), e.getMessage());
    }

    /**
     * Gets the mod entry used to instantiate this provider.
     *
     * @return
     *         The mod entry used to instantiate this provider.
     *
     * @since 1.0
     */
    @Nonnull
    protected IModEntry getModEntry() {
        return this.mod;
    }
}
