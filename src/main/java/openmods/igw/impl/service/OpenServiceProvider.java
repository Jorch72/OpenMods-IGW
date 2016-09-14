package openmods.igw.impl.service;

import openmods.Log;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.service.IClassProviderService;
import openmods.igw.api.service.IConstantRetrieverService;
import openmods.igw.api.service.IService;
import openmods.igw.api.service.ITranslationService;

import javax.annotation.Nonnull;

/**
 * Utility class used by OpenMods-IGW to initialize the various
 * needed services in the API.
 *
 * <p>Methods in here are called reflectively by the API
 * automatically, so you should not attempt to initialize
 * this class yourself</p>
 */
@SuppressWarnings("unused")
public final class OpenServiceProvider {

	private static int registeredServices;
	private static int totalServices;

	private OpenServiceProvider() {}

	private static void initializeServices() {
		registerService(IClassProviderService.class, new ClassProviderService());
		registerService(IConstantRetrieverService.class, ConstantRetrieverService.get());
		registerService(ITranslationService.class, TranslationService.get());

		Log.info("Successfully loaded and registered %d services out of %d total services",
				registeredServices,
				totalServices);
	}

	private static <T> void registerService(@Nonnull final Class<? extends IService<T>> serviceClass,
											@Nonnull final IService<T> newService) {
		++totalServices;
		final String serviceClassName = serviceClass.getName();
		final String newServiceClassName = newService.getClass().getName();
		if (!OpenModsIGWApi.get().serviceManager().registerService(serviceClass, newService)) {
			Log.warn("Registration failed for pair %s -> %s. This can lead to errors!",
					serviceClassName,
					newServiceClassName);
		}
		++registeredServices;
		Log.info("Registered implementation class %s for service %s (id: %d)", newServiceClassName,
				serviceClassName,
				totalServices);
	}
}
