import java.util.ArrayList;

public class CIfStmt extends CStmt {
	public CExpr assign;
	public CExpr condition;
	public CBlock truePart;
	public CBlock elsePart;
	public ArrayList<CVarDecl> vars;

	public CIfStmt(CExpr a, CExpr c, CBlock t, CBlock e) {
		assert c != null;
		assert t != null;

		assign = a;
		condition = c;
		truePart = t;
		elsePart = e;
	}
}
