package openmods.igw.impl.service;

import com.google.common.collect.Maps;
import openmods.Log;
import openmods.igw.api.service.ILoggingService;
import openmods.igw.api.service.IService;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LoggingService implements ILoggingService {

	private static class InvalidLevelException extends RuntimeException {
		private InvalidLevelException(final String message, final Throwable cause) {
			super(message, cause);
		}
	}

	private static final LoggingService IT = new LoggingService();

	private final Map<Level, org.apache.logging.log4j.Level> cache = Maps.newHashMap();

	private LoggingService() {}

	public static LoggingService get() {
		return IT;
	}

	@Override
	public void trace(@Nonnull final String message, @Nullable final Object... data) {
		Log.trace(message, data);
	}

	@Override
	public void debug(@Nonnull final String message, @Nullable final Object... data) {
		Log.debug(message, data);
	}

	@Override
	public void info(@Nonnull final String message, @Nullable final Object... data) {
		Log.info(message, data);
	}

	@Override
	public void warning(@Nonnull final String message, @Nullable final Object... data) {
		Log.warn(message, data);
	}

	@Override
	public void severe(@Nonnull final String message, @Nullable final Object... data) {
		Log.severe(message, data);
	}

	@Override
	public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object... data) {
		Log.log(this.toApacheLevel(level), message, data);
	}

	@Override
	public void trace(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data) {
		this.log(Level.TRACE, exception, message, data);
	}

	@Override
	public void debug(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data) {
		this.log(Level.DEBUG, exception, message, data);
	}

	@Override
	public void info(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data) {
		Log.info(exception, message, data);
	}

	@Override
	public void warning(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data) {
		Log.warn(exception, message, data);
	}

	@Override
	public void severe(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data) {
		Log.severe(exception, message, data);
	}

	@Override
	public void log(@Nonnull final Level level, @Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data) {
		Log.log(this.toApacheLevel(level), exception, message, data);
	}

	@Nonnull
	@Override
	public ILoggingService cast() {
		return this;
	}

	@Override
	public void onRegisterPre(@Nullable final IService<ILoggingService> previous) {}

	@Override
	public void onRegisterPost() {}

	@Override
	public void onUnregister() {
		this.cache.clear();
	}

	@Nonnull
	private org.apache.logging.log4j.Level toApacheLevel(@Nonnull final Level level) {
		if (this.cache.containsKey(level)) return this.cache.get(level);
		String levelName = level.name();
		if ("WARNING".equals(levelName)) levelName = "WARN";
		if ("SEVERE".equals(levelName)) levelName = "ERROR";
		final org.apache.logging.log4j.Level lvl;
		try {
			lvl = org.apache.logging.log4j.Level.valueOf(levelName);
		} catch (final IllegalArgumentException e) {
			throw new InvalidLevelException("An error has occurred while converting to apache level", e);
		}
		this.cache.put(level, lvl);
		return lvl;
	}
}
