package openmods.igw.client;

import com.google.common.collect.Sets;
import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import openmods.config.gui.OpenModsConfigScreen;
import openmods.igw.utils.Constants;

import java.util.Set;

public class GuiFactory implements IModGuiFactory {

	public static class ConfigGui extends OpenModsConfigScreen {

		public ConfigGui(final GuiScreen parent) {
			super(parent, Constants.MOD_ID, Constants.NAME);
		}
	}

	@Override
	public void initialize(final Minecraft mcInstance) { }

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return GuiFactory.ConfigGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return Sets.newHashSet();
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(final RuntimeOptionCategoryElement element) {
		return null;
	}
}
