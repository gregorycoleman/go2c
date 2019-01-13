import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class Lexer {

	// input buffer to fetch source code
	private BufferedInputStream stream;

	// source filename
	private String filename;

	// current line number
	private int lineNr;

	// number of errors found
	private int errorCount;

	// for lookahead cases
	private int save;

	// current lexem
	private StringBuilder lexem;

	// reserved words
	private HashMap<String, Token> keywords;

	public Lexer(String filename) throws FileNotFoundException {
		stream = new BufferedInputStream(new FileInputStream(filename));
		save = -1;
		errorCount = 0;
		lineNr = 1;
		this.filename = filename;
		lexem = new StringBuilder();

		keywords = new HashMap<>();

		keywords.put("break", Token.KW_BREAK);
		keywords.put("else", Token.KW_ELSE);
		keywords.put("for", Token.KW_FOR);
		keywords.put("func", Token.KW_FUNC);
		keywords.put("if", Token.KW_IF);
		keywords.put("import", Token.KW_IMPORT);
		keywords.put("package", Token.KW_PACKAGE);
		keywords.put("return", Token.KW_RETURN);
	}

	// scans the next token from the input stream
	public Token nextToken() throws IOException {
		int c;

		if (save != -1) {
			c = save;
			save = -1;
		} else
			c = stream.read();

		for (;;) {
			while (Character.isWhitespace(c)) {
				if (c == '\n')
					lineNr++;
				c = stream.read();
			}
			if (c != '/')
				break;

			c = stream.read();
			if (c == '/') {
				do {
					c = stream.read();
				} while (c != '\n' && c != -1);
			} else if (c == '*') {
				for (;;) {
					c = stream.read();
					if (c == '*') {
						do {
							c = stream.read();
						} while (c == '*');

						if (c == '/')
							break;
					}
				}
			} else {
				save = c;
				return Token.DIV;
			}
		}

		if (isAlpha(c)) {
			lexem.setLength(0);

			do {
				lexem.append((char)c);
				c = stream.read();
			} while (isAlpha(c) || isDigit(c));

			save = c;

			Token t = keywords.get(lexem.toString());
			return t != null ? t : Token.ID;
		}

		if (isDigit(c)) {
			lexem.setLength(0);

			do {
				lexem.append((char)c);
				c = stream.read();
			} while (isDigit(c));

			save = c;

			return Token.INTEGER;
		}

		if (c == '\'' || c == '"') {
			char quotes = (char)c;

			c = stream.read();

			lexem.setLength(0);
			while (c != quotes && c != '\n' && c != -1) {
				lexem.append((char)c);
				c = stream.read();
			}

			Token t = (quotes == '\'') ? Token.CHAR : Token.STRING;

			if (c != quotes) {
				save = c;
				reportError("unterminated character constant");
				lexem.setLength(0);
				return t;
			}

			String val = lexem.toString();

			if (t == Token.CHAR && val.length() != 1) {
				reportError("invalid character constant");
				lexem.setLength(0);
			}

			return t;
		}

		switch (c) {
		case '+':
			return Token.PLUS;
		case '-':
			return Token.MINUS;
		case '*':
			return Token.TIMES;
		case '%':
			return Token.MOD;
		case '>':
			c = stream.read();
			if (c == '=')
				return Token.GE;
			save = c;
			return Token.GT;
		case '<':
			c = stream.read();
			if (c == '=')
				return Token.LE;
			save = c;
			return Token.LT;
		case '=':
			c = stream.read();
			if (c == '=')
				return Token.EQ;
			save = c;
			return Token.ASSIGN;
		case ':':
			c = stream.read();
			if (c == '=')
				return Token.AUTO;
			save = c;
			break;
		case '(':
			return Token.LPAREN;
		case ')':
			return Token.RPAREN;
		case '{':
			return Token.LBRACE;
		case '}':
			return Token.RBRACE;
		case '[':
			return Token.LSQUARE;
		case ']':
			return Token.RSQUARE;
		case '|':
			c = stream.read();
			if (c == '|')
				return Token.OR;
			save = c;
			break;
		case '&':
			c = stream.read();
			if (c == '&')
				return Token.AND;
			save = c;
			break;
		case '!':
			return Token.NOT;
		case ';':
			return Token.SEMICOLON;
		case '.':
			return Token.PERIOD;
		case ',':
			return Token.COMMA;
		case -1:
			return Token.EOF;
		}

		return Token.INVALID;
	}

	public String lexem() {
		return lexem.toString();
	}

	private static boolean isAlpha(int c) {
		return Character.isAlphabetic(c) || c == '_';
	}

	private static boolean isDigit(int c) {
		return Character.isDigit(c);
	}

	public void reportError(String msg) {
		System.err.println(filename + ":" + lineNr + ": " + msg);
		errorCount++;
	}

	public int errorCount() {
		return errorCount;
	}
}
