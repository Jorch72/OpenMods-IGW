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
package openmods.igw.prefab.record.mod;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.record.Cached;
import openmods.igw.api.record.mod.IModEntry;
import openmods.igw.api.record.mod.IMutableModEntry;

import java.util.Map;
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
@Cached
public final class MutableModEntry implements IMutableModEntry {

    private static final Map<String, MutableModEntry> CACHE = Maps.newLinkedHashMap();

    private String modId;
    private String version;

    private MutableModEntry(@Nonnull final String modId, @Nonnull final String version) {
        this.modId(modId);
        this.version(version);
    }

    /**
     * Gets a new instance of this mod object or an existing one if
     * it has already been created.
     *
     * @param modId
     *         The mod id.
     * @param version
     *         The mod version.
     * @return
     *         A MutableModEntry object.
     * @throws NullPointerException
     *         If either the mod id or the version is {@code null}.
     * @throws IllegalArgumentException
     *         If the mod id is an empty string.
     *
     * @since 1.0
     */
    public static MutableModEntry of(@Nonnull final String modId, @Nonnull final String version) {
        Preconditions.checkNotNull(modId, "Mod ID must not be null");
        Preconditions.checkNotNull(version, "Version must not be null");
        Preconditions.checkArgument(!modId.isEmpty(), "Mod ID must not be an empty string");

        if (CACHE.containsKey(modId)) return CACHE.get(modId);

        final MutableModEntry entry = new MutableModEntry(modId, version);
        CACHE.put(modId, entry);
        return entry;
    }

    @Override
    public void modId(@Nonnull final String modId) {
        Preconditions.checkArgument(!modId.isEmpty(), "Mod ID must not be an empty string");
        this.modId = modId;
    }

    @Override
    public void version(@Nonnull final String version) {
        this.version = version;
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
            OpenModsIGWApi.get().log().warning("Attempted to get mod container for an unavailable mod.");
            return Optional.absent();
        }

        for (final ModContainer container : Loader.instance().getModList()) {
            if (!container.getModId().equals(this.modId())) continue;
            if (!container.getVersion().equals(this.version())) {
                OpenModsIGWApi.get().log().warning("Found container for mod %s, but version does not match (%s instead of %s). Returning anyway",
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
        return Optional.of((IModEntry) ModEntry.of(modId(), version()));
    }

    @Nonnull
    @Override
    public Optional<IMutableModEntry> toMutable() {
        return Optional.of((IMutableModEntry) this);
    }
}
