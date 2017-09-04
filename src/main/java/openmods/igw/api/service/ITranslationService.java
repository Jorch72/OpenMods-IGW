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
package openmods.igw.api.service;

import javax.annotation.Nonnull;

/**
 * Identifies a service used for translations.
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public interface ITranslationService extends IService<ITranslationService> {

    /**
     * Enum used to indicate whether to report a translation error
     * or not.
     *
     * <p>The use of this enum is strongly encouraged, over the
     * previously used contants {@link #REPORT_ERROR} and
     * {@link #IGNORE_ERROR}. You can use the {@link #fromBoolean(boolean)}
     * method to convert from one to the other system.</p>
     *
     * @author TheSilkMiner
     * @since 1.0
     */
    enum ErrorBehaviour {

        /**
         * Used to indicate that the error should be reported to the user
         * with a default message.
         *
         * @since 1.0
         */
        REPORT,
        /**
         * Used to indicate that the error should be silently suppressed.
         *
         * <p>The returned string is completely implementation-dependent.
         * Refer to {@link #translate(String, String, ErrorBehaviour)} for more information</p>
         *
         * @since 1.0
         */
        IGNORE;

        /**
         * Factory method that can be used to convert from a previously used
         * constant value (e.g., {@link #REPORT_ERROR}) to the correspondent
         * enum value.
         *
         * <p>This method is purely provided as a convenience one. You should
         * not use this in production code: before releasing a new version of
         * your software, please use enum constants directly.</p>
         *
         * @param b
         *         The boolean to convert. It should be either {@link #REPORT_ERROR}
         *         or {@link #IGNORE_ERROR}.
         * @return
         *         The enum constant which represents the given boolean.
         *         The contract guarantees the behaviour will be the same, but the
         *         actual implementation details are not enforced nor specified.
         *
         * @since 1.0
         */
        @Nonnull
        public static ErrorBehaviour fromBoolean(final boolean b) {
            return b? REPORT : IGNORE;
        }
    }

    /**
     * Indicates to report the error to the user, with
     * a default message.
     *
     * @deprecated Use {@link ErrorBehaviour#REPORT} instead.
     *
     * @since 1.0
     */
    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    boolean REPORT_ERROR = true;

    /**
     * Indicates to ignore translation errors, returning
     * the untranslated string.
     *
     * @deprecated Use {@link ErrorBehaviour#IGNORE} instead.
     *
     * @since 1.0
     */
    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    boolean IGNORE_ERROR = false;

    /**
     * Attempts to translate the given translation ID.
     *
     * <p>This method should add to the translation ID a default
     * mod ID, either visible to the end-user or only available
     * internally.</p>
     *
     * @param translationId
     *         The ID of the translation.
     * @return
     *         A String containing the translated keyword or the given
     *         {@code translationId} if none is available.
     *
     * @since 1.0
     */
    // TODO Default implementation (Java 8 needed)
    @Nonnull
    String translate(@Nonnull final String translationId);

    /**
     * Attempts to translate the given translation ID, prefixing it
     * with the given mod ID.
     *
     * @param modId
     *         The mod ID to use.
     * @param translationId
     *         The ID of the translation.
     * @return
     *         A String containing the translated keyword or the given
     *         {@code translationId} if none is available.
     *
     * @since 1.0
     */
    // TODO Default implementation (Java 8 needed)
    @Nonnull
    String translate(@Nonnull final String modId, @Nonnull final String translationId);

    /**
     * Attempts to translate the given translation ID, prefixing it
     * with the given mod ID and returning an error message if the
     * translation fails.
     *
     * @param modId
     *         The mod ID to use.
     * @param translationId
     *         The ID of the translation.
     * @param report
     *         Whether to return an error message or not.
     *         It should either be {@link #IGNORE_ERROR} or
     *         {@link #REPORT_ERROR}.
     * @return
     *         A String containing the translated keyword or the given
     *         {@code translationId} if none is available and the
     *         {@code report} flag is {@link #IGNORE_ERROR} or an error
     *         message if it is {@link #REPORT_ERROR}.
     *
     * @deprecated Use {@link #translate(String, String, ErrorBehaviour)} instead.
     *
     * @since 1.0
     */
    // TODO Default implementation (Java 8 needed)
    @Deprecated
    @Nonnull
    String translate(@Nonnull final String modId, @Nonnull final String translationId, final boolean report);

    /**
     * Attempts to translate the given translation ID, prefixing it
     * with the given mod ID and returning an error message if the
     * translation fails and the caller asks us to do so.
     *
     * @param modId
     *         The mod ID to use.
     * @param translationId
     *         The ID of the translation.
     * @param behaviour
     *         Whether to return an error message or not. Refer to the
     *         specific Javadoc for more information.
     * @return
     *         A String containing the translated keyword if no errors
     *         are encountered, otherwise a String respecting the
     *         specified {@code behaviour}. Implementation details are
     *         not discussed here, nor enforced.
     *
     * @since 1.0
     */
    @Nonnull
    String translate(@Nonnull final String modId, @Nonnull final String translationId, @Nonnull final ErrorBehaviour behaviour);
}
