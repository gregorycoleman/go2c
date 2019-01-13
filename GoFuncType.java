import java.util.ArrayList;

public class GoFuncType extends GoType {
	public ArrayList<GoSymbol> args;
	public GoType retType;

	public GoFuncType(ArrayList<GoSymbol> a, GoType r) {
		super("func");

		assert a != null;

		args = a;
		retType = r;
	}
}
