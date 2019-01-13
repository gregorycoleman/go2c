import java.io.IOException;
import java.util.ArrayList;

public class Parser {

	private Lexer lexer;
	private Token token;

	public Parser(Lexer l) {
		lexer = l;
		token = Token.EOF;
	}

	public GoUnit parse() {
		next();

		GoUnit unit = new GoUnit();

		// skip package
		if (token == Token.KW_PACKAGE) {
			next();
			match(Token.ID);
		}

		// skip import
		if (token == Token.KW_IMPORT) {
			next();

			if (token == Token.LPAREN) {
				do {
					next();
				} while (token == Token.STRING);

				match(Token.RPAREN);
			} else
				match(Token.STRING);
		}

		// func*
		while (token == Token.KW_FUNC)
			unit.add(parseFuncStmt());

		return unit;
	}

	private GoFuncStmt parseFuncStmt() {
		mustMatch(Token.KW_FUNC);

		String name = lexer.lexem();
		match(Token.ID);

		ArrayList<GoVarDecl> args = parseArgDeclList();

		String retTypename = null;
		if (token == Token.ID) {
			retTypename = lexer.lexem();
			next();
		}

		return new GoFuncStmt(name, args, retTypename, parseBlock());
	}

	private ArrayList<GoVarDecl> parseArgDeclList() {
		ArrayList<GoVarDecl> args = new ArrayList<>();

		if (!match(Token.LPAREN))
			return args;

		while (token != Token.RPAREN) {
			String name = lexer.lexem();
			match(Token.ID);

			String typename = lexer.lexem();
			match(Token.ID);

			args.add(new GoVarDecl(name, typename));

			if (token == Token.COMMA) {
				next();
				continue;
			}
			break;
		}
		match(Token.RPAREN);

		return args;
	}

	private GoStmt parseStmt() {
		switch (token) {
		case LBRACE:
			return parseBlock();
		case KW_IF:
			return parseIfStmt();
		case KW_FOR:
			return parseForStmt();
		case KW_RETURN:
			return parseReturnStmt();
		}

		if (isFirstExpr(token))
			return parseExprStmt();

		reportError("expected statement, found " + token);
		next();
		return null;
	}

	private GoBlock parseBlock() {
		GoBlock block = new GoBlock();

		if (!match(Token.LBRACE))
			return block;

		while (token != Token.RBRACE && token != Token.EOF) {
			GoStmt stmt = parseStmt();
			if (stmt != null)
				block.add(stmt);
		}

		match(Token.RBRACE);

		return block;
	}

	private GoReturnStmt parseReturnStmt() {
		mustMatch(Token.KW_RETURN);

		GoExpr expr = null;
		if (isFirstExpr(token))
			expr = parseExpr();

		return new GoReturnStmt(expr);
	}

	private GoIfStmt parseIfStmt() {
		mustMatch(Token.KW_IF);

		GoAssignStmt init = null;
		GoExpr cond = parseExpr();

		if (token == Token.ASSIGN || token == Token.AUTO) {
			Token op = token;
			next();

			GoExpr rhs = parseExpr();
			init = new GoAssignStmt(cond, op, rhs);

			match(Token.SEMICOLON);
			cond = parseExpr();
		}

		GoBlock truePart = parseBlock();
		GoBlock elsePart = null;

		if (token == Token.KW_ELSE) {
			next();
			elsePart = parseBlock();
		}

		return new GoIfStmt(init, cond, truePart, elsePart);
	}

	private GoForStmt parseForStmt() {
		mustMatch(Token.KW_FOR);

		GoAssignStmt init = null;
		GoExpr cond = null;
		GoAssignStmt step = null;

		if (token != Token.LBRACE) {
			cond = parseExpr();
			if (token == Token.ASSIGN || token == Token.AUTO) {
				Token op = token;
				next();

				GoExpr rhs = parseExpr();
				init = new GoAssignStmt(cond, op, rhs);

				match(Token.SEMICOLON);
				cond = parseExpr();
			}
			if (init != null) {
				match(Token.SEMICOLON);
				if (token != Token.LBRACE) {
					GoExpr lhs = parseExpr();
					match(Token.ASSIGN);
					GoExpr rhs = parseExpr();

					step = new GoAssignStmt(lhs, Token.ASSIGN, rhs);
				}
			}
		}

		GoBlock block = parseBlock();

		return new GoForStmt(init, cond, step, block);
	}

