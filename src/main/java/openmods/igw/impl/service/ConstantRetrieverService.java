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
package openmods.igw.impl.service;

import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.service.IConstantRetrieverService;
import openmods.igw.api.service.IService;
import openmods.igw.impl.config.Config;
import openmods.igw.impl.utils.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ConstantRetrieverService implements IConstantRetrieverService {

    private static final ConstantRetrieverService IT = new ConstantRetrieverService();

    private ConstantRetrieverService() {}

    @Nonnull
    public static ConstantRetrieverService get() {
        return IT;
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> ConfigConstantWrapper<T> getConfigConstant(@Nonnull final String name) {
        try {
            final Class<?> clazz = Config.class;
            for (final Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isPublic(field.getModifiers())) continue;
                if (!Modifier.isStatic(field.getModifiers())) continue;
                if (field.getName().equals(name)) {
                    try {
                        return new ConfigConstantWrapper<T>((T) field.get(null));
                    } catch (final Exception ignored) {
                        // Try with another one
                    }
                }
            }
            throw new NoSuchFieldException(name);
        } catch (final Exception e) {
            OpenModsIGWApi.get().log().warning(e, "Exception thrown while attempting to retrieve configuration constant " + name);
            return new ConfigConstantWrapper<T>(null);
        }
    }

    @Nonnull
    @Override
    public ConfigConstantWrapper<Boolean> getBooleanConfigConstant(@Nonnull final String name) {
        try {
            final ConfigConstantWrapper<?> it = this.getConfigConstant(name);
            if (!it.isPresent()) throw new IllegalArgumentException("Specified value not found in config file");
            if (!(it.get() instanceof Boolean)) {
                throw new IllegalArgumentException(
                        "Specified value is not a valid boolean config property",
                        new ClassCastException(String.format("Cannot cast %s to Boolean", it.get().getClass().getName()))
                );
            }
            return new ConfigConstantWrapper<Boolean>((Boolean) it.get());
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(
                    "Unexpected exception in ConstantRetrieverService",
                    new IllegalStateException("Invalid service state: invalid argument passed in", e)
            );
        }
    }

    @Override
    public boolean isEnabled(@Nonnull final String modId) {
        return Config.isEnabled(modId);
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> ConfigConstantWrapper<T> getConstant(@Nonnull final String name) {
        try {
            final Class<?> clazz = Constants.class;
            for (final Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isPublic(field.getModifiers())) continue;
                if (!Modifier.isStatic(field.getModifiers())) continue;
                if (!Modifier.isFinal(field.getModifiers())) continue;
                if (field.getName().equals(name)) {
                    try {
                        return new ConfigConstantWrapper<T>((T) field.get(null));
                    } catch (final Exception ignored) {
                        // Try with another one
                    }
                }
            }
            throw new NoSuchFieldException(name);
        } catch (final Exception e) {
            OpenModsIGWApi.get().log().warning(e, "Exception thrown while attempting to retrieve constant " + name);
            return new ConfigConstantWrapper<T>(null);
        }
    }

    @Nonnull
    @Override
    public IConstantRetrieverService cast() {
        return this;
    }

    @Override
    public void onRegisterPre(@Nullable final IService<IConstantRetrieverService> previous) {}

    @Override
    public void onRegisterPost() {}

    @Override
    public void onUnregister() {}
}
