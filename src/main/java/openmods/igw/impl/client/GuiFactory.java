package openmods.igw.impl.client;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import openmods.config.gui.OpenModsConfigScreen;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.service.IConstantRetrieverService;
import openmods.igw.api.service.IService;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class GuiFactory implements IModGuiFactory {

	public static class ConfigGui extends OpenModsConfigScreen {

		public ConfigGui(final GuiScreen parent) {
			super(parent,
					constant("MOD_ID"),
					constant("NAME"));
		}

		@Nullable // Dem static hax tho
		private static String constant(@Nonnull final String name) {
			final Optional<IService<IConstantRetrieverService>> service = OpenModsIGWApi.get()
					.obtainService(IConstantRetrieverService.class);
			if (!service.isPresent()) {
				throw new RuntimeException(new IllegalStateException("Constant retriever service unavailable"));
			}
			return service.get().cast().<String>getConstant(name).orNull();
		}
	}

	@Override
	public void initialize(final Minecraft mcInstance) {}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return GuiFactory.ConfigGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return Sets.newHashSet();
	}

	@Override
	@SuppressWarnings("deprecation") // How else? Considering I have to implement this method
	public RuntimeOptionGuiHandler getHandlerFor(final RuntimeOptionCategoryElement element) {
		return null;
	}
}
