public class GoIntExpr extends GoExpr {
	public String value;

	public GoIntExpr(String v) {
		value = v;
		type = GoType.INT;
	}

	public String toString() {
		return value;
	}
}
