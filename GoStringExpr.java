public class GoStringExpr extends GoExpr {
	public String value;

	public GoStringExpr(String v) {
		value = v;
		type = GoType.STRING;
	}

	public String toString() {
		return "\"" + value + "\"";
	}
}
