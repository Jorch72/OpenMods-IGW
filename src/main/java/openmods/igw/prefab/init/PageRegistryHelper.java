package openmods.igw.prefab.init;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import igwmod.api.WikiRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import openmods.api.VisibleForDocumentation;
import openmods.igw.api.init.IPageRegisterer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

/**
 * Helper class used to register the various pages to the
 * internal registry of IGW-Mod.
 *
 * @since 1.0
 */
public final class PageRegistryHelper implements IPageRegisterer {

	/**
	 * Functional interface used to provide callbacks to the various methods.
	 *
	 * @param <T>
	 *     	The object's type.
	 *
	 * @since 1.0
	 */
	//@FunctionalInterface // Needed to implement lambdas (and we are not in Java 8...)
	private interface Callback<T> {
		void call(final String modId, final String name, final T object);
	}

	/**
	 * Not to be confused with {@link openmods.igw.prefab.record.mod.ModEntry},
	 * represents an entry for a specific mod which holds all the various blocks,
	 * items and entities registered by that specific mod.
	 *
	 * @since 1.0
	 */
	private static class ModEntry {
		public final Map<String, Item> items = Maps.newHashMap();
		public final Map<String, Block> blocks = Maps.newHashMap();
		public final Map<String, Class<? extends Entity>> entities = Maps.newHashMap();
	}

	private final Map<String, ModEntry> mods = Maps.newHashMap();

	private final Map<String, ItemStack> claimedPages = Maps.newHashMap();
	private final Map<String, Class<? extends Entity>> claimedEntities = Maps.newHashMap();

	@Override
	public void loadItems() {
		// When lambdas would be extremely useful...
		this.iterateGameData(GameData.getBlockRegistry(), new Callback<Block>() {
			@Override
			public void call(final String modId, final String name, final Block object) {
				getOrCreateModEntry(modId).blocks.put(name, object);
			}
		});

		this.iterateGameData(GameData.getItemRegistry(), new Callback<Item>() {
			@Override
			public void call(final String modId, final String name, final Item object) {
				getOrCreateModEntry(modId).items.put(name, object);
			}
		});

		this.iterateEntities(new Callback<Class<? extends Entity>>() {
			@Override
			public void call(final String modId, final String name, final Class<? extends Entity> object) {
				getOrCreateModEntry(modId).entities.put(name, object);
			}
		});
	}

	@Nonnull
	@Override
	@SuppressWarnings("MethodCallSideOnly")
	public List<Pair<String, ItemStack>> claimModObjects(final String modId) {
		final ModEntry entry = this.mods.get(modId);
		if (entry == null) return Lists.newArrayList();

		final List<Pair<String, ItemStack>> results = Lists.newArrayList();

		final Set<String> blockIds = Sets.newHashSet();

		for (final Map.Entry<String, Block> blockEntry : entry.blocks.entrySet()) {
			final String blockId = blockEntry.getKey();
			blockIds.add(blockId);

			final Block block = blockEntry.getValue();
			final Item blockItem = Item.getItemFromBlock(block);

			final List<ItemStack> stacks = Lists.newArrayList();
			blockItem.getSubItems(blockItem, CreativeTabs.SEARCH, stacks);

			for (final ItemStack stack : stacks) {
				final String page = "openmods-igw:block/" + StringUtils.removeStart(stack.getUnlocalizedName(), "tile.");
				this.claimPage(stack, page);
				results.add(Pair.of(page, stack));
			}
		}

		for (final Map.Entry<String, Item> itemEntry : entry.items.entrySet()) {
			final String itemId = itemEntry.getKey();
			if (blockIds.contains(itemId)) continue;

			final Item item = itemEntry.getValue();

			final List<ItemStack> stacks = Lists.newArrayList();
			item.getSubItems(item, CreativeTabs.SEARCH, stacks);

			for (final ItemStack stack : stacks) {
				final String page = "openmods-igw:item/" + StringUtils.removeStart(stack.getUnlocalizedName(), "item.");
				this.claimPage(stack, page);
				results.add(Pair.of(page, stack));
			}
		}

		return results;
	}

	@Nonnull
	@Override
	public List<Pair<String, String>> claimEntities(final String modId) {
		final List<Pair<String, String>> result = Lists.newArrayList();

		final ModEntry entry = this.mods.get(modId);
		if (entry == null) return result;

		for (final Map.Entry<String, Class<? extends Entity>> e : entry.entities.entrySet()) {
			final Class<? extends Entity> cls = e.getValue();
			if (cls.isAnnotationPresent(VisibleForDocumentation.class)) {
				final String pageId = "openmods-igw:entity/" + e.getKey();
				WikiRegistry.registerEntityPageEntry(cls, pageId);
				result.add(Pair.of(pageId, cls.getCanonicalName()));
				if (!this.claimedEntities.containsKey(pageId)) this.claimedEntities.put(pageId, cls);
			}
		}

		return result;
	}

	@Nonnull
	@Override
	public Map<String, ItemStack> getAllClaimedPages() {
		return ImmutableMap.copyOf(claimedPages);
	}

	@Nonnull
	@Override
	public Map<String, Class<? extends Entity>> getAllClaimedEntitiesPages() {
		return ImmutableMap.copyOf(this.claimedEntities);
	}

	private <T extends IForgeRegistryEntry<T>> void iterateGameData(final FMLControlledNamespacedRegistry<T> registry,
                                                                    final Callback<T> callback) {
		final Set<ResourceLocation> ids = registry.getKeys();
		final Splitter splitter = Splitter.on(':');

		for (final ResourceLocation id : ids) {
			final T obj = registry.getObject(id);
			if (obj == null) continue;

			/*
			final Iterator<String> components = splitter.split(id).iterator();
			final String modId = components.next();
			final String name = components.next();
			*/
			// FIXME Actually test if this works
			final String modId = id.getResourceDomain();
			final String name = id.getResourcePath();

			callback.call(modId, name, obj);
		}
	}

	private void iterateEntities(final Callback<Class<? extends Entity>> callback) {
		final Set<Map.Entry<Class<? extends Entity>, String>> entrySet = EntityList.CLASS_TO_NAME.entrySet();
		for (final Map.Entry<Class<? extends Entity>, String> entry : entrySet) {
			final Class<? extends Entity> cls = entry.getKey();
			final EntityRegistry.EntityRegistration entityEntry = EntityRegistry.instance().lookupModSpawn(cls, false);
			if (entityEntry != null) {
				callback.call(entityEntry.getContainer().getModId(), entry.getValue(), cls);
			}
		}
	}

	@Nonnull
	private ModEntry getOrCreateModEntry(final String modId) {
		ModEntry result = this.mods.get(modId);
		if (result == null) {
			result = new ModEntry();
			this.mods.put(modId, result);
		}
		return result;
	}

	private void claimPage(final ItemStack stack, final String page) {
		WikiRegistry.registerBlockAndItemPageEntry(stack, page);
		if (!this.claimedPages.containsKey(page)) this.claimedPages.put(page, stack);
	}
}
