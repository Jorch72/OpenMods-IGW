package openmods.igw;

import igwmod.gui.*;
import igwmod.gui.tabs.IWikiTab;

import java.util.List;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/*
 * WARNING!
 * The addition of new static pages most likely requires the editing of the
 * search bar's position and the amount of items shown in the wiki
 * tab. These edits have to be performed in the methods:
 * pagesPerTab() (where you need to edit the amount of items shown, usually diminishing by 2 every time)
 * getReservedSpaces() (where the y of the located texture must be edited to allow for a design-compatible number).
 */
public final class OpenBlocksWikiTab implements IWikiTab {

	private static RenderItem renderer = new RenderItem();

	static {
		renderer.setRenderManager(RenderManager.instance);
	}

	private ItemStack tabIcon;

	private static class LinkNumerator {
		private int staticEntries;
		private int itemEntries;

		private int getNextStaticId() {
			return staticEntries++;
		}

		private int getNextItemId() {
			return itemEntries++;
		}
	}

	private interface IPageLinkFactory {
		public IPageLink createPage(LinkNumerator numerator);
	}

	private static IPageLinkFactory createStaticPageFactory(final String id) {
		return new IPageLinkFactory() {
			@Override
			public IPageLink createPage(LinkNumerator numerator) {
				final String localizedLinkName = StatCollector.translateToLocal("wiki.openblocks.page." + id);
				return new LocatedString(localizedLinkName,
						80,
						125 + 11 * numerator.getNextStaticId(),
						false,
						"openmods-igw:tab/" + id);
			}
		};
	}

	private static IPageLinkFactory createItemPageFactory(final String location, final ItemStack itemStack) {
		return new IPageLinkFactory() {
			@Override
			public IPageLink createPage(LinkNumerator numerator) {
				final int entryNumber = numerator.getNextItemId();
				return new LocatedStack(itemStack,
						41 + entryNumber % 2 * 18,
						111 + entryNumber / 2 * 18) {
					@Override
					public String getLinkAddress() {
						return location;
					}
				};
			}
		};
	}

	public final List<IPageLinkFactory> staticPageFactories = Lists.newArrayList();

	public final List<IPageLinkFactory> itemPageFactories = Lists.newArrayList();

	public OpenBlocksWikiTab(List<Pair<String, ItemStack>> stacks) {
		staticPageFactories.add(createStaticPageFactory("credits"));
		staticPageFactories.add(createStaticPageFactory("obUtils"));
		staticPageFactories.add(createStaticPageFactory("bKey"));
		staticPageFactories.add(createStaticPageFactory("enchantments"));
		staticPageFactories.add(createStaticPageFactory("changelogs"));

		for (Pair<String, ItemStack> e : stacks)
			itemPageFactories.add(createItemPageFactory(e.getLeft(), e.getRight()));
	}

	@Override
	public String getName() {
		return "wiki.openblocks.tab";
	}

	@Override
	public List<IReservedSpace> getReservedSpaces() {
		final List<IReservedSpace> reservedSpaces = Lists.newArrayList();
		final ResourceLocation textureLocation = new ResourceLocation("openmods-igw", "textures/gui/wiki/shorterItemGrid.png");
		reservedSpaces.add(new LocatedTexture(textureLocation, 40, 110, 36, 108));
		return reservedSpaces;
	}

	@Override
	public List<IPageLink> getPages(int[] pageIndexes) {
		final List<IPageLink> pages = Lists.newArrayList();
		LinkNumerator numerator = new LinkNumerator();
		if (pageIndexes == null) {
			for (IPageLinkFactory factory : itemPageFactories)
				pages.add(factory.createPage(numerator));
		}
		else {
			for (int index : pageIndexes) {
				if (index >= itemPageFactories.size()) break;
				IPageLinkFactory factory = itemPageFactories.get(index);
				pages.add(factory.createPage(numerator));
			}
		}

		// always append static ones, otherwise it looks weird when scrolling
		// also, must be last to prevent empty spaces (still,
		for (IPageLinkFactory factory : staticPageFactories)
			pages.add(factory.createPage(numerator));

		return pages;
	}

	@Override
	public int getSearchBarAndScrollStartY() {
		return 96;
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
	public void renderForeground(GuiWiki gui, int mouseX, int mouseY) {
		if (tabIcon != null) {
			if (tabIcon.getItem() instanceof ItemBlock) {
				gui.renderRotatingBlockIntoGUI(gui, tabIcon, 55, 33, 2.8F);
				return;
			}

			GL11.glPushMatrix();
			GL11.glTranslated(49, 20, 0);
			GL11.glScaled(2.2, 2.2, 2.2);
			renderer.renderItemAndEffectIntoGUI(gui.getFontRenderer(), gui.mc.getTextureManager(), tabIcon, 0, 0);
			GL11.glPopMatrix();
		}
	}

	private static ItemStack createIconItemStack() {
		return new ItemStack(Objects.firstNonNull(OpenBlocksItemHolder.flag, Blocks.sponge));
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return createIconItemStack();
	}

	@Override
	public void onPageChange(GuiWiki gui, String pageName, Object... metadata) {
		if (metadata.length > 0 && metadata[0] instanceof ItemStack) {
			tabIcon = (ItemStack)metadata[0];
		} else if (metadata.length == 0) {
			tabIcon = createIconItemStack();
		}
	}

	@Override
	public void renderBackground(GuiWiki gui, int mouseX, int mouseY) {}

	@Override
	public void onMouseClick(GuiWiki gui, int mouseX, int mouseY, int mouseKey) {}
}
