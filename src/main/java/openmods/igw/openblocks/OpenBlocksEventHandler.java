package openmods.igw.openblocks;

import net.minecraft.item.ItemStack;

import igwmod.api.PageChangeEvent;

import openmods.igw.common.CustomHandler;

@CustomHandler
@SuppressWarnings("unused")
//@Explain("Accessed via reflection")
public class OpenBlocksEventHandler {

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

			// Not working when event.associatedStack == null
			// FIXME
			openmods.Log.info("Associated Glass Canvas icon to page");
			event.associatedStack = new ItemStack(OpenBlocksItemHolder.GLASS_CANVAS);
		}
	}
}
