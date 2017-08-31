package openmods.igw.prefab.handler;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;

import igwmod.api.PageChangeEvent;
import igwmod.api.VariableRetrievalEvent;

import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.cache.IgnoreCache;
import openmods.igw.api.handler.IEventHandler;
import openmods.igw.api.init.IPageInit;
import openmods.igw.api.proxy.IInitProxy;
import openmods.igw.api.service.IClassProviderService;
import openmods.igw.api.service.IService;

import java.lang.reflect.Field;
import java.util.Map;
import javax.annotation.Nonnull;

// TODO Add API to all methods (of subclasses?)
public abstract class OpenModsEventHandler implements IEventHandler {

	private enum VariableType {
		BLOCK,
		CONFIG,
		ITEM;

		private final String id;

		VariableType() {
			this.id = this.toString().toLowerCase(java.util.Locale.ENGLISH).trim();
		}

		public final String id() {
			return this.id;
		}

		public static VariableType construct(final String from) {

			final String[] parts = from.substring(1).split(java.util.regex.Pattern.quote("@"));

			if ("block".equals(parts[2])) return VariableType.BLOCK;
			else if ("item".equals(parts[2])) return VariableType.ITEM;
			else if ("config".equals(parts[2])) return VariableType.CONFIG;
			else throw new IllegalArgumentException("Unrecognized variable type.");
		}
	}

	private static final Map<String, Boolean> CACHE_BLOCK = Maps.newHashMap();
	private static final Map<String, Object> CACHE_CONFIG = Maps.newHashMap();
	private static final Map<String, Boolean> CACHE_ITEM = Maps.newHashMap();

	private static final String PAGE_ID = "openmods-igw:";
	@SuppressWarnings("unused")
	private static final String OM_VARIABLE_SYNTAX = "@var@%s@%s@%s"; // @var@<modId>@<category>@<optionName>
	private static final String OM_VARIABLE_PREFIX_RAW = "@var@%s@%s@";

	@Nonnull
	@Override
	public abstract String modId();

	@Deprecated
	@Nonnull
	@Override
	public String getModId() {
		return this.modId();
	}

	@Nonnull
	@Override
	public abstract String configClass();

	@Deprecated
	@Nonnull
	@Override
	public String getConfigClass() {
		return this.configClass();
	}

	protected final void redirectToIgwTab(final PageChangeEvent event) {
		event.currentFile = PAGE_ID + event.currentFile;
	}

	protected final void askTabForNewIcon(final PageChangeEvent event, final ItemStack newIcon) {
		final StackTraceElement[] callStack = new Exception().getStackTrace();
		boolean allowed = false;

		for (int i = 0; i < callStack.length; ++i) {
			if (i == 0) continue;
			if (i > 2) break;
			if (callStack[i].getClassName().startsWith("openmods.igw.")) allowed = true;
		}

		if (!allowed) {
			OpenModsIGWApi.get().log().warning("Attempt of changing the icon of the tab stopped.");
			OpenModsIGWApi.get().log().warning("Call stack:");
			OpenModsIGWApi.get().log().warning(new Exception(), "");
			return;
		}

		event.associatedStack = newIcon;
		// Since we can't be sure of it, we ask the tab to override the icon.
		final Optional<IService<IClassProviderService>> serviceOptional = OpenModsIGWApi.get()
				.obtainService(IClassProviderService.class);
		if (!serviceOptional.isPresent()) throw new IllegalStateException("Class Provider Service unavailable");
		final IInitProxy proxy = serviceOptional.get().cast().proxy();
		if (proxy == null) {
			OpenModsIGWApi.get().log().severe("Unable to find proxy. Crashing...");
			throw new IllegalStateException("Proxy unavailable");
		}
		final IPageInit pageInit = proxy.asPageInit();
		if (pageInit == null) {
			OpenModsIGWApi.get().log().severe("Unable to obtain proxy instance as an IPageInit. Aborting...");
			return;
		}
		final OpenModsWikiTab tab = this.cast(pageInit.getTabForModId(this.modId()));
		tab.askForIconOverride(newIcon);
	}

	private OpenModsWikiTab cast(final igwmod.gui.tabs.IWikiTab tab) {
		if (!(tab instanceof OpenModsWikiTab)) throw new IllegalArgumentException();
		return (OpenModsWikiTab) tab;
	}

	protected void replaceVariableWithBlockStatus(final VariableRetrievalEvent event) {
		final String variable = event.variableName;
		final VariableType type = VariableType.construct(variable);

		if (!type.equals(VariableType.BLOCK)) return;
		final String configId = variable.substring(this.getPrefix(type).length());

		if (configId.contains("@")) {
			OpenModsIGWApi.get().log().warning("Malformed variable name (%s): could not match block name %s", variable, configId);
			return;
		}

		OpenModsIGWApi.get().log().info("Replacing variable (%s) with status of block", variable);

		if (CACHE_BLOCK.containsKey(configId)) {
			event.replacementValue = CACHE_BLOCK.get(configId).toString();
			return;
		}

		// FIXME Deprecation
		CACHE_BLOCK.put(configId,
				net.minecraftforge.fml.common.registry.GameRegistry.findBlock(this.modId(), configId) != null);

		event.replacementValue = CACHE_BLOCK.get(configId).toString();
	}

	protected void replaceVariableWithItemStatus(final VariableRetrievalEvent event) {
		final String variable = event.variableName;
		final VariableType type = VariableType.construct(variable);

		if (!type.equals(VariableType.ITEM)) return;
		final String configId = variable.substring(this.getPrefix(type).length());

		if (configId.contains("@")) {
			OpenModsIGWApi.get().log().warning("Malformed variable name (%s): could not match item name %s", variable, configId);
			return;
		}

		OpenModsIGWApi.get().log().info("Replacing variable (%s) with status of block", variable);

		if (CACHE_ITEM.containsKey(configId)) {
			event.replacementValue = CACHE_ITEM.get(configId).toString();
			return;
		}

		// FIXME Deprecation
		CACHE_ITEM.put(configId,
				net.minecraftforge.fml.common.registry.GameRegistry.findBlock(this.modId(), configId) != null);

		event.replacementValue = CACHE_ITEM.get(configId).toString();
	}

	// Don't substitute with API calls: config class can vary
	protected void replaceVariableWithConfigValue(final VariableRetrievalEvent event) {
		final String variable = event.variableName;
		final VariableType type = VariableType.construct(variable);

		if (!type.equals(VariableType.CONFIG)) return;
		String configId = variable.substring(this.getPrefix(type).length());

		if (configId.contains("@")) {
			OpenModsIGWApi.get().log().warning("Malformed variable name (%s): could not match config name %s", variable, configId);
			return;
		}

		OpenModsIGWApi.get().log().info("Replacing variable (%s) with config value", variable);

		if (CACHE_CONFIG.containsKey(configId)) {
			event.replacementValue = CACHE_CONFIG.get(configId).toString();
			return;
		}

		try {
			final Class<?> cConfig = Class.forName(this.configClass());
			final Field fSpecified = cConfig.getDeclaredField(configId);
			final Object fieldValue = fSpecified.get(null); //Config values must be public and static
			if (fSpecified.getAnnotation(IgnoreCache.class) == null) CACHE_CONFIG.put(configId, fieldValue);
			event.replacementValue = fieldValue.toString();
		} catch (final Exception e) {
			OpenModsIGWApi.get().log().severe(e, "Could not substitute variable value. See exception for more information.");
		}
	}

	private String getPrefix(final VariableType varType) {
		return String.format(OM_VARIABLE_PREFIX_RAW, this.modId(), varType.id());
	}
}
