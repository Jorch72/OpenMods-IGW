package openmods.igw.prefab.record.mod;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import cpw.mods.fml.common.ModContainer;

import openmods.igw.api.record.Immutable;
import openmods.igw.api.record.mod.IMismatchingModEntry;
import openmods.igw.api.record.mod.IModEntry;
import openmods.igw.api.record.mod.IMutableModEntry;

import javax.annotation.Nonnull;

/**
 * Represents a mismatching mod entry, used to hold all the
 * information the GUI needs to show.
 *
 * @since 1.0
 */
// TODO Cache
@Immutable
public final class MismatchingModEntry implements IMismatchingModEntry {

	private final IModEntry mod;
	private final String installedVersion;

	public MismatchingModEntry(@Nonnull final IModEntry mod, @Nonnull final String installedVersion) {
		this.mod = Preconditions.checkNotNull(mod);
		this.installedVersion = Preconditions.checkNotNull(installedVersion);
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
