package openmods.igw.prefab.service.fallback;

import openmods.igw.api.OpenModsIGWApi;
import openmods.igw.api.service.ILoggingService;
import openmods.igw.api.service.IService;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A basic logging service that logs everything
 * to {@link System#out} or {@link System#err}.
 *
 * <p><strong>WARNING!</strong><br />
 * This should not be used in production!</p>
 *
 * @author TheSilkMiner
 * @since 1.0
 */
@SuppressWarnings("unused")
public final class SystemLoggingService implements ILoggingService {

	private static final SystemLoggingService THE_LOGGING_SERVICE = new SystemLoggingService();

	private SystemLoggingService() {}

	@Nullable // Actually not null, but we use nullable to make the thing more annoying to use.
	@SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
	public static SystemLoggingService getTheLoggingService() {
		final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		if (isInvalidCall(stackTrace[0], "openmods.igw.prefab.service.fallback.SystemLoggingService.getTheLoggingService")
				|| isInvalidCall(stackTrace[3], "sun.reflect.DelegatingMethodAccessorImpl.invoke")
				|| isInvalidCall(stackTrace[5], "openmods.igw.api.OpenModsIGWApi.<clinit>")
				|| isInvalidCall(stackTrace[7], "openmods.igw.impl.proxy.ClientProxy.construct")
				|| isInvalidCall(stackTrace[13], "net.minecraftforge.fml.common.FMLModContainer.handleModStateEvent"))
			throw new SecurityException("Unsafe call to " + SystemLoggingService.class);
		return THE_LOGGING_SERVICE;
	}

	private static boolean isInvalidCall(@Nonnull final StackTraceElement ele, @Nonnull final String call) {
		final String actualCall = ele.getClassName() + "." + ele.getMethodName();
		if (!call.equals(actualCall)) {
			System.out.println("Expected " + call + ", got " + actualCall);
			return true;
		}
		return false;
	}

	private void register() {
		OpenModsIGWApi.get().serviceManager().registerService(ILoggingService.class, this);
	}

	@Nonnull
	@Override
	public ILoggingService cast() {
		return this;
	}

	@Override
	public void onRegisterPre(@Nullable final IService<ILoggingService> previous) {
		if (previous == null) return;

		// As I said, this service should not be used if others are in place
		// (That's also the reason why there are so many checks in place)
		// As such, we have to warn the programmer. Exceptions are no good,
		// so we log. In the end... it's their fault, not ours.
		this.warning("WHAT ARE YOU DOING? Why are you substituting a service with this fallback?");
		this.warning("This is a serious programming error! Please check your software NOW!");
	}

	@Override
	public void onRegisterPost() {
		this.info("Successfully registered fallback service!");
	}

	@Override
	public void onUnregister() {
		this.info("Fallback service has been replaced. This is a good sign!");
	}

	@Override
	public void trace(@Nonnull final String message, @Nullable final Object... data) {
		this.log(Level.TRACE, message, data);
	}

	@Override
	public void debug(@Nonnull final String message, @Nullable final Object... data) {
		this.log(Level.DEBUG, message, data);
	}

	@Override
	public void info(@Nonnull final String message, @Nullable final Object... data) {
		this.log(Level.INFO, message, data);
	}

	@Override
	public void warning(@Nonnull final String message, @Nullable final Object... data) {
		this.log(Level.WARNING, message, data);
	}

	@Override
	public void severe(@Nonnull final String message, @Nullable final Object... data) {
		this.log(Level.SEVERE, message, data);
	}

	@Override
	public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object... data) {
		this.log(level, null, message, data);
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
		this.log(Level.INFO, exception, message, data);
	}

	@Override
	public void warning(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data) {
		this.log(Level.WARNING, exception, message, data);
	}

	@Override
	public void severe(@Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data) {
		this.log(Level.SEVERE, exception, message, data);
	}

	@Override
	public void log(@Nonnull final Level level, @Nullable final Throwable exception, @Nonnull final String message, @Nullable final Object... data) {
		// Does this even make sense? I don't know...
		if (level == Level.TRACE || level == Level.DEBUG) return;

		final Calendar cal = Calendar.getInstance();
		//noinspection MagicConstant
		final String toLog = String.format("[%02d:%02d:%02d %s] [%s/%s] [OpenMods-IGW/Fallback Logger]: %s%n",
				cal.get(Calendar.HOUR),
				cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND),
				cal.get(Calendar.AM_PM) == Calendar.PM? "PM" : "AM", // So... I should compare with Sunday... whatever...
				Thread.currentThread().getName(),
				level.name().toUpperCase(Locale.ENGLISH),
				String.format(message, data));
		final PrintStream stream;

		if (level == Level.WARNING || level == Level.SEVERE) stream = System.err;
		else stream = System.out;

		stream.print(toLog);
		if (exception == null) return;
		exception.printStackTrace(stream);
	}
}
