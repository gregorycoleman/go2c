import java.util.ArrayList;

public class GoScope {
	public GoScope prior;
	public ArrayList<GoSymbol> symbols;

	public GoScope(GoScope p) {
		symbols = new ArrayList<>();
		prior = p;
	}

	public void add(GoSymbol sym) {
		symbols.add(sym);
	}

	// lookup a symbol starting from this scope
	public GoSymbol lookupSymbol(String name) {
		for (GoScope sc = this; sc != null; sc = sc.prior) {
			for (GoSymbol sym : sc.symbols)
				if (sym.name.equals(name))
					return sym;
		}
		return null;
	}
}
