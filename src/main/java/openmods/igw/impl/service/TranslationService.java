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

import net.minecraft.client.resources.I18n;

import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.service.IService;
import openmods.igw.api.service.ITranslationService;
import openmods.igw.impl.utils.Constants;

import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility class representing the default translation service provided
 * by this mod.
 */
public final class TranslationService implements ITranslationService {

    private static final TranslationService IT = new TranslationService();

    private String defaultModId;

    private TranslationService() {}

    public static TranslationService get() {
        return IT;
    }

    @Nonnull
    @Override
    public String translate(@Nonnull final String translationId) {
        return this.translate(this.defaultModId, translationId);
    }

    @Nonnull
    @Override
    public String translate(@Nonnull final String modId, @Nonnull final String translationId) {
        return this.translate(modId, translationId, ErrorBehaviour.IGNORE);
    }

    @Deprecated
    @Nonnull
    @Override
    public String translate(@Nonnull final String modId, @Nonnull final String translationId, final boolean report) {
        return this.translate(modId, translationId, ErrorBehaviour.fromBoolean(report));
    }

    @Nonnull
    @Override
    @SuppressWarnings("MethodCallSideOnly") // Service is registered on both sides, but called only on the client
    public String translate(@Nonnull final String modId, @Nonnull final String translationId, @Nonnull final ErrorBehaviour behaviour) {
        final String translation = I18n.format(
                String.format("%s.%s", modId, translationId.toLowerCase(Locale.ENGLISH).trim())
        );
        if (behaviour == ErrorBehaviour.REPORT && translation.equals(translationId)) {
            try {
                return this.translate("service.translation.missing");
            } catch (final StackOverflowError error) {
                // In case of broken implementation, return untranslated string
                try {
                    OpenModsIGWApi.get().log().severe("Unable to translate internal error message. Broken implementation?");
                } catch (final StackOverflowError ignored) {
                    // Probably we have not enough stack space to log a message
                    // Simply fail silently and don't care about it
                }
                return "An error has occurred while trying to translate the given string: " + error.getMessage();
            }
        }
        return translation;
    }

    @Nonnull
    @Override
    public ITranslationService cast() {
        return this;
    }

    @Override
    public void onRegisterPre(@Nullable final IService<ITranslationService> previous) {}

    @Override
    public void onRegisterPost() {
        this.defaultModId = Constants.MOD_ID.toLowerCase().replace('-', '.');
    }

    @Override
    public void onUnregister() {
        this.defaultModId = null; // Free up memory
    }
}
