/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Open Mods
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
