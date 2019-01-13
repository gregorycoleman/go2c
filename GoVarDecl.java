public class GoVarDecl extends GoStmt {
	public String name;
	public String typename;

	public GoVarDecl(String n, String t) {
		name = n;
		typename = t;
	}

	public String toString() {
		return name + " " + typename;
	}
}
