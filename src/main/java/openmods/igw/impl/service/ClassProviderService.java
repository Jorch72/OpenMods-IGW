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

import openmods.igw.OpenModsIGW;
import openmods.igw.api.config.IConfig;
import openmods.igw.api.init.IInit;
import openmods.igw.api.proxy.IInitProxy;
import openmods.igw.api.service.IClassProviderService;
import openmods.igw.api.service.IService;
import openmods.igw.impl.config.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public final class ClassProviderService implements IClassProviderService {

    private static boolean instanceCreated;

    ClassProviderService() {
        if (instanceCreated) throw new RuntimeException(new IllegalStateException("Instance already created"));
        instanceCreated = true;
    }

    @Nonnull
    @Override
    public Object mainClass() {
        return this.mainClassAsInit();
    }

    @Nonnull
    @Override
    public IInit mainClassAsInit() {
        return OpenModsIGW.getInstance();
    }

    @Nullable
    @Override
    public IInitProxy proxy() {
        return OpenModsIGW.proxy();
    }

    @Nonnull
    @Override
    public IClassProviderService cast() {
        return this;
    }

    @Nonnull
    @Override
    public Class<? extends IConfig> config() {
        return Config.class;
    }

    @Override
    public void onRegisterPre(@Nullable final IService<IClassProviderService> previous) {}

    @Override
    public void onRegisterPost() {}

    @Override
    public void onUnregister() {}
}
