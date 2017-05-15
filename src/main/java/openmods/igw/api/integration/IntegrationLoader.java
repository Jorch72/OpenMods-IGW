package openmods.igw.api.integration;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Manages the various integrations for the caller and loads them
 * when requested.
 *
 * <p>All {@link IIntegrationProvider} must be registered before
 * the loading procedure is initiated. Loading leaves the object in
 * an invalid state and thus a new loader must be created.</p>
 *
 * <p>The use of only one loader for every integration a software
 * provides is extremely encouraged. Also, API users are encouraged
 * to use this implementation instead of loading the various
 * integrations manually.</p>
 *
 * <p>When the caller obtains an instance of this class, it should
 * hold references to this object: every time the {@link #construct()}
 * method is called, a new instance is created. The caller is allowed
 * to discard the instance after the loading of all the various
 * integrations has been performed.</p>
 *
 * <p>It is highly suggested that loading takes place at the last
 * stage of software loading, while registration as soon as the program
 * starts up.</p>
 *
 * @since 1.0
 */
public class IntegrationLoader {

	private static final String MUST_NOT_BE_NULL = "%s must not be null";
	private static final String INTEGRATIONS_LOADED = "The integrations have already been loaded";
	private static final String INTEGRATIONS_NOT_LOADED = "The integrations have not been loaded yet";

	private final List<IIntegrationProvider> providers;
	private final List<IntegrationFailedException> exceptions;

	private boolean state;

	private IntegrationLoader() {
		this.providers = Lists.newArrayList();
		this.exceptions = Lists.newArrayList();
		this.state = true;
	}

	/**
	 * Constructs a new instance of this loader and returns it.
	 *
	 * @return
	 * 		A new instance of this loader.
	 *
	 * @since 1.0
	 */
	@Nonnull
	public static IntegrationLoader construct() {
		return new IntegrationLoader();
	}

	/**
	 * Registers the given {@link IIntegrationProvider} in the providers registry.
	 *
	 * @param provider
	 * 		The provider to register. It must not be {@code null}.
	 * @return
	 * 		If the provider was registered successfully.
	 * @throws IllegalStateException
	 * 		If the integrations have already been loaded.
	 *
	 * @since 1.0
	 */
	public boolean registerIntegration(@Nonnull final IIntegrationProvider provider) {
		Preconditions.checkNotNull(provider, String.format(MUST_NOT_BE_NULL, "provider"));
		Preconditions.checkState(this.state, INTEGRATIONS_LOADED);
		return this.providers.add(provider);
	}

	/**
	 * Registers the given {@link IIntegrationProvider}s in the providers registry.
	 *
	 * @param providers
	 * 		The providers to register. It must not be {@code null}.
	 * @return
	 * 		If all the providers were registered successfully.
	 * @throws IllegalStateException
	 * 		If the integrations have already been loaded.
	 *
	 * @since 1.0
	 */
	public boolean registerIntegrations(@Nonnull final IIntegrationProvider... providers) {
		boolean result = true;
		for (final IIntegrationProvider provider : providers) result &= this.registerIntegration(provider);
		return result;
	}

	/**
	 * Attempts to load the registered integrations.
	 *
	 * <p>The order in which integrations are loaded is <strong>not</strong>
	 * guaranteed to be the same in which the integrations have been
	 * registered.</p>
	 *
	 * <p>If, for whatever reason, any of the integrations throws an exception
	 * while loading, then the exception is going to be stored and will be
	 * able to be retrieved through {@link #getThrownExceptions()}.</p>
	 *
	 * @return
	 * 		If the integrations have thrown any exceptions during loading.
	 * @throws IllegalStateException
	 * 		If the integrations have already been loaded.
	 *
	 * @since 1.0
	 */
	public boolean load() {
		Preconditions.checkState(this.state, INTEGRATIONS_LOADED);
		this.state = false;
		this.providers.forEach(this::load0);
		return !this.exceptions.isEmpty();
	}

	private void load0(final IIntegrationProvider provider) {
		final IIntegrationProvider.ScanResult environmentStatus = provider.getAbilityToLoadIntegration();

		if (environmentStatus == IIntegrationProvider.ScanResult.UNSAFE_TO_LOAD) return;
		if (environmentStatus == IIntegrationProvider.ScanResult.MAY_CAUSE_ERRORS) provider.onEnvironmentMismatch();

		try {
			this.loadImpl(provider);
		} catch (final Exception e) {
			this.exceptions.add(new IntegrationFailedException(e));
			provider.onException(e);
		}
	}

	private void loadImpl(final IIntegrationProvider provider) throws Exception {
		provider.getExecutor().integrate();
		provider.getExecutor().andThen();
		provider.onLoaded();
	}

	/**
	 * Gets a {@link Collection} of every exception thrown during integration
	 * loading.
	 *
	 * <p>All the various exceptions have been wrapped in
	 * {@link IntegrationFailedException}s for ease of usage.</p>
	 *
	 * @return
	 * 		A collection containing all the exceptions thrown during loading.
	 * @throws IllegalStateException
	 * 		If the integrations have not been loaded yet.
	 *
	 * @since 1.0
	 */
	@Nonnull
	public Collection<IntegrationFailedException> getThrownExceptions() {
		Preconditions.checkState(!this.state, INTEGRATIONS_NOT_LOADED);
		return ImmutableList.copyOf(this.exceptions);
	}
}