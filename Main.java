public class Main {
	public static void main(String[] args) {
		try {
			Lexer l = new Lexer("example.go");

			Parser p = new Parser(l);

			GoNode tree = p.parse();
			if (tree != null) {
				//System.out.println(tree);

				TreeCheck check = new TreeCheck();
				check.typeCheck(tree);

				if (p.errorCount() == 0 && check.errorCount() == 0) {
					GoToC trans = new GoToC();

					trans.dump(trans.translate(tree));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
