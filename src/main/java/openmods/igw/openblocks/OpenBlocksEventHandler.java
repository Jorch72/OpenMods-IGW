package openmods.igw.openblocks;

import net.minecraft.item.ItemStack;

import igwmod.api.PageChangeEvent;
import igwmod.api.VariableRetrievalEvent;

import openmods.igw.common.CustomHandler;
import openmods.igw.common.OpenModsEventHandler;

@CustomHandler
@SuppressWarnings("unused")
//@Explain("Accessed via reflection")
public class OpenBlocksEventHandler extends OpenModsEventHandler {

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.PAGE_OPENED)
	public void handleCustomBlocks(final PageChangeEvent event) {

		if (event.currentFile.equals("block/openblocks.canvasglass")) {

			openmods.Log.info("Redirected Glass Canvas page from default to OpenMods-IGW");
			this.redirectToIgwTab(event);
		}
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.PAGE_OPENED)
	@SuppressWarnings("ConstantConditions")
	//@Explain("Field populated by Forge, so...")
	public void handleCustomIcons(final PageChangeEvent event) {

		if (event.currentFile.contains("block/openblocks.canvasglass") && OpenBlocksItemHolder.GLASS_CANVAS != null) {

			openmods.Log.info("Associated Glass Canvas icon to page");
			this.askTabForNewIcon(event, new ItemStack(OpenBlocksItemHolder.GLASS_CANVAS));
		}
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.VARIABLE_SUBSTITUTION)
	public void addCurrentBlockStatus(final VariableRetrievalEvent event) {

		this.replaceVariableWithBlockStatus(event);
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.VARIABLE_SUBSTITUTION)
	public void addCurrentConfigValues(final VariableRetrievalEvent event) {

		this.replaceVariableWithConfigValue(event);
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.VARIABLE_SUBSTITUTION)
	public void addCurrentItemStatus(final VariableRetrievalEvent event) {

		this.replaceVariableWithItemStatus(event);
	}

	@Override
	protected String getModId() {
		return openmods.Mods.OPENBLOCKS;
	}

	@Override
	protected String getConfigClass() {
		return "openblocks.Config";
	}
}
