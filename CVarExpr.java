public class CVarExpr extends CExpr {
	public String name;

	public CVarExpr(String n) {
		name = n;
	}

	public String toString() {
		return name;
	}
}
