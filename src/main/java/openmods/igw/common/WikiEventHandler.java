package openmods.igw.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import igwmod.api.PageChangeEvent;
import igwmod.api.VariableRetrievalEvent;

import openmods.igw.config.Config;
import openmods.igw.utils.PackageScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class WikiEventHandler {

	private final Map<Class<?>, Map<Method, String>> handlers = Maps.newHashMap();

	public WikiEventHandler() {

		final List<Class<?>> classes = Lists.newArrayList();

		if (Config.useUniqueWikiTab) {
			PackageScanner.get().registerPackage("openmods.igw.common");
		} else {
			PackageScanner.get().registerPackage("openmods.igw.openblocks");
			// Insert other packages here
		}
		classes.addAll(PackageScanner.get().scanForAnnotations(CustomHandler.class));

		for (final Class<?> clazz : classes) {

			final Map<Method, String> map = Maps.newHashMap();

			for (final Method method : clazz.getDeclaredMethods()) {

				for (final Annotation annotation : method.getDeclaredAnnotations()) {

					if (!(annotation instanceof CustomHandler.HandlerMethod)) continue;

					final CustomHandler.HandlerMethod handlerMethod = (CustomHandler.HandlerMethod) annotation;
					map.put(method, handlerMethod.event());

					//break;
				}
			}

			this.handlers.put(clazz, map);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unused")
	//@Explain("Called by Forge")
	public void onWikiPageChanged(final PageChangeEvent event) {

		for (final Map.Entry<Class<?>, Map<Method, String>> handler : this.handlers.entrySet()) {

			for (final Map.Entry<Method, String> entry : handler.getValue().entrySet()) {

				if (!entry.getValue().equals(CustomHandler.HandlerMethod.Events.PAGE_OPENED)) continue;

				try {
					entry.getKey().invoke(handler.getKey().newInstance(), event);
				} catch (final Exception e) {
					openmods.Log.severe(e, "Exception while posting event %s to class %s, method %s",
							event.toString(),
							handler.getKey(),
							entry.getKey());
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unused")
	//@Explain("Called by Forge")
	public void onVariableRetrieval(final VariableRetrievalEvent event) {

		for (final Map.Entry<Class<?>, Map<Method, String>> handler : this.handlers.entrySet()) {

			for (final Map.Entry<Method, String> entry : handler.getValue().entrySet()) {

				if (!entry.getValue().equals(CustomHandler.HandlerMethod.Events.VARIABLE_SUBSTITUTION)) continue;

				try {
					entry.getKey().invoke(handler.getKey().newInstance(), event);
				} catch (final Exception e) {
					openmods.Log.severe(e, "Exception while posting event %s to class %s, method %s",
							event.toString(),
							handler.getKey(),
							entry.getKey());
				}
			}
		}
	}
}
