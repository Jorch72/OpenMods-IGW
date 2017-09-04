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
import javax.annotation.Nullable;

/**
 * Identifies a service used to log various messages.
 *
 * <p>The logging location is completely implementation dependent:
 * it could be on a file on an output stream or even nowhere. Every
 * user of this service should <strong>not</strong> make any
 * assumptions on the logging location.</p>
 *
 * <p>Log format is also not restricted by any contract. It is
 * suggested that implementations output the log message along
 * the logging level, but that is not mandatory.</p>
 *
 * @author TheSilkMiner
 * @since 1.0
 */
public interface ILoggingService extends IService<ILoggingService> {

    /**
     * Holds all the various possible logging levels that can be used
     * by the caller.
     *
     * @author TheSilkMiner
     * @since 1.0
     */
    enum Level {

        /**
         * Identifies a serious error message.
         *
         * <p>This error may prevent the application from continuing
         * the current operation or be a recoverable error.</p>
         *
         * @since 1.0
         */
        SEVERE,
        /**
         * Identifies a warning message.
         *
         * <p>Warning messages should be used when a condition may lead
         * to a future error.</p>
         *
         * @since 1.0
         */
        WARNING,
        /**
         * Identifies an info message.
         *
         * @since 1.0
         */
        INFO,
        /**
         * Identifies a debug message.
         *
         * <p>This is used to output more technical messages that can
         * be used to identify errors and/or normal program flow.</p>
         *
         * @since 1.0
         */
        DEBUG,
        /**
         * Identifies a less important debug message.
         *
         * <p>This is mainly used to capture the flow through an
         * application execution, hence the name "Trace".</p>
         *
         * @since 1.0
         */
        TRACE
    }

    /**
     * Logs a message at {@link Level#TRACE TRACE} level.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void trace(@Nonnull final String message, @Nullable final Object... data);

    /**
     * Logs a message at {@link Level#DEBUG DEBUG} level.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void debug(@Nonnull final String message, @Nullable final Object... data);

    /**
     * Logs a message at {@link Level#INFO INFO} level.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void info(@Nonnull final String message, @Nullable final Object... data);

    /**
     * Logs a message at {@link Level#WARNING WARNING} level.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void warning(@Nonnull final String message, @Nullable final Object... data);

    /**
     * Logs a message at {@link Level#SEVERE SEVERE} level.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void severe(@Nonnull final String message, @Nullable final Object... data);

    /**
     * Logs a message with the specified logging level.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * @param level
     *         The logging level that should be used.
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object... data);

    /**
     * Logs a message with at {@link Level#TRACE TRACE} level along
     * with an exception.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * <p>The {@code exception}'s stacktrace is automatically logged
     * as well.</p>
     *
     * @param exception
     *         The exception to log.
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void trace(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data);

    /**
     * Logs a message with at {@link Level#DEBUG DEBUG} level along
     * with an exception.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * <p>The {@code exception}'s stacktrace is automatically logged
     * as well.</p>
     *
     * @param exception
     *         The exception to log.
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void debug(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data);

    /**
     * Logs a message with at {@link Level#INFO INFO} level along
     * with an exception.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * <p>The {@code exception}'s stacktrace is automatically logged
     * as well.</p>
     *
     * @param exception
     *         The exception to log.
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void info(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data);

    /**
     * Logs a message with at {@link Level#WARNING WARNING} level along
     * with an exception.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * <p>The {@code exception}'s stacktrace is automatically logged
     * as well.</p>
     *
     * @param exception
     *         The exception to log.
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void warning(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data);

    /**
     * Logs a message with at {@link Level#SEVERE SEVERE} level along
     * with an exception.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * <p>The {@code exception}'s stacktrace is automatically logged
     * as well.</p>
     *
     * @param exception
     *         The exception to log.
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void severe(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data);

    /**
     * Logs a message at the specified logging level along with an
     * exception.
     *
     * <p>The given {@code message} is automatically formatted if
     * valid {@code data} is supplied.</p>
     *
     * <p>The {@code exception}'s stacktrace is automatically logged
     * as well.</p>
     *
     * @param level
     *         The logging level that should be used.
     * @param exception
     *         The exception to log.
     * @param message
     *         The message to log. It must not be {@code null}.
     * @param data
     *         Optional data that should be used to format the given message.
     *
     * @since 1.0
     */
    void log(@Nonnull final Level level, @Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data);
}
