package openmods.igw.api.handler;

import javax.annotation.Nonnull;

/**
 * Represents the basic event handler used by OpenMods-IGW.
 *
 * <p>Every event handler (proxies and main mod class excluded)
 * should implement this interface.</p>
 *
 * @since 1.0
 */
public interface IEventHandler {

    /**
     * Gets the mod id this event handler is for.
     *
     * @return
     *         The mod id.
     *
     * @since 1.0
     */
    @Nonnull
    String modId();

    /**
     * Gets the mod id this event handler is for.
     *
     * @return
     *         The mod id.
     *
     * @deprecated Use {@link #modId()} instead.
     *
     * @since 1.0
     */
    @Deprecated
    @Nonnull
    String getModId();

    /**
     * Gets the name of the class that holds all the various
     * configuration options for the mod specified by
     * {@link #modId()}.
     *
     * @return
     *         The name of the configuration class.
     *
     * @since 1.0
     */
    @Nonnull
    String configClass();

    /**
     * Gets the name of the class that holds all the various
     * configuration options for the mod specified by
     * {@link #modId()}.
     *
     * @return
     *         The name of the configuration class.
     *
     * @deprecated Use {@link #configClass()} instead.
     *
     * @since 1.0
     */
    @Deprecated
    @Nonnull
    String getConfigClass();
}
