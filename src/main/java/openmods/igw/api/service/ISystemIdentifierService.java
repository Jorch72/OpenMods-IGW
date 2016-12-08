package openmods.igw.api.service;

import com.google.common.base.Objects;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Identifies a service used to identify a particular
 * system for various needs.
 *
 * <p>An example could be in order to automatically activate various
 * debug features, without having to call the methods or activating
 * configuration options.</p>
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public interface ISystemIdentifierService extends IService<ISystemIdentifierService> {

	/**
	 * A class used to hold the details of a system.
	 *
	 * @author TheSilkMiner
	 * @since 1.0
	 */
	class SystemDetails {

		/**
		 * The operative system of the current system.
		 *
		 * @since 1.0
		 */
		protected String os;

		/**
		 * The architecture of the current system (32 bit or 64 bit).
		 *
		 * @since 1.0
		 */
		protected String architecture;

		/**
		 * The directory from where this software has been run.
		 *
		 * @since 1.0
		 */
		protected String runDir;

		/**
		 * The main disk's total space.
		 *
		 * @since 1.0
		 */
		protected long diskSpace;

		/**
		 * The number of available processors.
		 *
		 * @since 1.0
		 */
		protected int processors;

		@Override
		public final boolean equals(final Object o) {
			if (this == o) return true;
			if (!(o instanceof SystemDetails)) return false;
			SystemDetails that = (SystemDetails) o;
			return this.diskSpace == that.diskSpace &&
					this.processors == that.processors &&
					Objects.equal(this.os, that.os) &&
					Objects.equal(this.architecture, that.architecture) &&
					Objects.equal(this.runDir, that.runDir);
		}

		@Override
		public final int hashCode() {
			return Objects.hashCode(this.os, this.architecture, this.runDir, this.diskSpace, this.processors);
		}

		@Override
		public final String toString() {
			return Objects.toStringHelper(this)
					.add("os", this.os)
					.add("architecture", this.architecture)
					.add("runDir", this.runDir)
					.add("diskSpace", this.diskSpace)
					.add("processors", this.processors)
					.toString();
		}
	}

	/**
	 * Enum used to identify a certain type of system.
	 *
	 * @author TheSilkMiner
	 * @since 1.0
	 */
	enum SystemType {

		/**
		 * Identifies a normal mod user.
		 *
		 * @since 1.0
		 */
		USER,
		/**
		 * Identifies a beta tester of the mod.
		 *
		 * <p>Verbose logging and other features should be
		 * activated for users in this category.</p>
		 *
		 * @since 1.0
		 */
		BETA_TESTER,
		/**
		 * Identifies a developer of the mod.
		 *
		 * <p>All debug features should be activated for
		 * this type of user.</p>
		 *
		 * @since 1.0
		 */
		DEVELOPER
	}

	/**
	 * Registers the current system in memory.
	 *
	 * @param type
	 * 		The type of the system.
	 * @since 1.0
	 */
	void registerCurrentSystem(@Nonnull final SystemType type);

	/**
	 * Registers the given system in memory.
	 *
	 * @param details
	 * 		The system details.
	 * @param type
	 * 		The type of the system.
	 *
	 * @since 1.0
	 */
	void registerSystem(@Nonnull final SystemDetails details, @Nonnull final SystemType type);

	/**
	 * Switches the type of the current system to the specified one.
	 *
	 * @param newType
	 * 		The new type of the system.
	 *
	 * @since 1.0
	 */
	void switchCurrentType(@Nonnull final SystemType newType);

	/**
	 * Switches the type of the given system to the specified one.
	 *
	 * @param details
	 * 		The system details.
	 * @param newType
	 * 		The new type of the system.
	 *
	 * @since 1.0
	 */
	void switchType(@Nonnull final SystemDetails details, @Nonnull final SystemType newType);

	/**
	 * Unregisters the current system.
	 *
	 * @since 1.0
	 */
	void unRegisterCurrent();

	/**
	 * Unregisters the given system.
	 *
	 * @param details
	 * 		The system details.
	 *
	 * @since 1.0
	 */
	void unRegister(@Nonnull final SystemDetails details);

	/**
	 * Gets if the current system is known to this service.

	 * @return
	 * 		If the system is known to the service.
	 *
	 * @since 1.0
	 */
	boolean isCurrentSystemKnown();

	/**
	 * Gets if the system provided is currently known to this service.
	 *
	 * @param details
	 * 		The system details.
	 * @return
	 * 		If the system is known to the service.
	 *
	 * @since 1.0
	 */
	boolean isKnownSystem(@Nonnull final SystemDetails details);

	/**
	 * Gets if the current system can be considered of the specified type.
	 *
	 * @param type
	 * 		The minimum system type.
	 * @return
	 * 		If the current system can be considered of the specified type.
	 *
	 * @since 1.0
	 */
	boolean isCurrentSystemLevelEnough(@Nonnull final SystemType type);

	/**
	 * Gets if the system provided can be considered of the specified type.
	 *
	 * @param details
	 * 		The system details.
	 * @param type
	 * 		The minimum system type.
	 * @return
	 * 		If the system can be considered of the specified type.
	 *
	 * @since 1.0
	 */
	boolean isSystemLevelEnough(@Nonnull final SystemDetails details, @Nonnull final SystemType type);

	/**
	 * Gets the system type of the current system.
	 *
	 * @return
	 * 		The system type.
	 *
	 * @since 1.0
	 */
	@Nullable
	SystemType getSystemType();

	/**
	 * Gets the system type of the given system.
	 *
	 * @param details
	 * 		The system details.
	 * @return
	 * 		The system type.
	 *
	 * @since 1.0
	 */
	@Nullable
	SystemType getSystemType(@Nonnull final SystemDetails details);

	/**
	 * Generates a system details class with the current system details.
	 *
	 * @return
	 * 		A new and populated system details object.
	 *
	 * @since 1.0
	 */
	@Nonnull
	SystemDetails populate();

	/**
	 * Gets the currently known systems.
	 *
	 * @return
	 * 		The currently known list of systems.
	 *
	 * @since 1.0
	 */
	@Nonnull
	Map<SystemDetails, SystemType> getCurrentlyKnownSystems();
}
