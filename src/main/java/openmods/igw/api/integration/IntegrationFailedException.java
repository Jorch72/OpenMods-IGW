package openmods.igw.api.integration;

import javax.annotation.Nonnull;

/**
 * Wraps every exception thrown during integration loading.
 *
 * @since 1.0
 */
public class IntegrationFailedException extends RuntimeException {

	/**
	 * Constructs a new runtime exception with the specified cause and a
	 * detail message of {@code cause.toString()} (which typically contains
	 * the class and detail message of {@code cause}).
	 *
	 * @param cause
	 * 		The cause (which is saved for later retrieval by the
	 * 		{@link #getCause()} method). It must not be {@code null}.
	 *
	 * @since 1.0
	 */
	public IntegrationFailedException(@Nonnull final Throwable cause) {
		super(cause);
	}
}
