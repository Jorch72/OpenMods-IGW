package openmods.igw.common;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import igwmod.gui.IReservedSpace;
import igwmod.gui.LocatedTexture;

import java.util.List;
import java.util.Map;

public class OpenModsCommonTab extends OpenModsWikiTab {

	@SuppressWarnings("unused")
	//@Explain("??")
	public OpenModsCommonTab(final List<Pair<String, ItemStack>> stacks,
							 final Map<String, ItemStack> allClaimedPages,
							 final Map<String, Class<? extends Entity>> allClaimedEntities) {

		super(stacks, allClaimedPages, allClaimedEntities, CommonPositionProviders.ITEMS_WITHOUT_STATIC_PAGES);

		// Since it is a common one, we will not make any special assumptions:
		// simply a list of blocks and items.
		// Maybe just some static pages to display OpenMods-IGW related things
		// and or other problems.
		// Still consider this as an uncompleted/beta thing.

		/*
		this.addPageToStaticPages(createStaticPageFactory("about", this, CommonPositionProviders.STATIC_PAGES));
		this.addPageToStaticPages(createStaticPageFactory("credits", this, CommonPositionProviders.STATIC_PAGES));
		this.addPageToStaticPages(createStaticPageFactory("changelogs", this, CommonPositionProviders.STATIC_PAGES));
		*/
	}

	@Override
	public String getTabName() {
		return "openmods.igw.tab.common.name";
	}

	@Override
	public String getPageName() {
		return "openmods.igw.tab.common.page";
	}

	@Override
	public List<IReservedSpace> getReservedSpaces() {
		List<IReservedSpace> reservedSpaces = Lists.newArrayList();
		reservedSpaces.add(new LocatedTexture(igwmod.lib.Textures.GUI_ITEMS_AND_BLOCKS, 40, 74, 36, 144));
		return reservedSpaces;
	}

	@Override
	public int getSearchBarAndScrollStartY() {
		return 61;
	}

	@Override
	public int pagesPerTab() {
		return 16;
	}

	@Override
	public int pagesPerScroll() {
		return 2;
	}
}
