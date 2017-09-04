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
package openmods.igw.impl.client;

import com.google.common.base.Preconditions;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.service.IClassProviderService;
import openmods.igw.api.service.IGuiService;

@SideOnly(Side.CLIENT)
public class GuiOpenEventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    //@Explain("Called by Forge, not by us")
    public void onMainMenuOpen(final GuiOpenEvent event) {
        if (!(event.getGui() instanceof GuiMainMenu)) return;

        final IGuiService it = Preconditions.checkNotNull(OpenModsIGWApi.get().serviceManager()
                .obtainAndCastService(IGuiService.class));

        if (it.shouldShow(IGuiService.GUIs.WARNING)) {
            event.setGui(it.<GuiScreen>construct(IGuiService.GUIs.WARNING));
            return;
        }

        if (it.shouldShow(IGuiService.GUIs.MISMATCHING_MODS)) {
            event.setGui(it.<GuiScreen>construct(IGuiService.GUIs.MISMATCHING_MODS, Preconditions.checkNotNull(
                    Preconditions.checkNotNull(
                            OpenModsIGWApi.get().serviceManager().obtainAndCastService(IClassProviderService.class)
                    ).proxy()
            ).getMismatchingMods()));
        }
    }
}
