package openmods.igw.impl.integration;

import com.google.common.collect.Sets;
import openmods.Mods;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.integration.IIntegrationProvider;
import openmods.igw.api.integration.IntegrationFailedException;
import openmods.igw.api.integration.IntegrationLoader;

public enum IntegrationHandler {
	IT;

	private final IntegrationLoader loader;

	IntegrationHandler() {
		this.loader = IntegrationLoader.construct();
	}

	public void register() {
		OpenModsIGWApi.get().log().info("Registering provided mod integrations");

		this.register(Mods.OPENBLOCKS, null); // TODO

		OpenModsIGWApi.get().log().info("Successfully registered integrations");
	}

	private void register(final String modId, final IIntegrationProvider provider) {
		OpenModsIGWApi.get().log().info("Registering provider (class %s) for mod %s in loader %s", provider.getClass(), modId, this.loader);
		this.loader.registerIntegration(provider);
	}

	public void load() {
		OpenModsIGWApi.get().log().info("Loading external mod integrations");

		if (this.loader.load()) {
			OpenModsIGWApi.get().log().info("Successfully loaded all integrations");
		} else {
			OpenModsIGWApi.get().log().warning("Some errors have occurred while registering integrations");

			for (final IntegrationFailedException e : this.loader.getThrownExceptions()) {
				OpenModsIGWApi.get().log().warning(e, e.getMessage());
			}
		}
	}
}
