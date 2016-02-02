package openmods.igw.openblocks;

import igwmod.gui.GuiWiki;
import igwmod.gui.IPageLink;
import igwmod.gui.IReservedSpace;
import igwmod.gui.LocatedStack;
import igwmod.gui.LocatedString;
import igwmod.gui.LocatedTexture;
import igwmod.gui.tabs.IWikiTab;

import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import openmods.Log;

import cpw.mods.fml.client.FMLClientHandler;

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

	private ItemStack tabIcon;
	private Entity tabEntity;

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

	private final Map<String, ItemStack> defaultIcons;
	private final Map<String, Class<? extends Entity>> defaultEntities;

	public OpenBlocksWikiTab(final List<Pair<String, ItemStack>> stacks,
							 final Map<String, ItemStack> allClaimedPages,
							 final Map<String, Class<? extends Entity>> allClaimedEntities) {
		staticPageFactories.add(createStaticPageFactory("about"));
		staticPageFactories.add(createStaticPageFactory("credits"));
		staticPageFactories.add(createStaticPageFactory("obUtils"));
		staticPageFactories.add(createStaticPageFactory("bKey"));
		staticPageFactories.add(createStaticPageFactory("enchantments"));
		staticPageFactories.add(createStaticPageFactory("changelogs"));

		for (Pair<String, ItemStack> e : stacks)
			itemPageFactories.add(createItemPageFactory(e.getLeft(), e.getRight()));

		this.defaultIcons = allClaimedPages;
		this.defaultEntities = allClaimedEntities;
	}

	@Override
	public String getName() {
		return "wiki.openblocks.tab";
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
		if (tabIcon == null) return;

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

	private static ItemStack createFallbackItemStack() {
		return new ItemStack(Objects.firstNonNull(OpenBlocksItemHolder.flag, Blocks.sponge));
	}

	private static Entity getEntity(Class<? extends Entity> clazz) {
		try {
			return clazz.getConstructor(net.minecraft.world.World.class)
					.newInstance(FMLClientHandler.instance().getClient().theWorld);
		} catch (ReflectiveOperationException e) {
			Log.warn(e, "The entity %s does not have a constructor with a single world parameter.", clazz);
			return null;
		}
	}

	@Override
	public ItemStack renderTabIcon(GuiWiki gui) {
		return createFallbackItemStack();
	}

	@Override
	public void onPageChange(GuiWiki gui, String pageName, Object... metadata) {
		final ItemStack stack;
		final Entity entity;

		if (metadata.length > 0 && metadata[0] instanceof ItemStack) {
			stack = (ItemStack)metadata[0];
			entity = null;
		} else if (metadata.length > 0 && metadata[0] instanceof Entity) {
			entity = getEntity(((Entity)metadata[0]).getClass());
			stack = entity == null? createFallbackItemStack() : null;
		} else if (metadata.length == 0) {
			final ItemStack defaultStack = defaultIcons.get(pageName);
			final Class<? extends Entity> defaultEntity = this.defaultEntities.get(pageName);
			stack = defaultStack != null? defaultStack : defaultEntity != null? null : createFallbackItemStack();
			entity = stack == null? getEntity(defaultEntity) : null;
		} else {
			Log.warn("Reached fallback code. This should never happen!");
			stack = createFallbackItemStack();
			entity = null;
		}

		tabIcon = stack;
		tabEntity = entity;
	}

	@Override
	public void renderBackground(GuiWiki gui, int mouseX, int mouseY) {
		if (tabEntity == null) return;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		final float maxHitBox = Math.max(1, Math.max(tabEntity.width, tabEntity.height));
		final int scale = (int)(40 * 0.7F / maxHitBox);
		final float x = gui.getGuiLeft() + 65;
		final float y = gui.getGuiTop() + 49;

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 50.0F);
		GL11.glScalef(-scale, scale, scale);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-igwmod.TickHandler.ticksExisted, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, tabEntity.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(tabEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void onMouseClick(GuiWiki gui, int mouseX, int mouseY, int mouseKey) {}
}
