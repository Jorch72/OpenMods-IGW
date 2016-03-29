package openmods.igw.utils;

import igwmod.api.WikiRegistry;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import openmods.api.VisibleForDocumentation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;

@SuppressWarnings("unused")
public class PageRegistryHelper {

	private interface Callback<T> {

		void call(String modId, String name, T object);
	}

	private static <T> void iterateGameData(final FMLControlledNamespacedRegistry<T> registry,
											final Callback<T> callback) {

		@SuppressWarnings("unchecked")
		Set<String> ids = registry.getKeys();

		final Splitter splitter = Splitter.on(':');

		for (String id : ids) {
			final T obj = registry.getObject(id);
			if (obj == null) continue;

			Iterator<String> components = splitter.split(id).iterator();
			final String modId = components.next();
			final String name = components.next();

			callback.call(modId, name, obj);
		}
	}

	private static class ModEntry {
		public final Map<String, Item> items = Maps.newHashMap();
		public final Map<String, Block> blocks = Maps.newHashMap();
		public final Map<String, Class<? extends Entity>> entities = Maps.newHashMap();
	}

	private final Map<String, ModEntry> mods = Maps.newHashMap();

	private final Map<String, ItemStack> claimedPages = Maps.newHashMap();
	private final Map<String, Class<? extends Entity>> claimedEntities = Maps.newHashMap();

	private ModEntry getOrCreateModEntry(final String modId) {
		ModEntry result = mods.get(modId);
		if (result == null) {
			result = new ModEntry();
			mods.put(modId, result);
		}
		return result;
	}

	public void loadItems() {
		iterateGameData(GameData.getBlockRegistry(), new Callback<Block>() {
			@Override
			public void call(String modId, String name, Block object) {
				getOrCreateModEntry(modId).blocks.put(name, object);
			}
		});

		iterateGameData(GameData.getItemRegistry(), new Callback<Item>() {
			@Override
			public void call(String modId, String name, Item object) {
				getOrCreateModEntry(modId).items.put(name, object);
			}
		});

		iterateEntities(new Callback<Class<? extends Entity>>() {
			@Override
			public void call(String modId, String name, Class<? extends Entity> object) {
				getOrCreateModEntry(modId).entities.put(name, object);
			}
		});
	}

	private void iterateEntities(final Callback<Class<? extends Entity>> callback) {
		@SuppressWarnings("unchecked")
		final Set<Entry<Class<? extends Entity>, String>> entrySet = EntityList.classToStringMapping.entrySet();
		for (Map.Entry<Class<? extends Entity>, String> entry : entrySet) {
			final Class<? extends Entity> cls = entry.getKey();
			final EntityRegistration entityEntry = EntityRegistry.instance().lookupModSpawn(cls, false);
			if (entityEntry != null) {
				callback.call(entityEntry.getContainer().getModId(), entry.getValue(), cls);
			}
		}
	}

	public List<Pair<String, ItemStack>> claimModObjects(final String modId) {
		final ModEntry entry = mods.get(modId);
		if (entry == null) return null;

		final List<Pair<String, ItemStack>> results = Lists.newArrayList();

		final Set<String> blockIds = Sets.newHashSet();

		for (Map.Entry<String, Block> blockEntry : entry.blocks.entrySet()) {
			final String blockId = blockEntry.getKey();
			blockIds.add(blockId);

			final Block block = blockEntry.getValue();
			final Item blockItem = Item.getItemFromBlock(block);

			final List<ItemStack> stacks = Lists.newArrayList();
			blockItem.getSubItems(blockItem, CreativeTabs.tabAllSearch, stacks);

			for (ItemStack stack : stacks) {
				final String page = "openmods-igw:block/" + StringUtils.removeStart(stack.getUnlocalizedName(), "tile.");
				claimPage(stack, page);
				results.add(Pair.of(page, stack));
			}
		}

		for (Map.Entry<String, Item> itemEntry : entry.items.entrySet()) {
			final String itemId = itemEntry.getKey();
			if (blockIds.contains(itemId)) continue;

			final Item item = itemEntry.getValue();

			final List<ItemStack> stacks = Lists.newArrayList();
			item.getSubItems(item, CreativeTabs.tabAllSearch, stacks);

			for (ItemStack stack : stacks) {
				final String page = "openmods-igw:item/" + StringUtils.removeStart(stack.getUnlocalizedName(), "item.");
				claimPage(stack, page);
				results.add(Pair.of(page, stack));
			}
		}

		return results;
	}

	private void claimPage(final ItemStack stack, final String page) {
		WikiRegistry.registerBlockAndItemPageEntry(stack, page);
		if (!claimedPages.containsKey(page)) claimedPages.put(page, stack);
	}

	public Map<String, ItemStack> getAllClaimedPages() {
		return ImmutableMap.copyOf(claimedPages);
	}

	public List<Pair<String, String>> claimEntities(final String modId) {
		final List<Pair<String, String>> result = Lists.newArrayList();

		final ModEntry entry = mods.get(modId);
		if (entry == null) return result;

		for (Map.Entry<String, Class<? extends Entity>> e : entry.entities.entrySet()) {
			final Class<? extends Entity> cls = e.getValue();
			if (cls.isAnnotationPresent(VisibleForDocumentation.class)) {
				final String pageId = "openmods-igw:entity/" + e.getKey();
				WikiRegistry.registerEntityPageEntry(cls, pageId);
				result.add(Pair.of(pageId, cls.getCanonicalName()));
				if (!claimedEntities.containsKey(pageId)) claimedEntities.put(pageId, cls);
			}
		}

		return result;
	}

	public Map<String, Class<? extends Entity>> getAllClaimedEntitiesPages() {
		return ImmutableMap.copyOf(this.claimedEntities);
	}
}
