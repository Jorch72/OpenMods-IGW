/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Open Mods
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package openmods.igw.prefab.init;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import igwmod.api.WikiRegistry;

import openmods.api.VisibleForDocumentation;
import openmods.igw.api.OpenModsIGWApi;
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
     *         The object's type.
     *
     * @since 1.0
     */
    //@FunctionalInterface
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
    @SuppressWarnings("WeakerAccess")
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
        this.iterateGameData(GameRegistry.findRegistry(Block.class), new Callback<Block>() {
            @Override
            public void call(final String modId, final String name, final Block object) {
                getOrCreateModEntry(modId).blocks.put(name, object);
            }
        });

        this.iterateGameData(GameRegistry.findRegistry(Item.class), new Callback<Item>() {
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

            if (blockItem == null) {
                OpenModsIGWApi.get().log().severe("ItemBlock 'null' for given block {}! THIS IS A SERIOUS ERROR!", block);
                continue;
            }

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

    private <T extends IForgeRegistryEntry<T>> void iterateGameData(final IForgeRegistry<T> registry,
                                                                    final Callback<T> callback) {
        final Set<ResourceLocation> ids = registry.getKeys();

        for (final ResourceLocation id : ids) {
            final T obj = registry.getValue(id);

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
