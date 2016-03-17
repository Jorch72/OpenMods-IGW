package openmods.igw.common;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import igwmod.gui.IReservedSpace;
import igwmod.gui.LocatedTexture;

import java.util.List;
import java.util.Map;

public class OpenModsCommonTab extends OpenModsWikiTab {

	public OpenModsCommonTab(final List<Pair<String, ItemStack>> stacks,
							 final Map<String, ItemStack> allClaimedPages,
							 final Map<String, Class<? extends Entity>> allClaimedEntities) {

		super(stacks, allClaimedPages, allClaimedEntities);

		// Since it is a common one, we will not make any special assumptions:
		// simply a list of blocks and items.
		// Maybe just some static pages to display OpenMOds-IGW related things
		// and or other problems.
		// Still consider this as an uncompleted/beta thing.

		this.addPageToStaticPages(createStaticPageFactory("about", this));
		this.addPageToStaticPages(createStaticPageFactory("credits", this));
		this.addPageToStaticPages(createStaticPageFactory("changelogs", this));
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
		final List<IReservedSpace> reservedSpaces = Lists.newArrayList();
		final ResourceLocation textureLocation = new ResourceLocation("openmods-igw",
				"textures/gui/wiki/shorterItemGrid.png");
		reservedSpaces.add(new LocatedTexture(textureLocation, 40, 110, 36, 108));
		return reservedSpaces;
	}

	@Override
	public int getSearchBarAndScrollStartY() {
		return 98;
	}

	@Override
	public int pagesPerTab() {
		return 12;
	}

	@Override
	public int pagesPerScroll() {
		return 2;
	}
}
