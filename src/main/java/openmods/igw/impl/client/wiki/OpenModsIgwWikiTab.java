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
package openmods.igw.impl.client.wiki;

import igwmod.gui.GuiWiki;
import igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import openmods.igw.api.OpenModsIGWApi;

/**
 * Wiki tab used by this mod to show some information.
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public class OpenModsIgwWikiTab extends BaseWikiTab {

    public OpenModsIgwWikiTab() {
        super();

        this.pageEntries.add("about");
        this.pageEntries.add("how_to");
        // TODO Add other pages here
        this.pageEntries.add("changelog");
        this.pageEntries.add("contacts_credits");
    }

    @Override
    protected String getPageName(final String pageEntry) {
        return OpenModsIGWApi.get().translate("wiki.openmods.igw.page." + pageEntry);
    }

    @Override
    protected String getPageLocation(final String pageEntry) {
        return "openmods-igw:tab/openmods-igw." + pageEntry;
    }

    @Override
    public String getName() {
        return "wiki.openmods.igw.tab";
    }

    @Override
    public ItemStack renderTabIcon(final GuiWiki gui) {
        return new ItemStack(Items.COMPASS);
    }
}
