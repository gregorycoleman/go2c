public class CStringExpr extends CExpr {
	public String value;

	public CStringExpr(String v) {
		value = v;
	}

	public String toString() {
		return value;
	}
}
