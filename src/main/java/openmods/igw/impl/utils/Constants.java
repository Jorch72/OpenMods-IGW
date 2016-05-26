package openmods.igw.impl.utils;

import openmods.Mods;

import openmods.igw.api.record.mod.IModEntry;
import openmods.igw.prefab.record.mod.ModEntry;

public final class Constants {

	public static final String MOD_ID = "OpenMods-IGW";
	public static final String NAME = "OpenMods-IGW";
	public static final String VERSION = "$VERSION$";
	public static final String FACTORY_CLASS = "openmods.igw.impl.client.GuiFactory";
	@SuppressWarnings("unused")
	//@Explain("Use of this constant: printing the string for deps under here ;)")
	public static final DepBuilder DEPENDENCIES = new DepBuilder()
			.addDep(DepBuilder.Type.AFTER, Mods.IGW)
			.addDep(DepBuilder.Type.AFTER, Mods.OPENBLOCKS);
	// Stupid Java restrictions!
	public static final String DEPS = "required-after:OpenMods@[$LIB-VERSION$,$NEXT-LIB-VERSION$);" +
			"after:IGWMod;" +
			"after:OpenBlocks";

	public static final String CLIENT_PROXY = "openmods.igw.impl.proxy.ClientProxy";
	public static final String COMMON_PROXY = "openmods.igw.impl.proxy.CommonProxy";

	@SuppressWarnings("unused")
	public static final IModEntry[] CURRENTLY_SUPPORTED_MODS = new IModEntry[] {
																			ModEntry.of(Mods.OPENBLOCKS, "1.5.1"),
																		 };
}
