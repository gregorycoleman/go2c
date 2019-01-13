public class CArrayExpr extends CExpr {
	public CExpr expr;
	public CExpr index;

	public CArrayExpr(CExpr e, CExpr i) {
		assert e != null;
		assert i != null;

		expr = e;
		index = i;
	}
}
