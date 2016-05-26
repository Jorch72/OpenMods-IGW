package openmods.igw.impl.client;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import openmods.igw.OpenModsIGW;

@SideOnly(Side.CLIENT)
public class GuiOpenEventHandler {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unused")
	//@Explain("Called by Forge, not by us")
	public void onMainMenuOpen(final GuiOpenEvent event) {
		if (!(event.gui instanceof GuiMainMenu)) return;

		if (WarningGui.shallShow()) {
			event.gui = new WarningGui();
			return;
		}

		if (MismatchingVersionsGui.shouldShow()) {
			event.gui = new MismatchingVersionsGui(OpenModsIGW.proxy().getMismatchingMods());
		}
	}
}
