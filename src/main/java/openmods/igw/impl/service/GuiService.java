package openmods.igw.impl.service;

import openmods.igw.api.record.mod.IMismatchingModEntry;
import openmods.igw.api.service.IGuiService;
import openmods.igw.api.service.IService;
import openmods.igw.impl.client.MismatchingVersionsGui;
import openmods.igw.impl.client.WarningGui;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings({"MethodCallSideOnly", "NewExpressionSideOnly"})
public final class GuiService implements IGuiService {

	private static boolean instanceCreated = false;

	GuiService() {
		if (instanceCreated) throw new RuntimeException(new IllegalStateException("Instance already created"));
		instanceCreated = true;
	}

	@Override
	public boolean shouldShow(@Nonnull final GUIs gui) {
		switch (gui) {
			case WARNING:
				return WarningGui.shallShow();
			case MISMATCHING_MODS:
				return MismatchingVersionsGui.shouldShow();
			default:
				throw new IllegalArgumentException("gui");
		}
	}

	@Override
	public void show(@Nonnull final GUIs gui) {
		switch (gui) {
			case WARNING:
				WarningGui.markShow();
				return;
			case MISMATCHING_MODS:
				MismatchingVersionsGui.show();
				return;
			default:
				throw new IllegalArgumentException("gui");
		}
	}

	@Nonnull
	@Override
	@SuppressWarnings({"ConstantConditions", "unchecked"})
	public <T> T construct(@Nonnull final GUIs gui, @Nullable final Object... parameters) {
		switch (gui) {
			case WARNING:
				return (T) new WarningGui();
			case MISMATCHING_MODS:
				return (T) new MismatchingVersionsGui((List<IMismatchingModEntry>) parameters[0]);
			default:
				throw new IllegalArgumentException("gui");
		}
	}

	@Nonnull
	@Override
	public IGuiService cast() {
		return this;
	}

	@Override
	public void onRegisterPre(@Nullable final IService<IGuiService> previous) {}

	@Override
	public void onRegisterPost() {}

	@Override
	public void onUnregister() {}
}
