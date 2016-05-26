package openmods.igw.prefab.record.mod;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

import openmods.Log;
import openmods.igw.api.record.Cached;
import openmods.igw.api.record.Immutable;
import openmods.igw.api.record.mod.IModEntry;
import openmods.igw.api.record.mod.IMutableModEntry;

import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Represents an object containing all the necessary information
 * used to identify a specific mod in the current OpenMods-IGW
 * environment.
 *
 * <p>This class must be considered immutable.</p>
 *
 * @since 1.0
 */
@Cached
@Immutable(toMutable = true)
public final class ModEntry implements IModEntry {

	private static final Map<String, ModEntry> CACHE = Maps.newLinkedHashMap();

	private final String modId;
	private final String version;

	private ModEntry(@Nonnull final String modId, @Nonnull final String version) {
		this.modId = modId;
		this.version = version;
	}

	/**
	 * Gets a new instance of this mod object or an existing one if
	 * it has already been created.
	 *
	 * @param modId
	 * 		The mod id.
	 * @param version
	 * 		The mod version.
	 * @return
	 * 		A ModEntry object.
	 * @throws NullPointerException
	 * 		If either the mod id or the version is {@code null}.
	 * @throws IllegalArgumentException
	 * 		If the mod id is an empty string.
	 *
	 * @since 1.0
	 */
	@Nonnull
	public static ModEntry of(final String modId, final String version) {
		Preconditions.checkNotNull(modId, "Mod ID must not be null");
		Preconditions.checkNotNull(version, "Mod version must not be null");
		Preconditions.checkArgument(!modId.isEmpty(), "Mod ID must not be an empty string");

		if (CACHE.containsKey(modId)) return CACHE.get(modId);

		final ModEntry entry = new ModEntry(modId, version);
		CACHE.put(modId, entry);
		return entry;
	}

	@Nonnull
	@Override
	public String modId() {
		return this.modId;
	}

	@Nonnull
	@Override
	public String version() {
		return this.version;
	}

	@Nonnull
	@Override
	public Optional<ModContainer> modContainer() {
		if (!Loader.isModLoaded(this.modId())) {
			Log.warn("Attempted to get mod container for an unavailable mod.");
			return Optional.absent();
		}

		for (final ModContainer container : Loader.instance().getModList()) {
			if (!container.getModId().equals(this.modId())) continue;
			if (!container.getVersion().equals(this.version())) {
				Log.warn("Found container for mod %s, but version does not match (%s instead of %s). Returning anyway",
						this.modId(),
						container.getVersion(),
						this.version());
			}
			return Optional.of(container);
		}

		return Optional.absent();
	}

	@Nonnull
	@Override
	public Optional<IModEntry> toImmutable() {
		return Optional.of((IModEntry) this);
	}

	@Nonnull
	@Override
	public Optional<IMutableModEntry> toMutable() {
		return Optional.of((IMutableModEntry) MutableModEntry.of(this.modId(), this.version()));
	}

	@Override
	public String toString() {
		return "ModEntry{"
				+ this.modId()
				+ ", "
				+ this.version()
				+ "}";
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) return true;
		if (object == null || this.getClass() != object.getClass()) return false;
		final ModEntry that = (ModEntry) object;
		return Objects.equal(this.modId(), that.modId())
				&& Objects.equal(this.version(), that.version());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.modId(), this.version());
	}
}
