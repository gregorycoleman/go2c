public class GoUnaryExpr extends GoExpr {
	public Token op;
	public GoExpr expr;

	public GoUnaryExpr(Token o, GoExpr e) {
		assert o != null;
		assert e != null;

		op = o;
		expr = e;
	}

	public String toString() {
		return op.toString() + expr;
	}
}
