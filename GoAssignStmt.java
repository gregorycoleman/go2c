import java.util.ArrayList;

public class GoAssignStmt extends GoStmt {
	public GoExpr lhs;
	public Token op;
	public GoExpr rhs;

	public GoAssignStmt(GoExpr l, Token o, GoExpr r) {
		assert l != null;
		assert o != null;
		assert r != null;

		lhs = l;
		rhs = r;
		op = o;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(lhs);
		sb.append(" ");
		sb.append(op);
		sb.append(" ");
		sb.append(rhs);

		return sb.toString();
	}
}
