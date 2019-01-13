import java.util.ArrayList;

public class CExprStmt extends CStmt {
	public CExpr expr;

	public CExprStmt(CExpr e) {
		assert e != null;

		expr = e;
	}

	public String toString() {
		return expr.toString();
	}
}
