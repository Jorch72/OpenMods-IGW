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
package openmods.igw.api.service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Identifies a service used to retrieve various constants used
 * thorough OpenMods-IGW.
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public interface IConstantRetrieverService extends IService<IConstantRetrieverService> {

    /**
     * Internal class similar to a Guava's
     * {@link com.google.common.base.Optional Optional}, but
     * final and with custom implementation.
     *
     * <p>Internal use only!</p>
     *
     * @param <T>
     *     The type of the constant.
     *
     * @author TheSilkMiner
     * @since 1.0
     */
    final class ConfigConstantWrapper<T> {

        /**
         * Interface used to supply data to a method.
         *
         * <p>The data type may be a factory, an object, an exception
         * or every other possible choice. The contract of this
         * interface does not restrict the possible implementations.</p>
         *
         * <p>This interface is similar to the one provided
         * by Guava ({@link com.google.common.base.Supplier}), but
         * it is specifically designed to not rely on external
         * libraries and for internal use only.</p>
         *
         * @param <T>
         *     The type to return.
         *
         * @author TheSilkMiner
         * @since 1.0
         */
        interface Supplier<T> {

            /**
             * Gets an instance of the appropriate type.
             *
             * @return
             *         An instance of the appropriate type.
             *
             * @since 1.0
             */
            @Nullable
            T get();
        }

        private final T constant;

        public ConfigConstantWrapper(@Nullable final T value) {
            this.constant = value;
        }

        /**
         * Gets the currently stored constant, if available.
         *
         * <p>If not, an {@link IllegalStateException} is thrown.</p>
         *
         * @return
         *         The constant value, if available.
         *
         * @since 1.0
         */
        @Nonnull
        public T get() {
            if (this.constant == null) throw new IllegalStateException("Invalid constant value");
            return this.constant;
        }

        /**
         * Gets the currently stored constant, if available, or the
         * given value.
         *
         * @param other
         *         The default value to return.
         * @return
         *         The constant or the default value.
         *
         * @since 1.0
         */
        @Nonnull
        public T orElse(@Nonnull final T other) {
            try {
                return this.get();
            } catch (final IllegalStateException exception) {
                return other;
            }
        }

        /**
         * Gets the constant value, if available, or {@code null} if not.
         *
         * @return
         *         The constant value or {@code null}
         *
         * @since 1.0
         */
        @Nullable
        public T orNull() {
            try {
                return this.get();
            } catch (final IllegalStateException exception) {
                return null;
            }
        }

        /**
         * Gets the constant value, if available, or else throws
         * an exception.
         *
         * <p>By default, the thrown exception is an
         * {@link IllegalStateException}, wrapped in a
         * {@link RuntimeException}.</p>
         *
         * @return
         *         The constant value, if available.
         * @throws RuntimeException
         *         If the constant's value is not present.
         *
         * @since 1.0
         */
        @Nonnull
        public T orElseThrow() throws RuntimeException {
            // OMG!
            // The amount of hacks used in this method is extreme!
            // This is so awful...
            // TODO Find a better way to implement this........ thing
            try {
                return this.orElseThrow(new Supplier<Throwable>() {
                    @Override
                    public Throwable get() {
                        return new RuntimeException(new IllegalStateException("Value not present"));
                    }
                });
            } catch (final Throwable t) {
                if (t instanceof RuntimeException) throw (RuntimeException) t;
                else throw new RuntimeException("Exception shadowing: " + t.getMessage(), t);
            }
        }

        /**
         * Gets the constant value, if available, or else
         * throws the specified exception.
         *
         * @param throwableSupplier
         *         A {@link Supplier} used to provide the
         *         exception type. You only have to construct
         *         the exception instance: throwing is done
         *         automatically by the method if needed.
         * @return
         *         The constant value, if available.
         * @throws NullPointerException
         *         If the provided supplier is {@code null}.
         * @throws Throwable
         *         If the constant value is not present.
         *
         * @since 1.0
         */
        @Nonnull
        @SuppressWarnings({"ConstantConditions", "WeakerAccess"}) // Can be removed later, if IDE doesn't complain
        public T orElseThrow(@Nonnull final Supplier<? extends Throwable> throwableSupplier) throws Throwable {
            if (!this.isPresent()) throw throwableSupplier.get();
            return this.get();
        }

        /**
         * Gets if the constant is present.
         *
         * @return
         *         If the constant is present.
         *
         * @since 1.0
         */
        @SuppressWarnings("BooleanMethodIsAlwaysInverted") // I have to see if this is applicable or not
        public boolean isPresent() {
            return this.constant != null;
        }
    }

    /**
     * Gets the config constant with the specified name.
     *
     * @param name
     *         The constant's name.
     * @param <T>
     *         The constant's type.
     * @return
     *         A {@link ConfigConstantWrapper wrapper} with the
     *         given constant stored, if available.
     *
     * @since 1.0
     */
    @Nonnull
    <T> ConfigConstantWrapper<T> getConfigConstant(@Nonnull final String name);

    /**
     * Gets a boolean config constant with the specified name.
     *
     * <p>Differently from {@link #getConfigConstant(String)},
     * this method should throw an exception if a boolean
     * constant with the given name is not found.</p>
     *
     * @param name
     *         The constant's name.
     * @return
     *         A {@link ConfigConstantWrapper wrapper} with the
     *         given constant stored.
     *
     * @since 1.0
     */
    @Nonnull
    ConfigConstantWrapper<Boolean> getBooleanConfigConstant(@Nonnull final String name);

    /**
     * Returns whether OpenMods-IGW should attempt to provide
     * wiki pages for the given mod ID.
     *
     * <p>This is mainly a wrapper around the same method
     * available in the config class.</p>
     *
     * @param modId
     *         The mod ID to check.
     * @return
     *         If OpenMods-IGW should attempt to provide wiki pages
     *         for the given mod ID.
     *
     * @since 1.0
     */
    boolean isEnabled(@Nonnull final String modId);

    /**
     * Gets the constant with the given name.
     *
     * <p>Differently from {@link #getConfigConstant(String)},
     * this method does not look for a config constant, but
     * for a real mod constant.</p>
     *
     * @param name
     *         The constant's name.
     * @param <T>
     *         The constant's type.
     * @return
     *         A wrapped constant instance, if available.
     *
     * @since 1.0
     */
    @Nonnull
    <T> ConfigConstantWrapper<T> getConstant(@Nonnull final String name);
}
