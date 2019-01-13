public class GoVarExpr extends GoExpr {
	public String name;
	public GoSymbol sym;

	public GoVarExpr(String n) {
		name = n;
		sym = null;
	}

	public String toString() {
		return name;
	}
}
