package openmods.igw.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;

import openmods.igw.utils.TranslationUtilities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

@SideOnly(Side.CLIENT)
public class WarningGui extends GuiYesNo {

	private static boolean shouldShow;
	private static final String TITLE = TranslationUtilities.translate("missing.title");
	private static final String MESSAGE = TranslationUtilities.translate("missing.text");
	private static final String IGW_URL = "http://www.curse.com/mc-mods/minecraft/223815-in-game-wiki-mod";
	private static final int CONTINUE = 0;
	private static final int INSTALL = 1;
	private static final int EXIT_CODE_INTERNAL = -101;

	public WarningGui() {
		super(null,
				TITLE,
				MESSAGE,
				TranslationUtilities.translate("button.continue"),
				TranslationUtilities.translate("button.install"),
				0);
		openmods.Log.info("IGW Mod not found. Gui constructed and shown");
		shouldShow = false;
	}

	public static void markShow() {
		shouldShow = true;
	}

	public static boolean shallShow() {
		return shouldShow;
	}

	@Override
	protected void actionPerformed(final GuiButton button) {
		switch (button.id) {
			case CONTINUE:
				this.mc.displayGuiScreen(null);
				break;
			case INSTALL:
				try {
					java.awt.Desktop.getDesktop().browse(new java.net.URL(IGW_URL).toURI());
				} catch (final MalformedURLException e) {
					throw new RuntimeException(e);
				} catch (final URISyntaxException e) {
					throw new RuntimeException(e);
				} catch (final IOException e) {
					openmods.Log.warn("Why would you run a client in a headless environment?");
				}
				// Hot loading is not possible, so...
				cpw.mods.fml.common.FMLCommonHandler.instance().exitJava(EXIT_CODE_INTERNAL, false);
				break;
			default:
				throw new IllegalStateException("Invalid button ID in WarningGui @ OpenMods-IGW");
		}
	}
}
