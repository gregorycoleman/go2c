import java.util.ArrayList;

public class CForStmt extends CStmt {
	public CExpr init;
	public CExpr condition;
	public CExpr step;
	public CBlock block;
	public ArrayList<CVarDecl> vars;

	public CForStmt(CExpr i, CExpr c, CExpr s, CBlock b) {
		assert b != null;

		init = i;
		condition = c;
		step = s;
		block = b;
	}
}
