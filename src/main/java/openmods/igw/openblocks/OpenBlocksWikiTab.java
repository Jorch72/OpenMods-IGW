package openmods.igw.openblocks;

import igwmod.gui.IReservedSpace;
import igwmod.gui.LocatedTexture;

import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import openmods.igw.common.OpenModsWikiTab;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

/*
 * WARNING!
 * The addition of new static pages most likely requires the editing of the
 * search bar's position and the amount of items shown in the wiki
 * tab. These edits have to be performed in the methods:
 * pagesPerTab() (where you need to edit the amount of items shown, usually diminishing by 2 every time)
 * getReservedSpaces() (where the y of the located texture must be edited to allow for a design-compatible number).
 */
public final class OpenBlocksWikiTab extends OpenModsWikiTab {

	public OpenBlocksWikiTab(final List<Pair<String, ItemStack>> stacks,
							 final Map<String, ItemStack> allClaimedPages,
							 final Map<String, Class<? extends Entity>> allClaimedEntities) {

		super(stacks, allClaimedPages, allClaimedEntities);

		this.addPageToStaticPages(createStaticPageFactory("about", this));
		this.addPageToStaticPages(createStaticPageFactory("credits", this));
		this.addPageToStaticPages(createStaticPageFactory("obUtils", this));
		this.addPageToStaticPages(createStaticPageFactory("bKey", this));
		this.addPageToStaticPages(createStaticPageFactory("enchantments", this));
		this.addPageToStaticPages(createStaticPageFactory("changelogs", this));
	}

	@Override
	public String getTabName() {
		return "wiki.openblocks.tab";
	}

	@Override
	public String getPageName() {
		return "wiki.openblocks.page";
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

	@Override
	protected Block[] getBlockCandidates() {
		return new Block[] {OpenBlocksItemHolder.flag, Blocks.sponge};
	}
}
