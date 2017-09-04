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

import openmods.igw.api.record.mod.IMismatchingModEntry;
import openmods.igw.api.service.IGuiService;
import openmods.igw.api.service.IService;
import openmods.igw.impl.client.MismatchingVersionsGui;
import openmods.igw.impl.client.WarningGui;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings({"MethodCallSideOnly", "NewExpressionSideOnly"})
public final class GuiService implements IGuiService {

    private static boolean instanceCreated;

    GuiService() {
        if (instanceCreated) throw new RuntimeException(new IllegalStateException("Instance already created"));
        instanceCreated = true;
    }

    @Override
    public boolean shouldShow(@Nonnull final GUIs gui) {
        switch (gui) {
            case WARNING:
                return WarningGui.shallShow();
            case MISMATCHING_MODS:
                return MismatchingVersionsGui.shouldShow();
            default:
                throw new IllegalArgumentException("gui");
        }
    }

    @Override
    public void show(@Nonnull final GUIs gui) {
        switch (gui) {
            case WARNING:
                WarningGui.markShow();
                return;
            case MISMATCHING_MODS:
                MismatchingVersionsGui.show();
                return;
            default:
                throw new IllegalArgumentException("gui");
        }
    }

    @Nonnull
    @Override
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public <T> T construct(@Nonnull final GUIs gui, @Nullable final Object... parameters) {
        switch (gui) {
            case WARNING:
                return (T) new WarningGui();
            case MISMATCHING_MODS:
                return (T) new MismatchingVersionsGui((List<IMismatchingModEntry>) parameters[0]);
            default:
                throw new IllegalArgumentException("gui");
        }
    }

    @Nonnull
    @Override
    public IGuiService cast() {
        return this;
    }

    @Override
    public void onRegisterPre(@Nullable final IService<IGuiService> previous) {}

    @Override
    public void onRegisterPost() {}

    @Override
    public void onUnregister() {}
}
