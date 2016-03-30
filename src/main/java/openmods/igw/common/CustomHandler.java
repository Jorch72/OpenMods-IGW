package openmods.igw.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomHandler {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface HandlerMethod {

		String event();

		class Events {

			public static final String PAGE_OPENED = "pageOpened";
			public static final String VARIABLE_SUBSTITUTION = "variableSubstitution";
		}
	}
}
