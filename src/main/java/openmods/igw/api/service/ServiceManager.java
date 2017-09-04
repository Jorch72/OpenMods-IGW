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
package openmods.igw.api.service;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An enum-based singleton, representing the manager for every
 * service implementation provided by this class.
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public enum ServiceManager {

    /**
     * The singleton instance.
     *
     * @since 1.0
     */
    IT;

    private final Map<Class<? extends IService<?>>, IService<?>> services;

    ServiceManager() {
        this.services = Maps.newHashMap();
    }

    /**
     * Registers a new service implementation for the specified class.
     *
     * @param serviceClass
     *         The service class the new implementation should be supplied for.
     * @param newService
     *         The new service implementation.
     * @param <T>
     *         The type of the new service implementation. Refer to
     *         {@link IService} javadoc for more information.
     * @return
     *         If the registration was successful.
     *
     * @since 1.0
     */
    public <T> boolean registerService(@Nonnull final Class<? extends IService<T>> serviceClass,
                                       @Nonnull final IService<T> newService) {
        @SuppressWarnings("unchecked") final IService<T> previous = (IService<T>) this.services.get(serviceClass);
        newService.onRegisterPre(previous);
        this.services.put(serviceClass, null);
        if (previous != null) previous.onUnregister();
        this.services.put(serviceClass, newService);
        newService.onRegisterPost();
        return this.services.get(serviceClass).equals(newService);
    }

    /**
     * Gets an {@link Optional} containing the service registered
     * for the specified class, if available.
     *
     * @param clazz
     *         The class the service implementation is for.
     * @param <T>
     *         The type of the service implementation. Refer to
     *         {@link IService} javadoc for more information.
     * @return
     *         An {@link Optional} containing the service, if available
     *         or {@link Optional#absent()} if none is found.
     *
     * @since 1.0
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public <T> Optional<IService<T>> obtainService(@Nonnull final Class<? extends IService<T>> clazz) {
        return Optional.fromNullable((IService<T>) this.services.get(clazz));
    }

    /**
     * Gets the service for the specified class, already unwrapped
     * and cast to the needed interface type.
     *
     * @param clazz
     *         The class the service implementation is for.
     * @param <T>
     *        The type of the service implementation. Refer to
     *         {@link IService} javadoc for more information.
     * @return
     *         The service implementation, if available. {@code null}
     *         otherwise.
     *
     * @since 1.0
     */
    @Nullable
    public <T> T obtainAndCastService(@Nonnull final Class<? extends IService<T>> clazz) {
        final Optional<IService<T>> optionalService = this.obtainService(clazz);
        if (!optionalService.isPresent()) return null;
        return optionalService.get().cast();
    }
}
