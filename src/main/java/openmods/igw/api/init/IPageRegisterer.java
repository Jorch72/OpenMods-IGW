package openmods.igw.api.init;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Identifies a class which provides registration methods for
 * a specific wiki page and/or tab.
 * 
 * <p>The implementation of this class is not encouraged. It is
 * suggested to use the prefab provided instead. This because
 * the whole API relies around it and using a different
 * implementation may lead to undefined behaviour.</p>
 * 
 * @since 1.0
 */
public interface IPageRegisterer {

	/**
	 * Loads all the various blocks, items and entities in order to
	 * register them in IGW-Mod registry.
	 *
	 * @since 1.0
	 */
	void loadItems();

	/**
	 * Claims the objects for the specific mod id, registering them
	 * in IGW-Mod registry and adding the various objects' pages
	 * to OpenMods-IGW namespace.
	 *
	 * @param modId
	 * 		The mod id.
	 * @return
	 * 		A list containing all the objects claimed.
	 *
	 * @since 1.0
	 */
	@Nonnull
	List<Pair<String, ItemStack>> claimModObjects(final String modId); // TODO Wrapper for ItemStack

	/**
	 * Claims the entities for the specific mod id, registering them
	 * in IGW-Mod registry and adding the various entities' pages
	 * to OpenMods-IGW namespace.
	 *
	 * @param modId
	 * 		The mod id.
	 * @return
	 * 		A list containing all the objects claimed.
	 *
	 * @since 1.0
	 */
	@Nonnull
	List<Pair<String, String>> claimEntities(final String modId);

	/**
	 * Gets all the pages currently claimed by IGW-Mod.
	 *
	 * <p>Implementations can decide to return these either as
	 * a modifiable map or an immutable one. In case the former
	 * is chosen, though, they must state it somewhere (e.g. in
	 * the Javadoc).</p>
	 *
	 * @return
	 * 		The pages currently claimed.
	 *
	 * @since 1.0
	 */
	@Nonnull
	Map<String, ItemStack> getAllClaimedPages();

	/**
	 * Gets all the entity pages currently claimed by IGW-Mod.
	 *
	 * <p>Implementations can decide to return these either as
	 * a modifiable map or an immutable one. In case the former
	 * is chosen, though, they must state it somewhere (e.g. in
	 * the Javadoc).</p>
	 *
	 * @return
	 * 		The entity pages currently claimed.
	 *
	 * @since 1.0
	 */
	@Nonnull
	Map<String, Class<? extends Entity>> getAllClaimedEntitiesPages();
}
