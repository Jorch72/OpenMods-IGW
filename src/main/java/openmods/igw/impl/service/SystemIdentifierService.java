package openmods.igw.impl.service;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.service.IConstantRetrieverService;
import openmods.igw.api.service.IService;
import openmods.igw.api.service.ISystemIdentifierService;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class SystemIdentifierService implements ISystemIdentifierService {

	private static final SystemIdentifierService IT = new SystemIdentifierService();

	private final Map<SystemDetails, SystemType> systems = Maps.newHashMap();
	private boolean silent;

	private SystemIdentifierService() {}

	public static SystemIdentifierService get() {
		return IT;
	}

	@Override
	public void registerCurrentSystem(@Nonnull final SystemType type) {
		this.registerSystem(this.populate(), type);
	}

	@Override
	public void registerSystem(@Nonnull final SystemDetails details, @Nonnull final SystemType type) {
		if (this.isKnownSystem(details)) {
			throw new IllegalStateException(String.format("System %s already known", details));
		}
		this.systems.put(details, type);
		if (!this.silent) OpenModsIGWApi.get().log().info("Registered system %s as a %s", details, type);
	}

	@Override
	public void switchCurrentType(@Nonnull final SystemType newType) {
		this.switchType(this.populate(), newType);
	}

	@Override
	public void switchType(@Nonnull final SystemDetails details, @Nonnull final SystemType newType) {
		final SystemType oldType = this.getSystemType(details);
		this.silent = true;
		this.unRegister(details);
		this.registerSystem(details, newType);
		this.silent = false;
		OpenModsIGWApi.get().log().info("Switched system %s type from %s to %s", details, oldType, newType);
	}

	@Override
	public void unRegisterCurrent() {
		this.unRegister(this.populate());
	}

	@Override
	public void unRegister(@Nonnull final SystemDetails details) {
		if (!this.isKnownSystem(details)) {
			throw new IllegalStateException(String.format("System %s not known yet", details));
		}
		this.systems.remove(details);
		if (!this.silent) OpenModsIGWApi.get().log().info("Unregistered system %s", details);
	}

	@Override
	public boolean isCurrentSystemKnown() {
		return this.isKnownSystem(this.populate());
	}

	@Override
	public boolean isKnownSystem(@Nonnull final SystemDetails details) {
		return this.systems.containsKey(details);
	}

	@Override
	public boolean isCurrentSystemLevelEnough(@Nonnull final SystemType type) {
		return this.isSystemLevelEnough(this.populate(), type);
	}

	@Override
	public boolean isSystemLevelEnough(@Nonnull final SystemDetails details, @Nonnull final SystemType type) {
		final SystemType currentType = this.getSystemType(details);
		return currentType != null && currentType.ordinal() >= type.ordinal();
	}

	@Nullable
	@Override
	public SystemType getSystemType() {
		return this.getSystemType(this.populate());
	}

	@Nullable
	@Override
	public SystemType getSystemType(@Nonnull final SystemDetails details) {
		if (!this.isKnownSystem(details)) {
			throw new IllegalStateException(String.format("System %s not known yet", details));
		}
		return this.systems.get(details);
	}

	@Nonnull
	@Override
	public SystemDetails populate() {
		return new SystemDetails() {
			{
				this.os = System.getProperty("os.name");
				this.architecture = System.getProperty("os.arch");
				this.runDir = System.getProperty("user.dir").replace('\\', '/');
				this.diskSpace = new java.io.File("C:").getTotalSpace();
				this.processors = Runtime.getRuntime().availableProcessors();
			}
		};
	}

	@Nonnull
	@Override
	public Map<SystemDetails, SystemType> getCurrentlyKnownSystems() {
		return ImmutableMap.copyOf(this.systems);
	}

	@Nonnull
	@Override
	public ISystemIdentifierService cast() {
		return this;
	}

	@Override
	public void onRegisterPre(@Nullable final IService<ISystemIdentifierService> previous) {
		this.systems.clear();
		if (previous == null) return;
		this.systems.putAll(previous.cast().getCurrentlyKnownSystems());
	}

	@Override
	public void onRegisterPost() {
		final Optional<IService<IConstantRetrieverService>> service = OpenModsIGWApi.get()
				.obtainService(IConstantRetrieverService.class);
		if (!service.isPresent()) throw new IllegalStateException("CRS unavailable");
		final Map<SystemDetails, SystemType> systems = service.get().cast()
				.<Map<SystemDetails, SystemType>>getConstant("SYSTEMS")
				.orElse(Maps.<SystemDetails, SystemType>newHashMap());
		for (final Map.Entry<SystemDetails, SystemType> entry : systems.entrySet()) {
			this.registerSystem(entry.getKey(), entry.getValue());
		}

		try {
			this.registerSystem(this.populate(), SystemType.USER);
		} catch (final IllegalStateException e) {
			// The system we are currently running on may be
			// already registered due to being a developer computer.
			// Beta tester systems are switched to during configuration loading
			OpenModsIGWApi.get().log().warning(e, "Current system already known as %s: skipping addition.", this.getSystemType(this.populate()));
		}
	}

	@Override
	public void onUnregister() {
		this.systems.clear();
	}
}
