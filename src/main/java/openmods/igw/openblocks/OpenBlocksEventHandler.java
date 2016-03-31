package openmods.igw.openblocks;

import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;

import igwmod.api.PageChangeEvent;
import igwmod.api.VariableRetrievalEvent;
import igwmod.gui.tabs.IWikiTab;

import openmods.Mods;
import openmods.igw.common.CustomHandler;
import openmods.igw.common.OpenModsWikiTab;

import java.lang.reflect.Field;
import java.util.Map;

@CustomHandler
@SuppressWarnings("unused")
//@Explain("Accessed via reflection")
//TODO OOP (Common Event Handler)
public class OpenBlocksEventHandler {

	private static final Map<String, Boolean> CACHE_BLOCK = Maps.newHashMap();
	private static final Map<String, Object> CACHE_CONFIG = Maps.newHashMap();
	private static final Map<String, Boolean> CACHE_ITEM = Maps.newHashMap();

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.PAGE_OPENED)
	public void handleCustomBlocks(final PageChangeEvent event) {

		if (event.currentFile.equals("block/openblocks.canvasglass")) {

			openmods.Log.info("Redirected Glass Canvas page from default to OpenMods-IGW");
			event.currentFile = "openmods-igw:" + event.currentFile;
		}
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.PAGE_OPENED)
	@SuppressWarnings("ConstantConditions")
	//@Explain("Field populated by Forge, so...")
	public void handleCustomIcons(final PageChangeEvent event) {

		if (event.currentFile.contains("block/openblocks.canvasglass") && OpenBlocksItemHolder.GLASS_CANVAS != null) {

			openmods.Log.info("Associated Glass Canvas icon to page");
			final ItemStack newIcon = new ItemStack(OpenBlocksItemHolder.GLASS_CANVAS);
			event.associatedStack = newIcon;
			// Since we can't be sure of it, we ask the tab to override the icon.
			final OpenModsWikiTab tab = this.cast(
					((openmods.igw.utils.IPageInit) openmods.igw.OpenModsIGW.proxy()) //cast because we know it's safe
							.getTabForModId(openmods.Mods.OPENBLOCKS));
			tab.askForIconOverride(newIcon);
		}
	}

	// Variable syntax: @var@<modid>@<type>@<value>
	// where type is currently:
	//    - block (for enabled/disabled blocks)
	//    - config (for configuration options)
	//    - item (for enabled/disabled items)
	// TODO OOP

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.VARIABLE_SUBSTITUTION)
	public void addCurrentBlockStatus(final VariableRetrievalEvent event) {

		final String variable = event.variableName;

		if (!this.canApply("block", variable)) return;
		String configId = variable.substring(this.identifyVariable("block").length());
		if (configId.startsWith("@")) configId = configId.substring(1);
		if (configId.contains("@")) {
			openmods.Log.warn("Malformed variable name (%s): could not match config name %s", variable, configId);
			return;
		}

		openmods.Log.info("Replacing variable (%s) with status of block", variable);

		if (CACHE_BLOCK.containsKey(configId)) {
			event.replacementValue = CACHE_BLOCK.get(configId).toString();
			return;
		}

		CACHE_BLOCK.put(configId,
				cpw.mods.fml.common.registry.GameRegistry.findBlock(Mods.OPENBLOCKS, configId) != null);

		event.replacementValue = CACHE_BLOCK.get(configId).toString();
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.VARIABLE_SUBSTITUTION)
	public void addCurrentConfigValues(final VariableRetrievalEvent event) {

		final String variable = event.variableName;

		if (!this.canApply("config", variable)) return;
		String configId = variable.substring(this.identifyVariable("config").length());
		if (configId.startsWith("@")) configId = configId.substring(1);
		if (configId.contains("@")) {
			openmods.Log.warn("Malformed variable name (%s): could not match config name %s", variable, configId);
			return;
		}

		openmods.Log.info("Replacing variable (%s) with config value", variable);

		if (CACHE_CONFIG.containsKey(configId)) {
			event.replacementValue = CACHE_CONFIG.get(configId).toString();
			return;
		}

		try {

			final Class<?> cConfig = Class.forName("openblocks.Config");
			final Field fSpecified = cConfig.getDeclaredField(configId);
			final Object fieldValue = fSpecified.get(null); //Config values must be public and static
			CACHE_CONFIG.put(configId, fieldValue);
			event.replacementValue = fieldValue.toString();
		} catch (final Exception e) {

			openmods.Log.severe(e, "Could not substitute variable value. See exception for more information.");
		}
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.VARIABLE_SUBSTITUTION)
	public void addCurrentItemStatus(final VariableRetrievalEvent event) {

		final String variable = event.variableName;

		if (!this.canApply("item", variable)) return;
		String configId = variable.substring(this.identifyVariable("item").length());
		if (configId.startsWith("@")) configId = configId.substring(1);
		if (configId.contains("@")) {
			openmods.Log.warn("Malformed variable name (%s): could not match config name %s", variable, configId);
			return;
		}

		openmods.Log.info("Replacing variable (%s) with status of item", variable);

		if (CACHE_ITEM.containsKey(configId)) {
			event.replacementValue = CACHE_ITEM.get(configId).toString();
			return;
		}

		CACHE_ITEM.put(configId,
				cpw.mods.fml.common.registry.GameRegistry.findItem(Mods.OPENBLOCKS, configId) != null);

		event.replacementValue = CACHE_ITEM.get(configId).toString();
	}

	private OpenModsWikiTab cast(final IWikiTab tab) {
		if (!(tab instanceof OpenModsWikiTab)) throw new IllegalArgumentException();
		return (OpenModsWikiTab) tab;
	}

	private boolean canApply(final String type, final String variable) {

		return variable.contains("@" + type + "@");
	}

	private String identifyVariable(final String type) {

		return String.format("@var@%s@%s@", openmods.Mods.OPENBLOCKS, type);
	}
}
