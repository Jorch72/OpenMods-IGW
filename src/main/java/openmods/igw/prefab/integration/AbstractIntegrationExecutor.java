package openmods.igw.prefab.integration;

import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.Pair;

import igwmod.api.WikiRegistry;
import igwmod.gui.tabs.IWikiTab;

import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.integration.IIntegrationExecutor;
import openmods.igw.api.record.mod.IModEntry;
import openmods.igw.api.service.IClassProviderService;
import openmods.igw.prefab.init.PageRegistryHelper;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Provides a skeletal implementation of the {@link IIntegrationExecutor}
 * interface to minimize effort required to implement this interface when
 * referring to OpenMods-IGW "native" methods.
 *
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail. Each of these methods may be overridden.</p>
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public abstract class AbstractIntegrationExecutor implements IIntegrationExecutor {

	private static final String ATTEMPTING_INTEGRATION = "Attempting integration with mod %s";
	private static final String ATTEMPTING_WIKI_INTEGRATION = "Attempting to register wiki page (1/2)";
	private static final String ATTEMPTING_FORGE_INTEGRATION = "Attempting to register Forge event handler (2/2)";
	private static final String LOADED_INTEGRATION = "Integration loaded successfully for mod %s";

	private static final String SEARCH_FAILED = "Unable to look up blocks, items and entities for mod %s";
	private static final String INVALID_CONSTRUCTOR_FOR_TAB = "Unable to instantiate specified tab class %s: no valid constructor found (%s)";
	private static final String INVALID_CONSTRUCTOR_FOR_HANDLER = "Unable to instantiate specified event handler %s: no valid constructor found (%s)";
	private static final String UNKNOWN_ERROR = "An unknown error has occurred";

	private final IModEntry mod;
	private final Class<? extends IWikiTab> tabClass;
	private final Class<?> eventHandlerClass;

	/**
	 * Constructs an instance of this integration executor.
	 *
	 * <p>No parameter can be {@code null}.</p>
	 *
	 * @param mod
	 * 		The mod entry this executor is for.
	 * @param tabClass
	 * 		The class of the wiki tab that should be instantiated.
	 * @param eventHandlerClass
	 * 		The class of the event handler.
	 *
	 * @since 1.0
	 */
	protected AbstractIntegrationExecutor(@Nonnull final IModEntry mod,
										  @Nonnull final Class<? extends IWikiTab> tabClass,
										  @Nonnull final Class<?> eventHandlerClass) {
		this.mod = Preconditions.checkNotNull(mod);
		this.tabClass = Preconditions.checkNotNull(tabClass);
		this.eventHandlerClass = Preconditions.checkNotNull(eventHandlerClass);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The implementation first loads all the various blocks, items and entities
	 * available, claiming the ones that are part of the mod specified in the constructor.
	 * Then, if no items, blocks or entities are found, then the method warns the user
	 * and exits. Otherwise, an instance of the tab class passed in the constructor
	 * is created with the given parameters and then registered to the {@link WikiRegistry}.
	 * The parameters must be, in order, a {@link List} of all the items and blocks
	 * found ({@code List<Pair<String, ItemStack>>}), a {@link Map} of all the pages
	 * claimed ({@code Map<String, ItemStack>}) and a {@code Map} of all the entities
	 * claimed ({@code Map<String, Class<? extends Entity>>}). After that, the provided
	 * handler class is registered to {@link MinecraftForge#EVENT_BUS Forge's event bus}.</p>
	 *
	 * <p>Logging is not extensive, but allows the user to track the path of the various
	 * integration attempts.</p>
	 *
	 * @throws Exception
	 * 		{@inheritDoc}
	 *
	 * @since 1.0
	 */
	@Override
	public void integrate() throws Exception {
		OpenModsIGWApi.get().log().info(ATTEMPTING_INTEGRATION, this.mod.modId());

		this.integrateWikiPage();
		this.integrateWikiHandler();

		OpenModsIGWApi.get().log().info(LOADED_INTEGRATION, this.mod.modId());
	}

	private void integrateWikiPage() {
		OpenModsIGWApi.get().log().info(ATTEMPTING_WIKI_INTEGRATION);

		final PageRegistryHelper helper = new PageRegistryHelper();
		helper.loadItems();

		final List<Pair<String, String>> entitiesEntries = helper.claimEntities(this.mod.modId());
		final List<Pair<String, ItemStack>> itemsAndBlocksEntries = helper.claimModObjects(this.mod.modId());

		final Map<String, ItemStack> allClaimedPages = helper.getAllClaimedPages();
		final Map<String, Class<? extends Entity>> allClaimedEntities = helper.getAllClaimedEntitiesPages();

		if (entitiesEntries.isEmpty() || itemsAndBlocksEntries.isEmpty()) {
			OpenModsIGWApi.get().log().warning(SEARCH_FAILED, this.mod.modId());
			return;
		}

		try {
			this.integrateWikiPage0(itemsAndBlocksEntries, allClaimedPages, allClaimedEntities);
		} catch (final NoSuchMethodException e) {
			OpenModsIGWApi.get().log().warning(e, INVALID_CONSTRUCTOR_FOR_TAB, this.tabClass, e.getMessage());
		} catch (final Exception e) {
			OpenModsIGWApi.get().log().warning(e, UNKNOWN_ERROR);
		}
	}

	private void integrateWikiPage0(final List<Pair<String, ItemStack>> itemsAndBlockEntries,
									final Map<String, ItemStack> allClaimedPages,
									final Map<String, Class<? extends Entity>> allClaimedEntityPages) throws Exception {
		//noinspection JavaReflectionMemberAccess
		final Constructor<?> constructor = this.tabClass.getConstructor(List.class, Map.class, Map.class);
		final Object tabInstance = constructor.newInstance(itemsAndBlockEntries, allClaimedPages, allClaimedEntityPages);
		final IWikiTab tab = IWikiTab.class.cast(tabInstance);
		WikiRegistry.registerWikiTab(tab);

		final IClassProviderService service = Preconditions.checkNotNull(OpenModsIGWApi.get().serviceManager()
				.obtainAndCastService(IClassProviderService.class));

		Preconditions.checkNotNull(Preconditions.checkNotNull(service.proxy()).asPageInit()).addTabForModId(this.mod.modId(), tab);
	}

	private void integrateWikiHandler() {
		OpenModsIGWApi.get().log().info(ATTEMPTING_FORGE_INTEGRATION);

		try {
			MinecraftForge.EVENT_BUS.register(this.eventHandlerClass.getConstructor().newInstance());
		} catch (final NoSuchMethodException e) {
			OpenModsIGWApi.get().log().warning(e, INVALID_CONSTRUCTOR_FOR_HANDLER, this.eventHandlerClass, e.getMessage());
		} catch (final Exception e) {
			OpenModsIGWApi.get().log().warning(e, UNKNOWN_ERROR);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>This implementation does nothing.</p>
	 *
	 * @since 1.0
	 */
	@Override
	public void andThen() {}
}
