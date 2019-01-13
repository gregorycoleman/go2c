import java.util.ArrayList;

public class GoBlock extends GoStmt {
	public ArrayList<GoStmt> stmts;
	public GoScope scope;

	public GoBlock() {
		stmts = new ArrayList<GoStmt>();
		scope = null;
	}

	public void add(GoStmt stmt) {
		stmts.add(stmt);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ");
		for (int i = 0; i < stmts.size(); i++) {
			sb.append(stmts.get(i));
			if (i != stmts.size() - 1)
				sb.append("; ");
			else
				sb.append(" ");
		}
		sb.append("}");

		return sb.toString();
	}
}
