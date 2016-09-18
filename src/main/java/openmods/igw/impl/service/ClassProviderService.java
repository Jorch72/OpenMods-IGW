package openmods.igw.impl.service;

import openmods.igw.OpenModsIGW;
import openmods.igw.api.config.IConfig;
import openmods.igw.api.init.IInit;
import openmods.igw.api.proxy.IInitProxy;
import openmods.igw.api.service.IClassProviderService;
import openmods.igw.api.service.IService;
import openmods.igw.impl.config.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public final class ClassProviderService implements IClassProviderService {

	private static boolean instanceCreated = false;

	ClassProviderService() {
		if (instanceCreated) throw new RuntimeException(new IllegalStateException("Instance already created"));
		instanceCreated = true;
	}

	@Nonnull
	@Override
	public Object mainClass() {
		return OpenModsIGW.instance;
	}

	@Nonnull
	@Override
	public IInit mainClassAsInit() {
		return (IInit) this.mainClass();
	}

	@Nullable
	@Override
	public IInitProxy proxy() {
		return OpenModsIGW.proxy();
	}

	@Nonnull
	@Override
	public IClassProviderService cast() {
		return this;
	}

	@Nonnull
	@Override
	public Class<? extends IConfig> config() {
		return Config.class;
	}

	@Override
	public void onRegisterPre(@Nullable final IService<IClassProviderService> previous) {}

	@Override
	public void onRegisterPost() {}

	@Override
	public void onUnregister() {}
}
