/**
 * Contains some basic implementations of services which
 * should be used as fallbacks.
 *
 * <p>E.g.: some services may require logging during their
 * initialization, but the logging service may not be ready
 * yet. As such, services in this package are used as a
 * fallback.</p>
 *
 * @author TheSilkMiner
 * @since 1.0
 */
@cpw.mods.fml.common.API(apiVersion = "1.0", owner = "OpenMods-IGW", provides = "OpenMods-IGW|Prefab|Service|Fallback")
package openmods.igw.prefab.service.fallback;