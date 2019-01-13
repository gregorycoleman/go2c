public class GoBinExpr extends GoExpr {
	public GoExpr left;
	public Token op;
	public GoExpr right;

	public GoBinExpr(GoExpr l, Token o, GoExpr r) {
		assert l != null;
		assert o != null;
		assert r != null;

		left = l;
		op = o;
		right = r;
	}

	public String toString() {
		return "(" + left + " " + op + " " + right + ")";
	}
}
