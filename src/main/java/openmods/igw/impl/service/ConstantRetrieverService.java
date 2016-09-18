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
			return new ConfigConstantWrapper<T>(null);
		}
	}

	@Nonnull
	@Override
	public ConfigConstantWrapper<Boolean> getBooleanConfigConstant(@Nonnull final String name) {
		try {
			return this.getConfigConstant(name);
		} catch (final ClassCastException e) {
			return new ConfigConstantWrapper<Boolean>(null);
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
