package openmods.igw.api;

import openmods.igw.api.cache.IgnoreCache;
import openmods.igw.api.init.IInit;
import openmods.igw.api.proxy.IInitProxy;
import openmods.igw.api.service.ServiceManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Entry point for every OpenModsIGW API interaction.
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public final class OpenModsIGWApi {

	private static final OpenModsIGWApi SINGLETON = new OpenModsIGWApi();
	private static final Method LOG;

	// TODO Cache classes, methods and fields
	private final Map<String, Object> constantsCache;
	private final Map<String, Object> configCache;

	static {
		try {
			LOG = Class.forName("openmods.Log")
					.getDeclaredMethod("severe", Throwable.class, String.class, Object[].class);
		} catch (final Throwable t) {
			throw new RuntimeException(t.getMessage(), t);
		}

		try {
			final Method m = Class.forName("openmods.igw.impl.service.OpenServiceProvider")
					.getDeclaredMethod("initializeServices");
			m.setAccessible(true);
			m.invoke(null);
			m.setAccessible(false);
		} catch (final Throwable throwable) {
			// Do not crash because services aren't actually needed
			// when interfacing with the API. Only to test the
			// full features (and that already comes with a
			// nasty NullPointerException, so...)

			// Log the thing anyway, just in case some madman
			// blames us for crashing when it is not our
			// fault
			log("Unable to find default service implementation.", throwable);
		}
	}

	private OpenModsIGWApi() {
		// Not using Guava because this is a stand-alone API
		this.constantsCache = new HashMap<String, Object>();
		this.configCache = new HashMap<String, Object>();
	}

	/**
	 * Gets the unique instance of the API class.
	 *
	 * @return
	 * 		The unique instance of the API class.
	 *
	 * @since 1.0
	 */
	@Nonnull
	public static OpenModsIGWApi get() {
		return SINGLETON;
	}

	/**
	 * Gets the service manager singleton, used to maintain
	 * all the various services of the API.
	 *
	 * @return
	 * 		The service manager.
	 *
	 * @since 1.0
	 */
	@Nonnull
	public ServiceManager serviceManager() {
		return ServiceManager.IT;
	}

	/* *
	 * Gets the mod's main class.
	 *
	 * @return
	 * 		The mod's main class.
	 *
	 * @since 1.0
	 */
/*
	@Nonnull //FIXME REMOVE
	public IInit modMainClass() {
		return this.findClass("openmods.igw.OpenModsIGW", IInit.class);
	}

*/
	/* *
	 * Gets the current proxy instance of the mod.
	 *
	 * @return
	 * 		The current proxy instance of the mod.
	 *
	 * @since 1.0
	 */
/*
	@Nullable //FIXME REMOVE
	public IInitProxy proxy() {
		final Method method = this.findMethod(this.modMainClass().getClass(), "proxy");
		return this.invokeMethod(method, IInitProxy.class, null);
	}

*/
	/* *
	 * Gets a constant stored in the "Constants" class.
	 *
	 * @param constantName
	 * 		The name of the constant. It is case-insensitive.
	 * @param <T>
	 *     	The constant's type.
	 * @return
	 * 		The specified constant if available or {@code null} if not found.
	 *
	 * @since 1.0
	 */
/*
	@Nullable
	@SuppressWarnings("unchecked") //FIXME REMOVE
	public <T> T constant(@Nonnull final String constantName) {
		if (this.constantsCache.containsKey(constantName)) return (T) this.constantsCache.get(constantName); // Trust

		final Class<?> constantClass = this.findClass("openmods.igw.impl.utils.Constants", Object.class).getClass();

		for (final Field field : constantClass.getFields()) {
			if (!field.getName().toUpperCase(Locale.ENGLISH).equals(constantName.toUpperCase(Locale.ENGLISH))) continue;
			if (!Modifier.isStatic(field.getModifiers())) continue;
			field.setAccessible(true); // No security concerns: they have to be accessible
			T tmp; // Non-final because of strange reasons
			try {
				tmp = (T) field.get(null); // We need to trust the caller
			} catch (final IllegalAccessException e) {
				tmp = null;
			}
			if (tmp != null) this.constantsCache.put(constantName, tmp);
			return tmp;
		}

		log("Invalid constant definition: %s",
				new ClassNotFoundException("Actually field not found, but whatever..."),
				constantName);
		return null;
	}

*/
	/* *
	 * Translates the specified string, prepending the default mod id.
	 *
	 * @param translationId
	 * 		The id of the translation.
	 * @return
	 * 		The translated string or {@code null} if the method throws an error.
	 *
	 * @since 1.0
	 */
/*
	@Nullable //FIXME REMOVE
	public String translate(@Nonnull final String translationId) {
		final Class<?> clazz = this.findClassNoInit("openmods.igw.impl.service.TranslationService");
		final Method method = this.findMethod(clazz, "translate", String.class);
		return this.invokeMethod(method, String.class, null, translationId);
	}

*/
	/* *
	 * Translates the specified string, prepending the specified mod id.
	 *
	 * @param id
	 * 		The mod id to prepend.
	 * @param translationId
	 * 		The id of the translation.
	 * @return
	 * 		The translated string or {@code null} if the method throws an error.
	 *
	 * @since 1.0
	 */
/*
	@Nullable //FIXME REMOVE
	public String translate(@Nonnull final String id, @Nonnull final String translationId) {
		final Class<?> clazz = this.findClassNoInit("openmods.igw.impl.service.TranslationService");
		final Method method = this.findMethod(clazz, "translate", String.class, String.class);
		return this.invokeMethod(method, String.class, null, id, translationId);
	}

*/
	/* *
	 * Gets a config value stored in the default config class.
	 *
	 * @param id
	 * 		The config's id (the field name).
	 * @param def
	 * 		The default value of the field.
	 * @param <T>
	 *     	The config's type.
	 * @return
	 * 		The specified config if available, else returns default.
	 *
	 * @since 1.0
	 */
/*
	@Nonnull //FIXME REMOVE
	public <T> T configValueNonNull(@Nonnull final String id, @Nonnull final T def) {
		final T config = this.configValue(id);
		return config == null? def : config;
	}

*/
	/* *
	 * Gets a config value stored in the default config class.
	 *
	 * @param id
	 * 		The config's id (the field name).
	 * @param <T>
	 *     The config's type.
	 * @return
	 * 		The specified config if available, else {@code null}.
	 *
	 * @since 1.0
	 */
/*
	@Nullable
	@SuppressWarnings("unchecked") //FIXME REMOVE
	public <T> T configValue(@Nonnull final String id) {
		if (this.configCache.containsKey(id)) return (T) this.configCache.get(id); // Trust me, ok?

		final Class<?> clazz = this.findClassNoInit("openmods.igw.impl.config.Config");

		for (final Field field : clazz.getFields()) {
			if (!field.getName().toUpperCase(Locale.ENGLISH).equals(id.toUpperCase(Locale.ENGLISH))) continue;
			if (!Modifier.isStatic(field.getModifiers())) continue;
			field.setAccessible(true); // No security concerns: they have to be accessible
			T tmp; // Non-final because of strange reasons
			try {
				tmp = (T) field.get(null); // We need to trust the caller
			} catch (final IllegalAccessException e) {
				tmp = null;
			}
			if (tmp != null && field.getAnnotation(IgnoreCache.class) == null) this.configCache.put(id, tmp);
			return tmp;
		}

		log("Invalid config value definition: %s",
				new ClassNotFoundException("Actually field not found, but whatever..."),
				id);
		return null;
	}



	@Nonnull //FIXME Remove?
	private <T> T findClass(@Nonnull final String className, @Nonnull final Class<T> castTo) {
		try {
			final Class<?> clazz = Class.forName(className);

			for (final Field field : clazz.getDeclaredFields()) {
				if (field.getName().toUpperCase(Locale.ENGLISH).equals("SINGLETON") ||
						field.getName().toUpperCase(Locale.ENGLISH).equals("INSTANCE")) {
					if (!Modifier.isStatic(field.getModifiers())) continue;
					final boolean accessible = Modifier.isPublic(field.getModifiers());
					if (!accessible) field.setAccessible(true);
					final Object content = field.get(null);
					if (!accessible) field.setAccessible(false);
					return castTo.cast(content);
				}
			}

			final Constructor<?> constructor = clazz.getConstructor();
			final Object instance = constructor.newInstance();
			return castTo.cast(instance);
		} catch (final ClassNotFoundException e) {
			log("Class %s not found in current environment", e, className);
		} catch (final IllegalAccessException e) {
			log("Attempted to access to inaccessible field in %s", e, className);
		} catch (final ClassCastException e) {
			log("Class %s cannot be cast to supplied type %s", e, className, castTo.getName());
		} catch (final NoSuchMethodException e) {
			log("Class %s does not have a parameter-less constructor", e, className);
		} catch (final InstantiationException e) {
			log("Unable to instantiate class %s", e, className);
		} catch (final InvocationTargetException e) {
			log("The constructor of the class %s has thrown an exception", e, className);
		}

		throw new RuntimeException("Unable to find class and/or construct an instance");
	}

	@Nonnull //FIXME Remove?
	private Class<?> findClassNoInit(@Nonnull final String className) {
		try {
			return Class.forName(className);
		} catch (final ClassNotFoundException e) {
			log("Class %s not found in current environment", e, className);
		}

		throw new RuntimeException("Unable to find class");
	}

	@Nonnull //FIXME Remove?
	private Method findMethod(@Nonnull final Class<?> clazz,
							  @Nonnull final String name,
							  @Nullable final Class<?>... parameters) {
		try {
			final Method method =  clazz.getDeclaredMethod(name, parameters);
			if (!Modifier.isPublic(method.getModifiers())) method.setAccessible(true);
			return method;
		} catch (final NoSuchMethodException e) {
			log("Unable to find specified method %s(%s) in class %s", e, name,
					Arrays.toString(parameters), clazz.getName());
		}

		throw new RuntimeException("Unable to find method");
	}

	@Nullable //FIXME Remove?
	private <T> T invokeMethod(@Nonnull Method method,
							   @Nonnull Class<T> castTo,
							   @Nullable Object classInstance,
							   @Nullable Object... arguments) {
		try {
			return castTo.cast(method.invoke(classInstance, arguments));
		} catch (final IllegalAccessException e) {
			log("Unable to access specified method %s", e, method.getName());
		} catch (final InvocationTargetException e) {
			log("The invoked method %s has thrown an exception", e, method.getName());
		} catch (final ClassCastException e) {
			log("Method result cannot be cast to %s", e, castTo.getName());
		}

		return null;
	}

	@Nullable //FIXME Remove?
	private <T> T getField(@Nonnull final Field field,
						   @Nonnull final Class<T> castTo,
						   @Nullable final Object classInstance) {
		try {
			return castTo.cast(field.get(classInstance));
		} catch (final IllegalAccessException e) {
			log("Unable to access field %s", e, field.getName());
		} catch (final ClassCastException e) {
			log("Field value cannot be cast to %s", e, castTo.getName());
		}

		return null;
	}
*/
	private static void log(@Nonnull final String message,
							@Nonnull final Throwable cause,
							@Nullable final Object... format) {
		try {
			LOG.invoke(null, cause, message, format);
		} catch (final Throwable t) {
			throw new RuntimeException(t);
		}
	}
}
