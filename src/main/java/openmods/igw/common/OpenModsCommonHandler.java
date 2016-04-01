package openmods.igw.common;

import igwmod.api.PageChangeEvent;
import igwmod.api.VariableRetrievalEvent;

import openmods.igw.openblocks.OpenBlocksEventHandler;

@CustomHandler
@SuppressWarnings("unused")
//@Explain("Access via reflection")
public class OpenModsCommonHandler extends OpenModsEventHandler {

	private String currentMod;
	private OpenBlocksEventHandler handlerForOpenBlocks;

	public OpenModsCommonHandler() {
		this.handlerForOpenBlocks = new OpenBlocksEventHandler();
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.PAGE_OPENED)
	public void handleCustomBlocks(final PageChangeEvent event) {

		if (event.currentFile.contains(openmods.Mods.OPENBLOCKS.toLowerCase())) {
			this.currentMod = openmods.Mods.OPENBLOCKS;
			this.handlerForOpenBlocks.handleCustomBlocks(event);
			//return;
		}
		// Insert here others
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.PAGE_OPENED)
	@SuppressWarnings("ConstantConditions")
	//@Explain("Field populated by Forge, so...")
	public void handleCustomIcons(final PageChangeEvent event) {

		if (event.currentFile.contains(openmods.Mods.OPENBLOCKS.toLowerCase())) {
			this.currentMod = openmods.Mods.OPENBLOCKS;
			this.handlerForOpenBlocks.handleCustomIcons(event);
			//return;
		}
		// Insert here others
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.VARIABLE_SUBSTITUTION)
	public void addCurrentBlockStatus(final VariableRetrievalEvent event) {
		this.setModId(event);
		this.replaceVariableWithBlockStatus(event);
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.VARIABLE_SUBSTITUTION)
	public void addCurrentConfigValues(final VariableRetrievalEvent event) {
		this.setModId(event);
		this.replaceVariableWithConfigValue(event);
	}

	@CustomHandler.HandlerMethod(event = CustomHandler.HandlerMethod.Events.VARIABLE_SUBSTITUTION)
	public void addCurrentItemStatus(final VariableRetrievalEvent event) {
		this.setModId(event);
		this.replaceVariableWithItemStatus(event);
	}

	@Override
	protected String getModId() {
		return this.currentMod;
	}

	@Override
	protected String getConfigClass() {
		return this.getConfigClass(this.getModId());
	}

	private boolean setModId(final VariableRetrievalEvent event) {
		this.currentMod = null; // Just make sure it is null.
		final String[] parts = event.variableName.substring(1).split(java.util.regex.Pattern.quote("@"));
		if (parts.length != 4) return false;
		final String id = parts[1];

		if (id.equals(openmods.Mods.OPENBLOCKS)) this.currentMod = openmods.Mods.OPENBLOCKS;
		// Insert others here

		return this.currentMod != null;
	}

	private String getConfigClass(final String modId) {
		// Hardcoding...
		if (modId.equals(openmods.Mods.OPENBLOCKS)) return "openblocks.Config";
		// Insert here other mods.
		else return null;
	}
}
