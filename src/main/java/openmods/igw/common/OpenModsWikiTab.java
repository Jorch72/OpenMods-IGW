package openmods.igw.common;

import igwmod.gui.GuiWiki;
import igwmod.gui.IPageLink;
import igwmod.gui.IReservedSpace;
import igwmod.gui.LocatedStack;
import igwmod.gui.LocatedString;
import igwmod.gui.tabs.IWikiTab;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import openmods.Log;

import cpw.mods.fml.client.FMLClientHandler;

public abstract class OpenModsWikiTab implements IWikiTab {

	private static RenderItem renderer = new RenderItem();

	private ItemStack tabIcon;
	private Entity tabEntity;

	private static class LinkNumerator {
		private int staticEntries;
		private int itemEntries;

		private int getNextStaticId() {
			return this.staticEntries++;
		}

		private int getNextItemId() {
			return this.itemEntries++;
		}
	}

	private interface IPageLinkFactory {
		IPageLink createPage(final LinkNumerator numerator);
	}

	protected static IPageLinkFactory createStaticPageFactory(final String id, final OpenModsWikiTab tab) {
		return new IPageLinkFactory() {
			@Override
			public IPageLink createPage(final LinkNumerator numerator) {
				final String pageName = tab.getPageName().endsWith(".")? tab.getPageName() : (tab.getPageName() + ".");
				final String localizedLinkName = StatCollector.translateToLocal(pageName + id);
				return new LocatedString(localizedLinkName,
						80,
						125 + 11 * numerator.getNextStaticId(),
						false,
						"openmods-igw:tab/" + id);
			}
		};
	}

	protected static IPageLinkFactory createItemPageFactory(final String location, final ItemStack itemStack) {
		return new IPageLinkFactory() {
			@Override
			public IPageLink createPage(final LinkNumerator numerator) {
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

	protected void addPageToStaticPages(final IPageLinkFactory factory) {
		this.staticPageFactories.add(factory);
	}

	private final List<IPageLinkFactory> staticPageFactories = Lists.newArrayList();

	private final List<IPageLinkFactory> itemPageFactories = Lists.newArrayList();

	private final Map<String, ItemStack> defaultIcons;
	private final Map<String, Class<? extends Entity>> defaultEntities;

	public OpenModsWikiTab(final List<Pair<String, ItemStack>> stacks,
							 final Map<String, ItemStack> allClaimedPages,
							 final Map<String, Class<? extends Entity>> allClaimedEntities) {

		for (final Pair<String, ItemStack> e : stacks)
			this.itemPageFactories.add(createItemPageFactory(e.getLeft(), e.getRight()));

		this.defaultIcons = allClaimedPages;
		this.defaultEntities = allClaimedEntities;
	}

	@Override
	public final String getName() {
		return this.getTabName();
	}

	public abstract String getTabName();

	/*
	 * Dot at the end is automatically appended
	 */
	public abstract String getPageName();

	@Override
	public abstract List<IReservedSpace> getReservedSpaces();

	@Override
	public List<IPageLink> getPages(final int[] pageIndexes) {
		final List<IPageLink> pages = Lists.newArrayList();
		final LinkNumerator numerator = new LinkNumerator();
		if (pageIndexes == null) {
			for (final IPageLinkFactory factory : itemPageFactories)
				pages.add(factory.createPage(numerator));
		}
		else {
			for (final int index : pageIndexes) {
				if (index >= itemPageFactories.size()) break;
				final IPageLinkFactory factory = itemPageFactories.get(index);
				pages.add(factory.createPage(numerator));
			}
		}

		// always append static ones, otherwise it looks weird when scrolling
		// also, must be last to prevent empty spaces (still,
		for (final IPageLinkFactory factory : staticPageFactories)
			pages.add(factory.createPage(numerator));

		return pages;
	}

	@Override
	public abstract int getSearchBarAndScrollStartY();

	@Override
	public abstract int pagesPerTab();

	@Override
	public abstract int pagesPerScroll();

	@Override
	public void renderForeground(final GuiWiki gui, final int mouseX, final int mouseY) {
		if (this.tabIcon == null) return;

		if (this.tabIcon.getItem() instanceof ItemBlock) {
			gui.renderRotatingBlockIntoGUI(gui, this.tabIcon, 55, 33, 2.8F);
			return;
		}

		GL11.glPushMatrix();
		GL11.glTranslated(49, 20, 0);
		GL11.glScaled(2.2, 2.2, 2.2);
		renderer.renderItemAndEffectIntoGUI(gui.getFontRenderer(), gui.mc.getTextureManager(), this.tabIcon, 0, 0);
		GL11.glPopMatrix();
	}

	private static<T> T firstNonNull(final T[] list) {
		for (final T obj : list) {

			if (obj != null) return obj;
		}

		return null;
	}

	protected final ItemStack createFallbackItemStack() {
		return new ItemStack(OpenModsWikiTab.firstNonNull(this.getCandidates()));
	}

	private Item[] getCandidates() {
		final List<Item> toArray = Lists.newArrayList();

		if (this.getItemCandidates() != null) toArray.addAll(Arrays.asList(this.getItemCandidates()));

		if (this.getBlockCandidates() != null) {

			for (final Block block : this.getBlockCandidates()) {

				toArray.add(Item.getItemFromBlock(block));
			}
		}

		toArray.add(Items.compass);

		return toArray.toArray(new Item[toArray.size()]);
	}

	protected Item[] getItemCandidates() {
		return null;
	}

	protected Block[] getBlockCandidates() {
		return null;
	}

	private static Entity getEntity(final Class<? extends Entity> clazz) {
		try {
			return clazz.getConstructor(net.minecraft.world.World.class)
					.newInstance(FMLClientHandler.instance().getClient().theWorld);
		} catch (ReflectiveOperationException e) {
			Log.warn(e, "The entity %s does not have a constructor with a single world parameter.", clazz);
			return null;
		}
	}

	@Override
	public final ItemStack renderTabIcon(final GuiWiki gui) {
		return this.createFallbackItemStack();
	}

	@Override
	public void onPageChange(final GuiWiki gui, final String pageName, final Object... metadata) {
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

		this.tabIcon = stack;
		this.tabEntity = entity;
	}

	@Override
	public void renderBackground(final GuiWiki gui, final int mouseX, final int mouseY) {
		if (this.tabEntity == null) return;

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
		GL11.glTranslatef(0.0F, this.tabEntity.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(this.tabEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void onMouseClick(final GuiWiki gui, final int mouseX, final int mouseY, final int mouseKey) {}
}
