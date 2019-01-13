public class CUnaryExpr extends CExpr {
	public String op;
	public CExpr expr;

	public CUnaryExpr(String o, CExpr e) {
		assert o != null;
		assert e != null;

		op = o;
		expr = e;
	}
}
