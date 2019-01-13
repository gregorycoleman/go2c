public class GoCharExpr extends GoExpr {
	public String value;

	public GoCharExpr(String v) {
		value = v;
		type = GoType.INT;
	}

	public String toString() {
		return "'" + value + "'";
	}
}
