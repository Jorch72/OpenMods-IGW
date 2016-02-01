package openmods.igw.client;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiOpenEventHandler {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onMainMenuOpen(final GuiOpenEvent event) {
		if (!(event.gui instanceof GuiMainMenu)) return;
		if (!WarningGui.shallShow()) return;
		event.gui = new WarningGui();
	}
}
