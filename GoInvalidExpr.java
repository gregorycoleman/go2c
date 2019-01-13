public class GoInvalidExpr extends GoExpr {
	public GoInvalidExpr() {
		type = GoType.INVALID;
	}

	public String toString() {
		return "<invalid>";
	}
}
