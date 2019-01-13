public enum Token {
	KW_BREAK("break"),
	KW_ELSE("else"),
	KW_FOR("for"),
	KW_FUNC("func"),
	KW_IF("if"),
	KW_IMPORT("import"),
	KW_PACKAGE("package"),
	KW_RETURN("return"),

	ID("<id>"),
	INTEGER("<integer>"),
	CHAR("<char>"),
	STRING("<string>"),

	PLUS("+"),
	MINUS("-"),
	TIMES("*"),
	DIV("/"),
	MOD("%"),

	ASSIGN("="),
	AUTO(":="),
	EQ("=="),
	NE("!="),
	GT(">"),
	GE(">="),
	LT("<"),
	LE("<="),

	LPAREN("("),
	RPAREN(")"),
	LBRACE("{"),
	RBRACE("}"),
	LSQUARE("["),
	RSQUARE("]"),
	OR("||"),
	AND("&&"),
	NOT("!"),
	SEMICOLON(";"),
	PERIOD("."),
	COMMA(","),

	INVALID("<invalid>"),
	EOF("<eof>");

	private String string;

	private Token(String str) {
		string = str;
	}

	public String toString() {
		return string;
	}
}
