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
package openmods.igw.impl.integration.openblocks;

import net.minecraft.item.ItemStack;

import igwmod.api.PageChangeEvent;
import igwmod.api.VariableRetrievalEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.prefab.handler.OpenModsEventHandler;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
//@Explain("Accessed via reflection")
public final class OpenBlocksEventHandler extends OpenModsEventHandler {

    @SuppressWarnings("SpellCheckingInspection")
    private static final String GLASS_CANVAS_PAGE_NAME = "block/openblocks.canvasglass";

    @SubscribeEvent
    public void handleCustomBlocks(final PageChangeEvent event) {
        if (GLASS_CANVAS_PAGE_NAME.equals(event.currentFile)) {
            OpenModsIGWApi.get().log().info("Redirected Glass Canvas page from default to OpenMods-IGW");
            this.redirectToIgwTab(event);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("ConstantConditions")
    //@Explain("Field populated by Forge, so...")
    public void handleCustomIcons(final PageChangeEvent event) {
        // TODO Wouldn't this be a "equals" check?
        if (event.currentFile.contains(GLASS_CANVAS_PAGE_NAME) && OpenBlocksItemHolder.GLASS_CANVAS != null) {
            OpenModsIGWApi.get().log().info("Associated Glass Canvas icon to page");
            this.askTabForNewIcon(event, new ItemStack(OpenBlocksItemHolder.GLASS_CANVAS));
        }
    }

    @SubscribeEvent
    public void addCurrentBlockStatus(final VariableRetrievalEvent event) {
        this.replaceVariableWithBlockStatus(event);
    }

    @SubscribeEvent
    public void addCurrentConfigValues(final VariableRetrievalEvent event) {
        this.replaceVariableWithConfigValue(event);
    }

    @SubscribeEvent
    public void addCurrentItemStatus(final VariableRetrievalEvent event) {
        this.replaceVariableWithItemStatus(event);
    }

    @Nonnull
    @Override
    public String modId() {
        return openmods.Mods.OPENBLOCKS;
    }

    @Nonnull
    @Override
    @SuppressWarnings("SpellCheckingInspection")
    public String configClass() {
        return "openblocks.Config";
    }
}
