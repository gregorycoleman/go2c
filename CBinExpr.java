public class CBinExpr extends CExpr {
	public CExpr left;
	public String op;
	public CExpr right;

	public CBinExpr(CExpr l, String o, CExpr r) {
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
