package openmods.igw.api.record.mod;

import javax.annotation.Nonnull;

/**
 * Represents an object containing all the necessary information
 * used to identify a specific mod in the current OpenMods-IGW
 * environment.
 *
 * <p>The caching of all instances of the implementations of this
 * interface must be avoided, because the entry may change during
 * its lifetime.</p>
 *
 * @since 1.0
 */
public interface IMutableModEntry extends IModEntry {

    /**
     * Sets the mod id of the current entry.
     *
     * @param modId
     *         The new mod id.
     *
     * @since 1.0
     */
    void modId(@Nonnull final String modId);

    /**
     * Sets the version of the current entry.
     *
     * @param version
     *         The new version.
     *
     * @since 1.0
     */
    void version(@Nonnull final String version);
}
