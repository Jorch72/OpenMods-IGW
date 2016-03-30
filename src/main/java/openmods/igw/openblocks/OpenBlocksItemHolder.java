package openmods.igw.openblocks;

import net.minecraft.block.Block;
import openmods.Mods;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Mods.OPENBLOCKS)
@SuppressWarnings("WeakerAccess")
public class OpenBlocksItemHolder {

	@GameRegistry.ObjectHolder("flag")
	public static final Block FLAG = null;

	@GameRegistry.ObjectHolder("canvasglass")
	public static final Block GLASS_CANVAS = null;
}