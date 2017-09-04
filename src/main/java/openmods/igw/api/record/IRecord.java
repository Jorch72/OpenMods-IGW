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
package openmods.igw.api.record;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;

/**
 * Identifies a record used in the OpenMods-IGW environment.
 *
 * <p>Implementors that do not provide either a mutable or an
 * immutable implementation are free to defer the declaration
 * at a later point or to fill in the blanks with an
 * {@link IRecord}. Declaring {@link Object} is not allowed,
 * though.</p>
 *
 * @param <I>
 *     The immutable type of this record.
 * @param <M>
 *     The mutable type of this record.
 *
 * @since 1.0
 */
public interface IRecord<I, M> {

    /**
     * Gets a new instance of the record as an immutable one.
     *
     * <p>Implementors of this interface should attempt to
     * return a record of their immutable type wrapped in an
     * {@link Optional}, if available. If no implementation
     * is available, implementors should return
     * {@link Optional#absent()}.</p>
     *
     * @return
     *         A new immutable record instance or an empty optional.
     *
     * @since 1.0
     */
    @Nonnull
    Optional<I> toImmutable();

    /**
     * Gets a new instance of the record as a mutable one.
     *
     * <p>Implementors of this interface should attempt to
     * return a record of their mutable type wrapped in an
     * {@link Optional}, if available. If no implementation
     * is available, implementors should return
     * {@link Optional#absent()}.</p>
     *
     * @return
     *         A new mutable record instance or an empty optional.
     *
     * @since 1.0
     */
    @Nonnull
    Optional<M> toMutable();
}
