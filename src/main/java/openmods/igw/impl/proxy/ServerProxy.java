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
