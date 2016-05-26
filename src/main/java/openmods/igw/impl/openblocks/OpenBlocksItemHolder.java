package openmods.igw.impl.openblocks;

import net.minecraft.block.Block;

import cpw.mods.fml.common.registry.GameRegistry;

import openmods.Mods;

@GameRegistry.ObjectHolder(Mods.OPENBLOCKS)
@SuppressWarnings("WeakerAccess")
public final class OpenBlocksItemHolder {

	@GameRegistry.ObjectHolder("flag")
	public static final Block FLAG = null;

	@GameRegistry.ObjectHolder("canvasglass")
	public static final Block GLASS_CANVAS = null;
}