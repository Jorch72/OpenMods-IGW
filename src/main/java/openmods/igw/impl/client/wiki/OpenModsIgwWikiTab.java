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
