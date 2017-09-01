package openmods.igw.prefab.record.mod;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import net.minecraftforge.fml.common.ModContainer;
import openmods.igw.api.record.Cached;
import openmods.igw.api.record.Immutable;
import openmods.igw.api.record.mod.IMismatchingModEntry;
import openmods.igw.api.record.mod.IModEntry;
import openmods.igw.api.record.mod.IMutableModEntry;

import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Represents a mismatching mod entry, used to hold all the
 * information the GUI needs to show.
 *
 * @since 1.0
 */
@Cached
@Immutable
public final class MismatchingModEntry implements IMismatchingModEntry {

    private static final Map<IModEntry, IMismatchingModEntry> CACHE = Maps.newHashMap();

    private final IModEntry mod;
    private final String installedVersion;

    private MismatchingModEntry(@Nonnull final IModEntry mod, @Nonnull final String installedVersion) {
        this.mod = Preconditions.checkNotNull(mod);
        this.installedVersion = Preconditions.checkNotNull(installedVersion);
    }

    /**
     * Gets a new instance of this mismatching mod object or an existing
     * one if it is already available.
     *
     * @param mod
     *         The mod entry of this mod.
     * @param installedVersion
     *         The currently installed version of the specified mod.
     * @return
     *         A {@link MismatchingModEntry} object.
     * @throws NullPointerException
     *         If either argument is {@code null}.
     *
     * @since 1.0
     */
    @Nonnull
    public static IMismatchingModEntry of(@Nonnull final IModEntry mod, @Nonnull final String installedVersion) {
        Preconditions.checkNotNull(mod, "Mod entry must not be null");
        Preconditions.checkNotNull(installedVersion, "Currently installed version must not be null");

        if (CACHE.containsKey(mod)) return CACHE.get(mod);

        final IMismatchingModEntry entry = new MismatchingModEntry(mod, installedVersion);
        CACHE.put(mod, entry);
        return entry;
    }

    /**
     * Gets a new instance of this mismatching mod object or an
     * existing one if it is already available.
     *
     * <p>This method automatically constructs a new {@link IModEntry}
     * instance for internal use, with the given {@code id} and
     * {@code version}. By default, the used implementation is
     * {@link ModEntry}.</p>
     *
     * @param id
     *         The mod's id.
     * @param version
     *         The mod's theoretical version.
     * @param installedVersion
     *         The mod's currently installed version.
     * @return
     *         A new {@link IMismatchingModEntry} object.
     * @throws NullPointerException
     *         If at least one of the arguments is {@code null}.
     * @throws IllegalArgumentException
     *         If the specified {@code id} is an empty string.
     *
     * @see MismatchingModEntry#of(IModEntry, String)
     * @see ModEntry#of(String, String)
     *
     * @since 1.0
     */
    @Nonnull
    public static IMismatchingModEntry of(@Nonnull final String id,
                                          @Nonnull final String version,
                                          @Nonnull final String installedVersion) {
        return of(ModEntry.of(id, version), installedVersion);
    }

    @Nonnull
    @Override
    public String modId() {
        return this.mod().modId();
    }

    @Nonnull
    @Override
    public String version() {
        return this.mod().version();
    }

    @Nonnull
    @Override
    public Optional<ModContainer> modContainer() {
        return this.mod().modContainer();
    }

    @Nonnull
    @Override
    public Optional<IModEntry> toImmutable() {
        return Optional.of((IModEntry) this);
    }

    @Nonnull
    @Override
    public Optional<IMutableModEntry> toMutable() {
        return Optional.absent();
    }

    @Nonnull
    @Override
    public IModEntry mod() {
        return this.mod;
    }

    @Nonnull
    @Override
    public String installedVersion() {
        return this.installedVersion;
    }

    @Override
    public String toString() {
        return "MismatchingModEntry{"
                + this.mod()
                + ", "
                + this.installedVersion()
                + "}";
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || this.getClass() != object.getClass()) return false;
        final MismatchingModEntry that = (MismatchingModEntry) object;
        return Objects.equal(this.mod(), that.mod())
                && Objects.equal(this.installedVersion(), that.installedVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.mod(), this.installedVersion());
    }
}
