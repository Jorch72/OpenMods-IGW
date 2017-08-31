package openmods.igw.impl.integration.openblocks;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.tuple.Pair;

import igwmod.gui.IReservedSpace;
import igwmod.gui.LocatedTexture;

import openmods.igw.prefab.handler.OpenModsWikiTab;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

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

		super(stacks, allClaimedPages, allClaimedEntities, new IItemPositionProvider() {
			@Override
			public int getX(final int entryId) {
				return 41 + entryId % 2 * 18;
			}

			@Override
			public int getY(final int entryId) {
				return 111 + entryId / 2 * 18;
			}
		});

		this.addPageToStaticPages(createStaticPageFactory("about", this, CommonPositionProviders.STATIC_PAGES));
		this.addPageToStaticPages(createStaticPageFactory("credits", this, CommonPositionProviders.STATIC_PAGES));
		this.addPageToStaticPages(createStaticPageFactory("obUtils", this, CommonPositionProviders.STATIC_PAGES));
		this.addPageToStaticPages(createStaticPageFactory("bKey", this, CommonPositionProviders.STATIC_PAGES));
		this.addPageToStaticPages(createStaticPageFactory("enchantments", this, CommonPositionProviders.STATIC_PAGES));
		this.addPageToStaticPages(createStaticPageFactory("changelog", this, CommonPositionProviders.STATIC_PAGES));
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
				"textures/gui/wiki/6x2.png");
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

	@Nonnull
	@Override
	protected List<Block> getBlockCandidates() {
		return Lists.newArrayList(OpenBlocksItemHolder.FLAG, Blocks.SPONGE);
	}
}
