public class GoType {
	public static GoType INVALID = new GoType("<invalid>");
	public static GoType INT = new GoType("int");
	public static GoType STRING = new GoType("string");
	public static GoType BOOL = new GoType("bool");

	private String name;

	public GoType(String n) {
		name = n;
	}

	public String toString() {
		return name;
	}
}