	// assign or expr(func call)
	private GoStmt parseExprStmt() {
		GoExpr left = parseExpr();

		// just an expression
		if (token != Token.ASSIGN && token != Token.AUTO)
			return new GoExprStmt(left);

		Token op = token;
		next(); // skip ASSIGN|AUTO

		return new GoAssignStmt(left, op, parseExpr());
	}

	private GoExpr parseExpr() {
		return parseOrExpr();
	}

	private GoExpr parseOrExpr() {
		GoExpr left = parseAndExpr();

		while (token == Token.OR) {
			Token op = token;

			next();

			GoExpr right = parseAndExpr();

			left = new GoBinExpr(left, op, right);
		}

		return left;
	}

	private GoExpr parseAndExpr() {
		GoExpr left = parseEqExpr();

		while (token == Token.AND) {
			Token op = token;

			next();

			GoExpr right = parseEqExpr();

			left = new GoBinExpr(left, op, right);
		}

		return left;
	}

	private GoExpr parseEqExpr() {
		GoExpr left = parseAddExpr();

		while (token == Token.EQ || token == Token.NE ||
				token == Token.LT || token == Token.LE ||
				token == Token.GT || token == Token.GE) {
			Token op = token;

			next();

			GoExpr right = parseAddExpr();

			left = new GoBinExpr(left, op, right);
		}

		return left;
	}

	private GoExpr parseAddExpr() {
		GoExpr left = parseMulExpr();

		while (token == Token.PLUS || token == Token.MINUS) {
			Token op = token;

			next();

			GoExpr right = parseMulExpr();

			left = new GoBinExpr(left, op, right);
		}

		return left;
	}

	private GoExpr parseMulExpr() {
		GoExpr left = parseUnaryExpr();

		while (token == Token.TIMES || token == Token.DIV ||
				token == Token.MOD) {
			Token op = token;

			next();

			GoExpr right = parseUnaryExpr();

			left = new GoBinExpr(left, op, right);
		}

		return left;
	}

	private GoExpr parseUnaryExpr() {
		if (token == Token.PLUS || token == Token.MINUS) {
			Token op = token;
			next();
			return new GoUnaryExpr(op, parseUnaryExpr());
		}

		GoExpr e = parsePrimaryExpr();

		while (token == Token.LPAREN || token == Token.LSQUARE) {
			if (token == Token.LPAREN) {
				next();

				ArrayList<GoExpr> args = new ArrayList<>();

				while (isFirstExpr(token)) {
					args.add(parseExpr());
					if (token == Token.RPAREN)
						break;
					match(Token.COMMA); // ,
				}
				match(Token.RPAREN);

				e = new GoFuncCallExpr(e, args);
			} else {
				next();

				GoExpr idx = parseExpr();

				match(Token.RSQUARE);

				e = new GoArrayExpr(e, idx);
			}
		}

		return e;
	}

	private GoExpr parsePrimaryExpr() {
		GoExpr e;

		switch (token) {
		case INTEGER:
			e = new GoIntExpr(lexer.lexem());
			next();
			break;
		case STRING:
			e = new GoStringExpr(lexer.lexem());
			next();
			break;
		case CHAR:
			e = new GoCharExpr(lexer.lexem());
			next();
			break;
		case ID:
			String name = lexer.lexem();
			next();

			// package name like fmt.Printf
			while (token == Token.PERIOD) {
				name += ".";
				next();
				name += lexer.lexem();
				match(Token.ID);
			}

			e = new GoVarExpr(name);
			break;
		case LPAREN:
			next();
			e = parseExpr();
			match(Token.RPAREN);
			break;
		default:
			reportError("expected primary expression, found " + token + ".");
			next();
			e = new GoInvalidExpr();
		}

		return e;
	}

	private boolean isFirstExpr(Token t) {
		return t == Token.INTEGER || t == Token.CHAR || t == Token.STRING ||
		       t == Token.ID || t == Token.LPAREN || t == Token.PLUS ||
		       t == Token.MINUS;
	}

	private void next() {
		try {
			token = lexer.nextToken();
		} catch (IOException e) {
			e.printStackTrace();
			token = Token.EOF;
		}
	}

	private boolean match(Token expected) {
		if (token != expected) {
			reportError("unexpected token " + token + ". " + expected + " expected.");
			return false;
		}
		next();
		return true;
	}

	private void mustMatch(Token expected) {
		if (!match(expected)) {
			System.err.println("mustMatch failure, compiler bug.");
			System.exit(1);
		}
	}

	private void reportError(String msg) {
		lexer.reportError(msg);
	}

	public int errorCount() {
		return lexer.errorCount();
	}
}
