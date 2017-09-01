package openmods.igw.api.record.mod;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Represents a mismatching mod entry, used to hold all the
 * information the GUI needs to show.
 *
 * @since 1.0
 */
public interface IMismatchingModEntry extends IModEntry {

    /**
     * This annotation must be used on the mod's main class
     * to specify the version OpenMods-IGW should treat the
     * annotated mod like.
     *
     * <p>In other words, when annotating your main mod
     * class with this annotation, you need to specify a
     * certain version number. Refer to {@link #value()}
     * for more information.</p>
     *
     * <p>When the mod version checker reaches the mod's
     * class, it will search for this annotation and, if
     * present, consider the mod's version the one
     * specified in {@link #value()} instead of the one
     * present in the mod's container.</p>
     *
     * <p>This allows you to tell OpenMods-IGW to ignore
     * version mismatching because the information
     * reported on the wiki is still valid. This can be
     * useful for minor updates, which mainly consist of
     * bug fixes (even if then the change log is out of
     * date, but that's, like previously said, minor).</p>
     *
     * <p>Of course it's the developer's job to check
     * that the information reported on the annotation
     * is still valid before releasing a new mod version.</p>
     *
     * @since 1.0
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface VersionProvider {

        /**
         * Gets the version that the discoverer should treat
         * the mod's like.
         *
         * @return
         *         The version.
         *
         * @since 1.0
         */
        String value();
    }

    /**
     * Gets the mod entry associated with this mismatching object.
     *
     * @return
     *         The mod entry.
     *
     * @since 1.0
     */
    @Nonnull
    IModEntry mod();

    /**
     * Gets the current expected version.
     *
     * @return
     *         The expected version.
     */
    @Nonnull
    String installedVersion();
}
