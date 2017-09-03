package openmods.igw.impl.proxy;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import openmods.Log;
import openmods.igw.api.init.IPageInit;
import openmods.igw.api.proxy.IInitProxy;
import openmods.igw.api.record.mod.IMismatchingModEntry;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class ServerProxy implements IInitProxy {

    @Override
    public void construct(final FMLConstructionEvent event) {}

    @Override
    public void preInit(final FMLPreInitializationEvent evt) {}

    @Override
    public void init(final FMLInitializationEvent evt) {}

    @Override
    public void postInit(final FMLPostInitializationEvent evt) {
        // Use direct implementation imports mainly because we do not want to crash with client-side only services
        // TODO Split client-side services and load them only through ClientProxy
        Log.warn("This mod is currently not needed on server side.");
        Log.warn("You can install it to force people to use it, though.");
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
