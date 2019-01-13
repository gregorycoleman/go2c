import java.util.ArrayList;

public class CUnit extends CNode {
	public ArrayList<CFuncStmt> funcs;

	public CUnit() {
		funcs = new ArrayList<>();
	}

	public void add(CFuncStmt f) {
		funcs.add(f);
	}
}
