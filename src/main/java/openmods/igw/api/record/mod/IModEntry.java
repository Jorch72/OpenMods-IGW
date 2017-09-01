package openmods.igw.api.record.mod;

import com.google.common.base.Optional;

import net.minecraftforge.fml.common.ModContainer;
import openmods.igw.api.record.IRecord;

import javax.annotation.Nonnull;

/**
 * Represents an object containing all the necessary information
 * used to identify a specific mod in the current OpenMods-IGW
 * environment.
 *
 * <p>Every implementation of this class must be completely
 * immutable. Mutable implementations must extend {@link IMutableModEntry}.</p>
 *
 * @since 1.0
 */
public interface IModEntry extends IRecord<IModEntry, IMutableModEntry> {

    /**
     * Gets the mod id associated with this mod entry.
     *
     * @return
     *         The mod id.
     *
     * @since 1.0
     */
    @Nonnull
    String modId();

    /**
     * Gets the mod version associated with this mod entry.
     *
     * @return
     *         The mod version.
     *
     * @since 1.0
     */
    @Nonnull
    String version();

    /**
     * Used to obtain the mod container instance for this specific mod.
     *
     * @return
     *         An {@link Optional} containing either a mod container or an
     *         {@link Optional#absent() absent} value if no container was
     *         found or if the mod was unavailable.
     *
     * @since 1.0
     */
    @Nonnull
    Optional<ModContainer> modContainer();
}
