import java.util.ArrayList;

public class GoFuncStmt extends GoStmt {
	public String name;
	public ArrayList<GoVarDecl> args;
	public GoBlock body;
	public String retTypename;
	public GoSymbol sym;
	public GoScope scope;

	public GoFuncStmt(String n, ArrayList<GoVarDecl> a, String r, GoBlock b) {
		assert n != null;
		assert a != null;
		assert b != null;

		name = n;
		args = a;
		body = b;
		retTypename = r;
		scope = null;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("func ");
		sb.append(name);
		sb.append("(");
		for (int i = 0; i < args.size(); i++) {
			sb.append(args.get(i));
			if (i != args.size() - 1)
				sb.append(", ");
		}
		sb.append(") ");
		if (retTypename != null) {
			sb.append(retTypename);
			sb.append(" ");
		}
		sb.append(body);

		return sb.toString();
	}
}
