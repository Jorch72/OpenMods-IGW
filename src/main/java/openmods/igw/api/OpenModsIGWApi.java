package openmods.igw.api;

import com.google.common.base.Optional;

import com.google.common.base.Preconditions;
import openmods.igw.api.service.ILoggingService;
import openmods.igw.api.service.IService;
import openmods.igw.api.service.ITranslationService;
import openmods.igw.api.service.ServiceManager;

import java.lang.reflect.Method;
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

	static {
		try {
			// noinspection SpellCheckingInspection
			LOG = Class.forName("openmods.Log")
					.getDeclaredMethod("severe", Throwable.class, String.class, Object[].class);
		} catch (final Throwable t) {
			throw new RuntimeException(t.getMessage(), t);
		}

		try {
			// Mainly done just to annoy users and discourage them to use this
			// service: it's only internal! kek

			// noinspection SpellCheckingInspection
			final Class<?> theServiceClass = Class.forName("openmods.igw.prefab.service.fallback.SystemLoggingService");
			final Object service = theServiceClass.getDeclaredMethod("getTheLoggingService").invoke(null);
			final Method register = theServiceClass.getDeclaredMethod("register");
			register.setAccessible(true);
			register.invoke(service);
			register.setAccessible(false);
		} catch (final Throwable thr) {
			// Log that default logging service could not be
			// loaded (wow, that's so weird to type), so nobody
			// blames us.
			// And why would they, anyway?
			log("Unable to load default logging service. This may lead to future errors.", thr);

			if (thr.getCause() instanceof SecurityException) {
				// But, if the ancestor is a security exception, then
				// notify and rethrow.
				log("Ancestor was a SecurityException. Rethrowing!", new Throwable());
				throw new RuntimeException(thr);
			}
		}

		try {
			// noinspection SpellCheckingInspection
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

	private OpenModsIGWApi() {}

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

	/**
	 * Gets an {@link Optional} containing the service registered
	 * for the specified class, if available.
	 *
	 * <p>This is an helper method, which can be used as a quicker way
	 * to access the one in {@link ServiceManager}. Calling this
	 * method or {@link ServiceManager#obtainService(Class)} yields
	 * the exact same result.</p>
	 *
	 * @param clazz
	 * 		The class the service implementation is for.
	 * @param <T>
	 *     	The type of the service implementation. Refer to
	 *     	{@link IService} javadoc for more information.
	 * @return
	 * 		An {@link Optional} containing the service, if available
	 * 		or {@link Optional#absent()} if none is found.
	 *
	 * @since 1.0
	 */
	@Nonnull
	public <T> Optional<IService<T>> obtainService(@Nonnull final Class<? extends IService<T>> clazz) {
		return this.serviceManager().obtainService(clazz);
	}

	/**
	 * Attempts to translate the given translation ID.
	 *
	 * <p>This method should add to the translation ID a default
	 * mod ID, either visible to the end-user or only available
	 * internally.</p>
	 *
	 * <p>This is a wrapper method, which can be used to translate
	 * a sentence in a quicker way. Using this method or the one in
	 * {@link ITranslationService}
	 * ({@link ITranslationService#translate(String)}) is exactly
	 * the same.</p>
	 *
	 * @param translationId
	 * 		The ID of the translation.
	 * @return
	 * 		A String containing the translated keyword or the given
	 * 		{@code translationId} if none is available.
	 *
	 * @since 1.0
	 */
	@Nonnull
	public String translate(@Nonnull final String translationId) {
		final Optional<IService<ITranslationService>> optionalService = this.obtainService(ITranslationService.class);
		if (!optionalService.isPresent()) {
			throw new RuntimeException(new IllegalStateException("Translation service unavailable"));
		}
		final ITranslationService service = optionalService.get().cast();
		return service.translate(translationId);
	}

	/**
	 * Gets the current logging service implementation.
	 *
	 * <p>Since logging is extensively used in an application, this
	 * method is provided here for convenience. Using the {@link
	 * ServiceManager} yields the exact same result.</p>
	 *
	 * @return
	 * 		The current logging service implementation.
	 * @see ILoggingService
	 *
	 * @since 1.0
	 */
	@Nonnull
	public ILoggingService log() {
		return Preconditions.checkNotNull(this.serviceManager().obtainAndCastService(ILoggingService.class));
	}

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
