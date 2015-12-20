package openmods.igw.utils;

/**
 * @author TheSilkMiner
 */
public class DepBuilder {

	public enum Type {

		REQUIRED_BEFORE(true, Order.BEFORE),
		REQUIRED_AFTER(true, Order.AFTER),
		BEFORE(Order.BEFORE),
		AFTER(Order.AFTER);

		private boolean required;

		private Order order;

		Type(boolean required, Order order) {

			this.required = required;
			this.order = order;
		}

		Type(Order order) {

			this(false, order);
		}

		@Override
		public String toString() {

			String builder = "";
			builder += this.required ? "required-" : "";
			builder += this.order.toString();
			return builder;
		}
	}

	private enum Order {
		BEFORE,
		AFTER;

		@Override
		public String toString() {

			return this == BEFORE ? "before" : "after";
		}
	}

	private StringBuilder builder;

	public DepBuilder() {

		builder = new StringBuilder();
		this.addDepWithVersion(DepBuilder.Type.REQUIRED_AFTER, "OpenMods", "$LIB-VERSION$", "$NEXT-LIB-VERSION$");
	}

	public DepBuilder addDep(Type type, String modId) {

		if (!builder.toString().isEmpty()) builder.append(";");

		builder.append(type.toString());
		builder.append(":");
		builder.append(modId);

		return this;
	}

	public DepBuilder addDepWithVersion(Type type, String modId, String minVer, String maxVer) {

		this.addDep(type, modId);
		builder.append("@[");
		builder.append(minVer);
		builder.append(",");
		builder.append(maxVer == null ? " " : maxVer);
		builder.append(")");

		return this;
	}

	@Override
	public String toString() {

		return builder.toString();
	}
}
