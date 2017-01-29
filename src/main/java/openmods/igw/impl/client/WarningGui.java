package openmods.igw.impl.client;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.service.IConstantRetrieverService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class WarningGui extends GuiYesNo {

	private static boolean shouldShow;
	private static final String TITLE = OpenModsIGWApi.get().translate("missing.title");
	private static final String MESSAGE = OpenModsIGWApi.get().translate("missing.text");
	private static final String CONTINUE_BUTTON_LABEL = OpenModsIGWApi.get().translate("button.continue");
	private static final String INSTALL_BUTTON_LABEL = OpenModsIGWApi.get().translate("button.install");
	private static final String INSTALL_BUTTON_WARNING = OpenModsIGWApi.get().translate("button.install.warning");
	private static final String IGW_URL = "http://www.curse.com/mc-mods/minecraft/223815-in-game-wiki-mod";
	private static final int CONTINUE = 0;
	private static final int INSTALL = 1;
	private static final int EXIT_CODE_INTERNAL = "IGW-Hot-Load".hashCode();

	@SuppressWarnings({"WeakerAccess","unused"})
	public WarningGui() {
		super(null,
				TITLE,
				MESSAGE,
				CONTINUE_BUTTON_LABEL,
				INSTALL_BUTTON_LABEL,
				0);
		OpenModsIGWApi.get().log().info("IGW Mod not found. Gui constructed and shown");
		shouldShow = false;
	}

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public static void markShow() {
		if (!OpenModsIGWApi.get().obtainService(IConstantRetrieverService.class).get()
				.cast().getBooleanConfigConstant("enableMissingModWarningMessage").get()) return;
		shouldShow = true;
	}

	@SuppressWarnings("WeakerAccess")
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
					OpenModsIGWApi.get().log().severe("Why would you run a client in a headless environment?");
				}
				// Hot loading is not possible, so...
				cpw.mods.fml.common.FMLCommonHandler.instance().exitJava(EXIT_CODE_INTERNAL, false);
				break;
			default:
				throw new IllegalStateException("Invalid button ID in WarningGui @ OpenMods-IGW");
		}
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float renderPartialTicks) {
		// We want to add a tooltip to the INSTALL button because it opens
		// a web page: I think it is better to warn the user.
		super.drawScreen(mouseX, mouseY, renderPartialTicks);

		final Object btnObj = this.buttonList.get(INSTALL);

		if (!(btnObj instanceof GuiButton)) return;

		final GuiButton btn = (GuiButton) btnObj;

		if (!btn.func_146115_a()) return;

		final List<String> text = Lists.newArrayList();
		text.add(INSTALL_BUTTON_WARNING);
		text.add(IGW_URL);
		this.drawHoveringText(text, mouseX, mouseY, this.fontRendererObj);
	}
}
