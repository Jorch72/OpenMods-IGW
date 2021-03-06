package openmods.igw.api.record;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies records that use an internal cache to store
 * their instances.
 *
 * <p>When annotated, the record registers all the created
 * instances in an internal cache. When an instance is then
 * requested, the record processes the internal cache before
 * creating a new entry. If an existing entry is found, that
 * one is returned, otherwise a new one is created, cached
 * and then returned.</p>
 *
 * <p>Be extremely wary when managing records annotated with
 * this annotation but not with {@link Immutable}. In certain
 * cases this may lead to undefined behaviour, influencing
 * the results of other methods and/or calls regarding that
 * specific entry.</p>
 *
 * <p><strong>To the developers:</strong> While it may seem so,
 * this annotation is not in the wrong package: the
 * {@code openmods.igw.api.cache} package is related to caching
 * in general, while this annotation is strictly related to
 * records and caching <strong>within</strong> records.</p>
 *
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Cached {
}
