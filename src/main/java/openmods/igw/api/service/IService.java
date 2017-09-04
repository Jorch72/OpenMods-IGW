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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Marker interface used to mark services.
 *
 * @param <T>
 *     The service's type. It must be an interface type identifying
 *     the specific service implementation target (e.g., translation).
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public interface IService<T> {

    /**
     * Casts the class to the specified type.
     *
     * <p>By default, the implementation should be like</p>
     * <pre>
     *     public T cast() {
     *         return (T) this;
     *     }
     * </pre>
     *
     * @return
     *         The same instance, casted to the specified type.
     *
     * @since 1.0
     */
    @Nonnull
    T cast();

    /**
     * Called when the service is about to be registered.
     *
     * <p>This can be useful for services to obtain information
     * from the previous registered service implementation,
     * if available.</p>
     *
     * <p>It is guaranteed that this method will be called, in
     * any circumstances and before {@link #onRegisterPost()}.</p>
     *
     * @param previous
     *         The previous implementation, if available.
     *
     * @since 1.0
     */
    void onRegisterPre(@Nullable final IService<T> previous);

    /**
     * Called after the service implementation is registered.
     *
     * <p>You should perform various late initialization tasks,
     * such as grabbing information from the environment here.</p>
     *
     * <p>This method may not be called, in case the previous
     * service fails to unregister or something else happens.
     * When called, it is guaranteed that
     * {@link #onRegisterPre(IService)} has already been called,
     * while {@link #onUnregister()} hasn't.</p>
     *
     * @since 1.0
     */
    void onRegisterPost();

    /**
     * Called when a service implementation has been successfully
     * unregistered from the service manager.
     *
     * <p>You should perform all clean-up tasks in here.</p>
     *
     * <p>This method is called after the new service implementation
     * has run the {@link #onRegisterPre(IService)} logic. This method
     * may not be called if the service can't be unregistered or
     * the new service initialization fails.</p>
     *
     * @since 1.0
     */
    void onUnregister();
}
