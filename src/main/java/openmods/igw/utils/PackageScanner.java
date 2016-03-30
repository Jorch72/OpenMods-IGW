package openmods.igw.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public final class PackageScanner {

	private PackageScanner() { }

	private static final PackageScanner SINGLETON = new PackageScanner();
	private final List<Package> packages = Lists.newArrayList();
	private final List<Class<?>> cache = Lists.newArrayList();

	public static PackageScanner get() {
		return SINGLETON;
	}

	public boolean registerPackage(@Nonnull final String packName) {

		openmods.Log.info("Attempting registration of package " + packName);

		Package pkg = Package.getPackage(packName);

		if (pkg == null && !packName.endsWith(".")) pkg = Package.getPackage(packName + ".");

		return pkg != null && !this.packages.contains(pkg) && this.packages.add(pkg) && this.invalidate();
	}

	private boolean invalidate() {

		openmods.Log.info("Registration successful");
		cache.clear();
		return true;
	}

	public List<Class<?>> scan() {

		openmods.Log.info("Scanning classes...");

		if (!this.cache.isEmpty()) return this.cache;

		final List<Class<?>> classes = Lists.newArrayList();

		for (final Package pkg : this.packages) {

			final String name = pkg.getName().replace('.', '/');
			final URL url = this.getClass().getClassLoader().getResource(name);

			if (url == null) continue; // Invalid package name. How?

			final File dir = new File(url.getFile());
			final File[] contents = dir.listFiles();

			if (contents == null) continue; // Empty package, I guess.

			for (final File classFile : contents) {

				if (classFile.isDirectory()) continue; // Sub-packages must be registered separately

				final String res = name + "." + classFile.getName();

				if (!res.endsWith(".class")) continue; // Not a (compiled) Java class.

				final String className = res.substring(0, res.length() - 6).replace('/', '.');

				try {
					classes.add(Class.forName(className));
				} catch (final ClassNotFoundException e) {
					openmods.Log.warn(e, "Class %s could not be loaded. Please check the file.", className);
				}
			}
		}

		this.cache.addAll(classes);

		return classes;
	}

	@SuppressWarnings("WeakerAccess")
	public Map<Class<?>, List<Annotation>> getAnnotationForClasses(final List<Class<?>> classes) {

		final Map<Class<?>, List<Annotation>> map = Maps.newHashMap();

		for (final Class<?> clazz : classes) {

			final List<Annotation> classAnnotations = Lists.newArrayList();

			Collections.addAll(classAnnotations, clazz.getDeclaredAnnotations());

			map.put(clazz, classAnnotations);
		}

		return map;
	}

	public List<Class<?>> scanForAnnotations(@Nonnull final Class<?>... annotationClasses) {

		final List<Class<?>> annotatedClasses = Lists.newArrayList();
		final List<Class<?>> classes = this.scan();
		final Map<Class<?>, List<Annotation>> annotationsMap = this.getAnnotationForClasses(classes);

		for (final Map.Entry<Class<?>, List<Annotation>> entry : annotationsMap.entrySet()) {

			for (final Class<?> annotationClass : annotationClasses) {

				@SuppressWarnings("unchecked")
				//@Explain("How can I avoid this???")
				final Class<? extends Annotation> annotationClazz = (Class<? extends Annotation>) annotationClass;
				final Annotation annotation = entry.getKey().getAnnotation(annotationClazz);
				if (annotation != null) annotatedClasses.add(entry.getKey());
			}
		}

		return annotatedClasses;
	}
}
