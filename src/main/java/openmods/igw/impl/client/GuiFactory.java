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
package openmods.igw.impl.client;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import openmods.config.gui.OpenModsConfigScreen;
import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.service.IConstantRetrieverService;
import openmods.igw.api.service.IService;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class GuiFactory implements IModGuiFactory {

    public static class ConfigGui extends OpenModsConfigScreen {

        public ConfigGui(final GuiScreen parent) {
            super(parent,
                    constant("MOD_ID"),
                    constant("NAME"));
        }

        @Nullable // Dem static hax tho
        private static String constant(@Nonnull final String name) {
            final Optional<IService<IConstantRetrieverService>> service = OpenModsIGWApi.get()
                    .obtainService(IConstantRetrieverService.class);
            if (!service.isPresent()) {
                throw new RuntimeException(new IllegalStateException("Constant retriever service unavailable"));
            }
            return service.get().cast().<String>getConstant(name).orNull();
        }
    }

    @Override
    public void initialize(final Minecraft mcInstance) {}

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GuiFactory.ConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return Sets.newHashSet();
    }

    @Override
    @SuppressWarnings("deprecation") // How else? Considering I have to implement this method
    public RuntimeOptionGuiHandler getHandlerFor(final RuntimeOptionCategoryElement element) {
        return null;
    }
}
