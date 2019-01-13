import java.util.ArrayList;

public class GoUnit extends GoNode {
	public ArrayList<GoFuncStmt> funcs;
	public GoScope scope;

	public GoUnit() {
		funcs = new ArrayList<>();
		scope = null;
	}

	public void add(GoFuncStmt f) {
		funcs.add(f);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (GoFuncStmt f : funcs) {
			sb.append(f.toString());
			sb.append("\n");
		}

		return sb.toString();
	}
}
