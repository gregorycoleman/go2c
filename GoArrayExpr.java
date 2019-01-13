public class GoArrayExpr extends GoExpr {
	public GoExpr expr;
	public GoExpr index;

	public GoArrayExpr(GoExpr e, GoExpr i) {
		assert e != null;
		assert i != null;

		expr = e;
		index = i;
	}

	public String toString() {
		return expr + "[" + index + "]";
	}
}
