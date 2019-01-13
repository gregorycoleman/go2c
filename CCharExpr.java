public class CCharExpr extends CExpr {
	public String value;

	public CCharExpr(String v) {
		value = v;
	}

	public String toString() {
		return value;
	}
}
