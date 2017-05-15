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
 * really simple. First, {@link #getAbilityToLoadIntegration()} is called
 * and, if the returned value reports that the integration is loadable,
 * the executor is obtained via {@link #getExecutor()} and then run.
 * If the method completes normally, then the {@link IIntegrationExecutor#andThen()}
 * is called first and {@link #onLoaded()} immediately after. If the
 * integration loading throws an exception, {@link #onException(Exception)}
 * is called instead.</p>
 *
 * @since 1.0
 */
public interface IIntegrationProvider {

	/**
	 * Represents the result of the current environment scan.
	 *
	 * @since 1.0
	 */
	enum ScanResult {

		/**
		 * Identifies an environment that matches all the various conditions
		 * needed to successfully load the integration.
		 *
		 * @since 1.0
		 */
		SAFE_TO_LOAD,
		/**
		 * Identifies an environment that doesn't perfectly match all the
		 * various conditions, but would still be able to load the integration
		 * without any errors.
		 *
		 * <p>E.g., an environment where an annotation whose retention policy
		 * is not runtime is not present may classify as a candidate for this
		 * value.</p>
		 *
		 * @since 1.0
		 */
		MAY_CAUSE_ERRORS,
		/**
		 * Identifies an environment where loading the integration will result
		 * most surely in a crash and, as such, the integration must not be
		 * loaded at all.
		 *
		 * @since 1.0
		 */
		UNSAFE_TO_LOAD
	}

	/**
	 * Gets the ability for the current environment to load the
	 * integration represented by this provider.
	 *
	 * <p>This method <b>must</b> check for all the various conditions
	 * needed to successfully load the integration. Assumptions are
	 * allowed iff they are always verified.</p>
	 *
	 * <p>Refer to the various constants in {@link ScanResult} for more
	 * information.</p>
	 *
	 * @return
	 * 		The ability for the current environment to load the integration.
	 *
	 * @since 1.0
	 */
	@Nonnull
	ScanResult getAbilityToLoadIntegration();

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
	 * @since 1.0
	 */
	void onLoaded();

	/**
	 * Executes a certain operation iff the environment has been found non-perfectly
	 * complaint with the various constraints.
	 *
	 * <p>Usually you would want to log the probable incompatibility in case something
	 * goes wrong during loading. It is also safe to leave this method as a
	 * {@code NO-OP} method.</p>
	 *
	 * <p>It is illegal to throw <b>voluntarily</b> exceptions during the execution of
	 * this method: you should return a value that marks impossibility to load the
	 * integration when {@link #getAbilityToLoadIntegration()} is called instead.</p>
	 *
	 * @since 1.0
	 */
	void onEnvironmentMismatch();

	/**
	 * Executes a certain operation if the integration failed to load.
	 *
	 * <p>Even if all the checks in place in {@link #getAbilityToLoadIntegration()}
	 * succeeded but the integration loading failed anyway, this method is
	 * called.</p>
	 *
	 * <p>You are not allowed to <b>voluntarily</b> throw exceptions in this
	 * method.</p>
	 *
	 * @param e
	 * 		The exception that caused the failure. It is never going to be
	 * 		{@code null}.
	 *
	 * @since 1.0
	 */
	void onException(@Nonnull final Exception e);
}
