/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Open Mods
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package openmods.igw.impl.service;

import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.service.IClassProviderService;
import openmods.igw.api.service.IConstantRetrieverService;
import openmods.igw.api.service.IGuiService;
import openmods.igw.api.service.ILoggingService;
import openmods.igw.api.service.IService;
import openmods.igw.api.service.ISystemIdentifierService;
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
        registerService(IGuiService.class, new GuiService());
        registerService(ILoggingService.class, LoggingService.get());
        registerService(ISystemIdentifierService.class, SystemIdentifierService.get());
        registerService(ITranslationService.class, TranslationService.get());

        log().info("Successfully loaded and registered %d services out of %d total services",
                registeredServices,
                totalServices);
    }

    private static <T> void registerService(@Nonnull final Class<? extends IService<T>> serviceClass,
                                            @Nonnull final IService<T> newService) {
        ++totalServices;
        final String serviceClassName = serviceClass.getName();
        final String newServiceClassName = newService.getClass().getName();
        if (!OpenModsIGWApi.get().serviceManager().registerService(serviceClass, newService)) {
            log().warning("Registration failed for pair %s -> %s. This can lead to errors!",
                    serviceClassName,
                    newServiceClassName);
            log().info("Skipping ID " + totalServices);
        }
        ++registeredServices;
        log().info("Registered implementation class %s for service %s (id: %d)", newServiceClassName,
                serviceClassName,
                totalServices);
    }

    @Nonnull
    private static ILoggingService log() {
        return OpenModsIGWApi.get().log();
    }
}
