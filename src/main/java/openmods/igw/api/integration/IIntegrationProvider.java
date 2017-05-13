package openmods.igw.api.integration;

import javax.annotation.Nonnull;

/**
 * Identifies a provider that allows for integration of the current
 * software with third-party programs.
 *
 * <p>Every provider provides only for one software integration.
 * To integrate with multiple programs you need to use multiple
 * providers.</p>
 *
 * <p>Also, every provider must provide a valid
 * {@link IIntegrationExecutor executor} and perform all the various
 * checks beforehand. E.g., if the executor needs classes {@code X},
 * {@code Y} and {@code Z} to work properly, the provider must check
 * for the existence of these classes before reporting if the loading
 * can be done.</p>
 *
 * <p>Assumptions on the status of loaded classes is allowed as long
 * as there is a main condition. E.g., if a program needs classes
 * {@code X} and {@code Y}, but {@code Y} is always present if {@code X}
 * is, then it is allowed to skip the check for {@code Y}.</p>
 *
 * <p>In an ideal condition, if all provider checks do not fail,
 * integration should happen without any exceptions thrown.</p>
 *
 * <p>The flow of an integration loading concerning the provider is
 * really simple. First, {@link #canLoadIntegration()} is called and,
 * if the returned value is {@code true}, the executor is obtained
 * via {@link #getExecutor()} and then run. If the method completes
 * normally, then the {@link IIntegrationExecutor#andThen()}
 * is called first and {@link #onLoaded()} immediately after. If the
 * integration loading throws an exception, {@link #onException(Exception)}
 * is called instead.</p>
 *
 * @since 1.0
 */
public interface IIntegrationProvider {

	/**
	 * Gets if the integration represented by this provider can be
	 * loaded without errors.
	 *
	 * <p>This method <b>must</b> check for all the various conditions
	 * needed to successfully load the integration. Assumptions are
	 * allowed iff they are always verified.</p>
	 *
	 * @return
	 * 		If the integration can be loaded.
	 *
	 * @since 1.0
	 */
	boolean canLoadIntegration();

	/**
	 * Gets the executor for this provider.
	 *
	 * <p>Refer to {@link IIntegrationExecutor} for more information.</p>
	 *
	 * @return
	 * 		This provider's executor.
	 *
	 * @since 1.0
	 */
	@Nonnull
	IIntegrationExecutor getExecutor();

	/**
	 * Executes a certain operation after the integration has been successfully
	 * loaded.
	 *
	 * <p>You should perform any cleanup or notification operations in here.</p>
	 *
	 * <p>By default this method does nothing.</p>
	 *
	 * @since 1.0
	 */
	default void onLoaded() {}

	/**
	 * Executes a certain operation if the integration failed to load.
	 *
	 * <p>Even if all the checks in place in {@link #canLoadIntegration()}
	 * succeeded but the integration loading failed anyway, this method is
	 * called.</p>
	 *
	 * @param e
	 * 		The exception that caused the failure. It is never going to be
	 * 		{@code null}.
	 *
	 * @since 1.0
	 */
	void onException(@Nonnull final Exception e);
}
