import java.util.ArrayList;

public class CFuncStmt extends CStmt {
	public String name;
	public ArrayList<CVarDecl> args;
	public CBlock body;
	public String retTypename;

	public CFuncStmt(String n, ArrayList<CVarDecl> a, String r, CBlock b) {
		assert n != null;
		assert a != null;
		assert b != null;

		name = n;
		args = a;
		body = b;
		retTypename = r;
	}

	public String toString() {
		return name;
	}
}
