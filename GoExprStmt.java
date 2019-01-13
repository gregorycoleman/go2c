import java.util.ArrayList;

public class GoExprStmt extends GoStmt {
	public GoExpr expr;

	public GoExprStmt(GoExpr e) {
		assert e != null;

		expr = e;
	}

	public String toString() {
		return expr.toString();
	}
}
