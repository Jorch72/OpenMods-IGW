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
