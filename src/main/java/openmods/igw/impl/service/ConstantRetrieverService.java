package openmods.igw.impl.service;

import openmods.igw.api.service.IConstantRetrieverService;
import openmods.igw.api.service.IService;
import openmods.igw.impl.config.Config;
import openmods.igw.impl.utils.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ConstantRetrieverService implements IConstantRetrieverService {

	private static final ConstantRetrieverService IT = new ConstantRetrieverService();

	private ConstantRetrieverService() {}

	@Nonnull
	public static ConstantRetrieverService get() {
		return IT;
	}

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	public <T> ConfigConstantWrapper<T> getConfigConstant(@Nonnull final String name) {
		try {
			final Class<?> clazz = Config.class;
			for (final Field field : clazz.getDeclaredFields()) {
				if (!Modifier.isPublic(field.getModifiers())) continue;
				if (!Modifier.isStatic(field.getModifiers())) continue;
				if (field.getName().equals(name)) {
					try {
						return new ConfigConstantWrapper<T>((T) field.get(null));
					} catch (final Exception ignored) {
						// Try with another one
					}
				}
			}
			throw new NoSuchFieldException(name);
		} catch (final Exception e) {
			openmods.Log.warn(e, "Exception thrown while attempting to retrieve configuration constant " + name);
			return new ConfigConstantWrapper<T>(null);
		}
	}

	@Nonnull
	@Override
	public ConfigConstantWrapper<Boolean> getBooleanConfigConstant(@Nonnull final String name) {
		try {
			final ConfigConstantWrapper<?> it = this.getConfigConstant(name);
			if (!it.isPresent()) throw new IllegalArgumentException("Specified value not found in config file");
			if (!(it.get() instanceof Boolean)) {
				throw new IllegalArgumentException(
						"Specified value is not a valid boolean config property",
						new ClassCastException(String.format("Cannot cast %s to Boolean", it.get().getClass().getName()))
				);
			}
			return new ConfigConstantWrapper<Boolean>((Boolean) it.get());
		} catch (final IllegalArgumentException e) {
			throw new RuntimeException(
					"Unexpected exception in ConstantRetrieverService",
					new IllegalStateException("Invalid service state: invalid argument passed in", e)
			);
		}
	}

	@Override
	public boolean isEnabled(@Nonnull final String modId) {
		return Config.isEnabled(modId);
	}

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	public <T> ConfigConstantWrapper<T> getConstant(@Nonnull String name) {
		try {
			final Class<?> clazz = Constants.class;
			for (final Field field : clazz.getDeclaredFields()) {
				if (!Modifier.isPublic(field.getModifiers())) continue;
				if (!Modifier.isStatic(field.getModifiers())) continue;
				if (!Modifier.isFinal(field.getModifiers())) continue;
				if (field.getName().equals(name)) {
					try {
						return new ConfigConstantWrapper<T>((T) field.get(null));
					} catch (final Exception ignored) {
						// Try with another one
					}
				}
			}
			throw new NoSuchFieldException(name);
		} catch (final Exception e) {
			openmods.Log.warn(e, "Exception thrown while attempting to retrieve constant " + name);
			return new ConfigConstantWrapper<T>(null);
		}
	}

	@Nonnull
	@Override
	public IConstantRetrieverService cast() {
		return this;
	}

	@Override
	public void onRegisterPre(@Nullable final IService<IConstantRetrieverService> previous) {}

	@Override
	public void onRegisterPost() {}

	@Override
	public void onUnregister() {}
}
