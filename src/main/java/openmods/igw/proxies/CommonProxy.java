package openmods.igw.proxies;

import com.google.common.collect.Lists;

import openmods.igw.client.MismatchingVersionsGui;
import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import openmods.Log;
import openmods.igw.utils.Constants;

import java.util.List;
import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class CommonProxy implements IInitProxy {

	@Override
	public void preInit(final FMLPreInitializationEvent evt) {}

	@Override
	public void init(final FMLInitializationEvent evt) {}

	@Override
	public void postInit(final FMLPostInitializationEvent evt) {
		Log.warn("This mod (%s) is currently not needed on server side.", Constants.NAME);
		Log.info("You can install it to force people to use it, though.");
	}

	@Override
	@Nonnull
	public List<MismatchingVersionsGui.MismatchingModEntry> getMismatchingMods() {
		return Lists.newArrayList();
	}
}
