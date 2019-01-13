public class GoReturnStmt extends GoStmt {
	GoExpr expr;

	public GoReturnStmt(GoExpr e) {
		expr = e;
	}

	public String toString() {
		String s = "return";
		if (expr != null)
			s += " " + expr;
		return s;
	}
}
