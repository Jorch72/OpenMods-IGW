package openmods.igw.impl.proxy;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.init.IPageInit;
import openmods.igw.api.proxy.IInitProxy;
import openmods.igw.api.record.mod.IMismatchingModEntry;
import openmods.igw.api.service.IConstantRetrieverService;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class CommonProxy implements IInitProxy {

	@Override
	public void construct(final FMLConstructionEvent event) {
		// Force load services
		OpenModsIGWApi.get().obtainService(IConstantRetrieverService.class);
	}

	@Override
	public void preInit(final FMLPreInitializationEvent evt) {}

	@Override
	public void init(final FMLInitializationEvent evt) {}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void postInit(final FMLPostInitializationEvent evt) {
		OpenModsIGWApi.get().log().warning("This mod (%s) is currently not needed on server side.",
				OpenModsIGWApi.get().obtainService(IConstantRetrieverService.class).get().cast().getConstant("NAME")
						.orNull());
		OpenModsIGWApi.get().log().info("You can install it to force people to use it, though.");
	}

	@Nonnull
	@Override
	public List<IMismatchingModEntry> getMismatchingMods() {
		return Lists.newArrayList();
	}

	@Nullable
	@Override
	public IPageInit asPageInit() {
		return null;
	}

	@Override
	public void addMismatchingMod(@Nonnull final IMismatchingModEntry entry) {}
}
