import java.util.ArrayList;

public class CBlock extends CStmt {
	public ArrayList<CVarDecl> vars;
	public ArrayList<CStmt> stmts;

	public CBlock() {
		stmts = new ArrayList<>();
		vars = null;
	}

	public void add(CStmt stmt) {
		stmts.add(stmt);
	}

	public String toString() {
		return "block";
	}
}
