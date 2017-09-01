package openmods.igw;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import openmods.igw.api.init.IInit;
import openmods.igw.api.proxy.IInitProxy;
import openmods.igw.impl.utils.Constants; // Can't use services due to annotations

// TODO Network handler method
@Mod(modid = Constants.MOD_ID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES,
        guiFactory = Constants.FACTORY_CLASS)
public final class OpenModsIGW implements IInit {

    @Mod.Instance(Constants.MOD_ID)
    @SuppressWarnings("unused")
    private static OpenModsIGW instance;

    @SidedProxy(modId = Constants.MOD_ID, clientSide = Constants.CLIENT_PROXY, serverSide = Constants.COMMON_PROXY)
    @SuppressWarnings("unused")
    private static IInitProxy proxy;

    @Mod.EventHandler
    @Override
    public void construct(final FMLConstructionEvent evt) {
        proxy().construct(evt);
    }

    @Mod.EventHandler
    @Override
    public void preInit(final FMLPreInitializationEvent evt) {
        proxy().preInit(evt);
    }

    @Mod.EventHandler
    @Override
    public void init(final FMLInitializationEvent evt) {
        proxy().init(evt);
    }

    @Mod.EventHandler
    @Override
    public void postInit(final FMLPostInitializationEvent evt) {
        proxy().postInit(evt);
    }

    public static IInitProxy proxy() {
        return proxy;
    }

    public static OpenModsIGW getInstance() {
        return instance;
    }
}
