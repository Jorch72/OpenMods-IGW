package openmods.igw.api.record;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks immutable records.
 *
 * <p>The use of this annotation is not required, but it
 * is suggested to help the software developers while
 * dealing with mutable and immutable objects (e.g. avoid
 * casting to the wrong interface).</p>
 *
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Immutable {

    /**
     * Marks if it is possible to switch to a mutable
     * version of the same type.
     *
     * @return
     *         If it is possible to switch to a mutable type.
     *
     * @since 1.0
     */
    boolean toMutable() default false;
}
