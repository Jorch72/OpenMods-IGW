package openmods.igw.utils;

/**
 * @author TheSilkMiner
 */
public class Constants {

	public static final String MOD_ID = "OpenMods-IGW";
	public static final String NAME = "OpenMods-IGW";
	public static final String VERSION = "$VERSION$";
	/*
	public static final DepBuilder DEPENDENCIES = new DepBuilder()
			.addDep(DepBuilder.Type.AFTER, Mods.IGW)
			.addDep(DepBuilder.Type.AFTER, Mods.OPENBLOCKS);
	*/
	// Stupid Java restrictions!
	public static final String DEPS = "required-after:OpenMods@[$LIB-VERSION$,$NEXT-LIB-VERSION$);" +
			"after:IGWMod;" +
			"after:OpenBlocks";

	public static final String CLIENT_PROXY = "openmods.igw.proxies.ClientProxy";
	public static final String COMMON_PROXY = "openmods.igw.proxies.CommonProxy";
}
