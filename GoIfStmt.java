import java.util.ArrayList;

public class GoIfStmt extends GoStmt {
	public GoAssignStmt assign;
	public GoExpr condition;
	public GoBlock truePart;
	public GoBlock elsePart;
	public GoScope scope;

	public GoIfStmt(GoAssignStmt a, GoExpr c, GoBlock t, GoBlock e) {
		assert c != null;
		assert t != null;

		assign = a;
		condition = c;
		truePart = t;
		elsePart = e;
		scope = null;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("if ");
		if (assign != null) {
			sb.append(assign);
			sb.append("; ");
		}
		sb.append(condition);
		sb.append(" ");
		sb.append(truePart);

		if (elsePart != null) {
			sb.append(" else ");
			sb.append(elsePart);
		}

		return sb.toString();
	}
}
