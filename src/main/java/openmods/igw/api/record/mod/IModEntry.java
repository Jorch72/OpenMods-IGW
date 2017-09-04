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
