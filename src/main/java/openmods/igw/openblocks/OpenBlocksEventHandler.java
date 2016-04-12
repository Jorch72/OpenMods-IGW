package openmods.igw.openblocks;

import net.minecraft.item.ItemStack;

import igwmod.api.PageChangeEvent;
import igwmod.api.VariableRetrievalEvent;

import openmods.igw.common.OpenModsEventHandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
//@Explain("Accessed via reflection")
public class OpenBlocksEventHandler extends OpenModsEventHandler {

	@SubscribeEvent
	public void handleCustomBlocks(final PageChangeEvent event) {

		if (event.currentFile.equals("block/openblocks.canvasglass")) {

			openmods.Log.info("Redirected Glass Canvas page from default to OpenMods-IGW");
			this.redirectToIgwTab(event);
		}
	}

	@SubscribeEvent
	@SuppressWarnings("ConstantConditions")
	//@Explain("Field populated by Forge, so...")
	public void handleCustomIcons(final PageChangeEvent event) {

		if (event.currentFile.contains("block/openblocks.canvasglass") && OpenBlocksItemHolder.GLASS_CANVAS != null) {

			openmods.Log.info("Associated Glass Canvas icon to page");
			this.askTabForNewIcon(event, new ItemStack(OpenBlocksItemHolder.GLASS_CANVAS));
		}
	}

	@SubscribeEvent
	public void addCurrentBlockStatus(final VariableRetrievalEvent event) {

		this.replaceVariableWithBlockStatus(event);
	}

	@SubscribeEvent
	public void addCurrentConfigValues(final VariableRetrievalEvent event) {

		this.replaceVariableWithConfigValue(event);
	}

	@SubscribeEvent
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
