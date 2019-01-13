import java.util.ArrayList;

public class GoFuncCallExpr extends GoExpr {
	public GoExpr expr;
	public ArrayList<GoExpr> args;

	public GoFuncCallExpr(GoExpr e, ArrayList<GoExpr> a) {
		expr = e;
		args = a;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(expr);
		sb.append("(");
		for (int i = 0; i < args.size(); i++) {
			sb.append(args.get(i));
			if (i != args.size() - 1)
				sb.append(", ");
		}
		sb.append(")");

		return sb.toString();
	}
}
