package openmods.igw.proxies;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import openmods.igw.client.MismatchingVersionsGui;

import java.util.List;
import javax.annotation.Nonnull;

public interface IInitProxy {

	void preInit(FMLPreInitializationEvent evt);

	void init(FMLInitializationEvent evt);

	void postInit(FMLPostInitializationEvent evt);

	@Nonnull
	List<MismatchingVersionsGui.MismatchingModEntry> getMismatchingMods();
}
