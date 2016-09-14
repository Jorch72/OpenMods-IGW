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
		 * 		The constant value, if available.
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
		 * 		The default value to return.
		 * @return
		 * 		The constant or the default value.
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
		 * 		The constant value or {@code null}
		 *
		 * @since 1.0
		 */
		@SuppressWarnings("ConstantConditions")
		@Nullable
		public T orNull() {
			return this.orElse(null);
		}

		/**
		 * Gets if the constant is present.
		 *
		 * @return
		 * 		If the constant is present.
		 *
		 * @since 1.0
		 */
		public boolean isPresent() {
			return this.constant != null;
		}
	}

	/**
	 * Gets the config constant with the specified name.
	 *
	 * @param name
	 * 		The constant's name.
	 * @param <T>
	 *     	The constant's type.
	 * @return
	 * 		A {@link ConfigConstantWrapper wrapper} with the
	 * 		given constant stored, if available.
	 *
	 * @since 1.0
	 */
	@Nonnull
	<T> ConfigConstantWrapper<T> getConfigConstant(@Nonnull final String name);

	/**
	 * Gets a boolean config constant with the specified name.
	 *
	 * <p>Differently from {@link #getConfigConstant(String)},
	 * this method should thrown an exception if a boolean
	 * constant with the given name is not found.</p>
	 *
	 * @param name
	 * 		The constant's name.
	 * @return
	 * 		A {@link ConfigConstantWrapper wrapper} with the
	 * 		given constant stored.
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
	 * 		The mod ID to check.
	 * @return
	 * 		If OpenMods-IGW should attempt to provide wiki pages
	 * 		for the given mod ID.
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
	 * 		The constant's name.
	 * @param <T>
	 *     	The constant's type.
	 * @return
	 * 		A wrapped constant instance, if available.
	 *
	 * @since 1.0
	 */
	@Nonnull
	<T> ConfigConstantWrapper<T> getConstant(@Nonnull final String name);
}
