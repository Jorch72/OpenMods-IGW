package openmods.igw.impl.integration.openblocks;

import openmods.Mods;
import openmods.igw.api.integration.IIntegrationExecutor;
import openmods.igw.prefab.integration.AbstractIntegrationProvider;
import openmods.igw.prefab.record.mod.ModEntry;

import javax.annotation.Nonnull;

/**
 * Provides integration for OpenBlocks.
 *
 * @since 1.0
 */
public final class OpenBlocksIntegrationProvider extends AbstractIntegrationProvider {

	private static final String SUPPORTED_OPEN_BLOCKS_VERSION = "1.6";

	public OpenBlocksIntegrationProvider() {
		super(ModEntry.of(Mods.OPENBLOCKS, SUPPORTED_OPEN_BLOCKS_VERSION));
	}

	@Nonnull
	@Override
	public IIntegrationExecutor getExecutor() {
		return new OpenBlocksIntegrationExecutor(this.getModEntry());
	}
}
