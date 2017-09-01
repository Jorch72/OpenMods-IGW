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
