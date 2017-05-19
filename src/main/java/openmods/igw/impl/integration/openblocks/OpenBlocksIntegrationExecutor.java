package openmods.igw.impl.integration.openblocks;

import openmods.igw.api.record.mod.IModEntry;
import openmods.igw.prefab.integration.AbstractIntegrationExecutor;

import javax.annotation.Nonnull;

/**
 * Integrates with OpenBlocks
 *
 * @since 1.0
 */
public final class OpenBlocksIntegrationExecutor extends AbstractIntegrationExecutor {

	public OpenBlocksIntegrationExecutor(@Nonnull final IModEntry entry) {
		super(entry, OpenBlocksWikiTab.class, OpenBlocksEventHandler.class);
	}
}
