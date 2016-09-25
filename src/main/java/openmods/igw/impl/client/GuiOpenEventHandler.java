package openmods.igw.impl.client;

import com.google.common.base.Preconditions;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.service.IClassProviderService;
import openmods.igw.api.service.IGuiService;

@SideOnly(Side.CLIENT)
public class GuiOpenEventHandler {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unused")
	//@Explain("Called by Forge, not by us")
	public void onMainMenuOpen(final GuiOpenEvent event) {
		if (!(event.gui instanceof GuiMainMenu)) return;

		final IGuiService it = Preconditions.checkNotNull(OpenModsIGWApi.get().serviceManager()
				.obtainAndCastService(IGuiService.class));

		if (it.shouldShow(IGuiService.GUIs.WARNING)) {
			event.gui = it.construct(IGuiService.GUIs.WARNING);
			return;
		}

		if (it.shouldShow(IGuiService.GUIs.MISMATCHING_MODS)) {
			event.gui = it.construct(IGuiService.GUIs.MISMATCHING_MODS, Preconditions.checkNotNull(
					Preconditions.checkNotNull(
							OpenModsIGWApi.get().serviceManager().obtainAndCastService(IClassProviderService.class)
					).proxy()
			).getMismatchingMods());
		}
	}
}
