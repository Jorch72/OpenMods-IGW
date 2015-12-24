package openmods.igw.openblocks;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameRegistry;

import igwmod.gui.GuiWiki;
import igwmod.gui.IPageLink;
import igwmod.gui.IReservedSpace;
import igwmod.gui.LocatedStack;
import igwmod.gui.LocatedString;
import igwmod.gui.LocatedTexture;
import igwmod.gui.tabs.IWikiTab;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import openmods.Log;
import openmods.Mods;

import org.apache.commons.lang3.StringUtils;
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
/**
 * @author TheSilkMiner
 */
public final class OpenBlocksWikiTab implements IWikiTab {

	private static RenderItem renderer = new RenderItem();

	/*
	 * First string is unlocalized name, second is the internal name.
	 *
	 * Refer to OpenBlocks main class.
	 */
	private static Map<String, String> exceptions = Maps.newHashMap();

	static {
		renderer.setRenderManager(RenderManager.instance);

		exceptions.put("glasses.pencil", "pencilGlasses");
		exceptions.put("glasses.crayon", "crayonGlasses");
		exceptions.put("glasses.technicolor", "technicolorGlasses");
		exceptions.put("glasses.admin", "seriousGlasses");
		exceptions.put("crane_control", "craneControl");
		exceptions.put("crane_backpack", "craneBackpack");
		exceptions.put("xpbucket", "filledbucket"); //?
		exceptions.put("sleepingbag", "sleepingBag");
		exceptions.put("paintbrush", "paintBrush");
		exceptions.put("height_map", "heightMap");
		exceptions.put("empty_map", "emptyMap");
		exceptions.put("tasty_clay", "tastyClay");
		exceptions.put("golden_eye", "goldenEye");
		exceptions.put("info_book", "infoBook");
		exceptions.put("epic_eraser", "epicEraser");
		exceptions.put("OpenBlocks.xpjuice", "xpjuice"); // I guess not
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
		IPageLink createPage(LinkNumerator numerator);
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

	private final List<IPageLinkFactory> staticPageFactories = Lists.newArrayList();

	private final List<IPageLinkFactory> itemPageFactories = Lists.newArrayList();

	public OpenBlocksWikiTab(List<Pair<String, ItemStack>> stacks) {
		staticPageFactories.add(createStaticPageFactory("about"));
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

	private static String normalizeString(final String name) {
		String str = name;

		if (str.startsWith("openmods-igw:")) str = StringUtils.removeStart(str, "openmods-igw:");
		if (str.startsWith("block/")) str = StringUtils.removeStart(str, "block/");
		if (str.startsWith("item/")) str = StringUtils.removeStart(str, "item/");
		if (str.startsWith("openblocks.")) str = StringUtils.removeStart(str, "openblocks.");
		if (str.contains(".")) str = str.replace('.', ':');

		return str;
	}

	private static int normalizeMeta(final String meta) {
		// TODO Implement.
		return 0;
	}

	private boolean manageException(final String name, final int meta) {

		boolean isException = false;
		String key = "";

		for (Map.Entry<String, String> entry : exceptions.entrySet()) {
			if (entry.getKey().contains(name)) {
				isException = true;
				key = entry.getKey();
				Log.debug("Found exception: " + key);
			}
		}

		Log.debug(key);

		if(!isException || key == null || key.isEmpty()) return false;

		Item item = GameRegistry.findItem(Mods.OPENBLOCKS, exceptions.get(key));

		if (item == null) {
			Log.warn("Exception handling failed!");
			return false;
		}

		tabIcon = new ItemStack(item, 1, meta);

		return true;
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return createIconItemStack();
	}

	@Override
	public void onPageChange(GuiWiki gui, String pageName, Object... metadata) {
		ItemStack stack = null;

		Log.debug(pageName);

		if (metadata.length > 0 && metadata[0] instanceof ItemStack) {
			stack = (ItemStack)metadata[0];
		} else if (metadata.length == 0) {
			boolean wasItem = pageName.contains("item/");
			String name = normalizeString(pageName);
			int meta = 0;
			if (name.contains(":")) {
				final String[] split = name.split(Pattern.quote(":"));
				if (split.length != 2) {
					Log.warn("An error in the page name has been found.");
					Log.warn("Process can not continue.");
					tabIcon = createIconItemStack();
					return;
				}
				name = split[0];
				meta = normalizeMeta(split[1]);
			}

			Item item = null;
			if (!wasItem) {
				Block b = GameRegistry.findBlock(Mods.OPENBLOCKS, name);
				if (b == null) {
					Log.warn("Couldn't find specified block.");
					Log.warn("Attempting to search for an item");
					wasItem = true;
				}
				if (!wasItem) item = Item.getItemFromBlock(b);
			}
			if (wasItem) {
				item = GameRegistry.findItem(Mods.OPENBLOCKS, name);
				if (item == null) {
					Log.warn("Couldn't find specified item.");
					Log.warn("Make sure the selected item is correct.");
					Log.warn("Reverting back to default ItemStack");
				}
			}

			if (item == null) {
				if (!manageException(name, meta)) {
					stack = createIconItemStack();
				} else {
					Log.debug("Exception managed");
					return;
				}
			} else {
				stack = new ItemStack(item, 1, meta);
			}
		}

		tabIcon = stack;
	}

	@Override
	public void renderBackground(GuiWiki gui, int mouseX, int mouseY) {}

	@Override
	public void onMouseClick(GuiWiki gui, int mouseX, int mouseY, int mouseKey) {}
}
