public class CVarDecl extends CStmt {
	public String name;
	public String typename;

	public CVarDecl(String n, String t) {
		name = n;
		typename = t;
	}

	public String toString() {
		return typename + " " + name;
	}
}
