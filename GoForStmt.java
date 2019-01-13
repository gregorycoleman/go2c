import java.util.ArrayList;

public class GoForStmt extends GoStmt {
	public GoAssignStmt init;
	public GoExpr condition;
	public GoAssignStmt step;
	public GoBlock block;
	public GoScope scope;

	public GoForStmt(GoAssignStmt i, GoExpr c, GoAssignStmt s, GoBlock b) {
		assert b != null;

		init = i;
		condition = c;
		step = s;
		block = b;
		scope = null;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("for ");
		if (init != null) {
			sb.append(init);
			sb.append("; ");
		}
		if (condition != null) {
			sb.append(condition);
			sb.append("; ");
		}
		if (step != null)
			sb.append(step);
		sb.append(block);

		return sb.toString();
	}
}
