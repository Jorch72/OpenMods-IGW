package openmods.igw;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;

@Mod(modid = OpenModsIGW.MODID, name = OpenModsIGW.NAME, version = OpenModsIGW.VERSION, dependencies = OpenModsIGW.DEPENDENCIES)
public class OpenModsIGW {
	public static final String MODID = "OpenMods-IGW";
	public static final String NAME = "OpenMods-IGW";
	public static final String VERSION = "$VERSION$";
	public static final String DEPENDENCIES = "required-after:OpenMods@[$LIB-VERSION$,$NEXT-LIB-VERSION$;required-after:IGWMod)";

	@Instance(MODID)
	public static OpenModsIGW instance;
}
