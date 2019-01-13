import java.util.ArrayList;

public class CFuncCallExpr extends CExpr {
	public String name;
	public ArrayList<CExpr> args;

	public CFuncCallExpr(String n, ArrayList<CExpr> a) {
		name = n;
		args = a;
	}
}
