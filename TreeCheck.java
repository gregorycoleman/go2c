import java.util.ArrayList;

public class TreeCheck {
	private int errorCount;
	private GoFuncStmt currentFunc;
	private GoScope globalScope;
	private GoScope currentScope;

	public TreeCheck() {
		errorCount = 0;
		currentFunc = null;

		// this is the global scope, never popped out
		globalScope = new GoScope(null);
		currentScope = globalScope;

		// urghh
		ArrayList<GoSymbol> args = new ArrayList<>();
		args.add(new GoSymbol("value", GoType.INT));
		globalScope.add(new GoSymbol("int", new GoFuncType(args, GoType.INT)));

		args = new ArrayList<>();
		args.add(new GoSymbol("value", GoType.INT));
		globalScope.add(new GoSymbol("string", new GoFuncType(args, GoType.STRING)));

		args = new ArrayList<>();
		args.add(new GoSymbol("value", GoType.STRING));
		globalScope.add(new GoSymbol("len", new GoFuncType(args, GoType.INT)));

		args = new ArrayList<>();
		args.add(new GoSymbol("value", GoType.STRING));
		globalScope.add(new GoSymbol("fmt.Println", new GoFuncType(args, null)));

		args = new ArrayList<>();
		args.add(new GoSymbol("value", GoType.STRING));
		globalScope.add(new GoSymbol("fmt.Print", new GoFuncType(args, null)));
	}

	private void pushScope() {
		currentScope = new GoScope(currentScope);
	}

	private GoScope popScope() {
		assert currentScope != globalScope;

		GoScope sc = currentScope;
		currentScope = currentScope.prior;
		return sc;
	}

	public void reportError(String msg) {
		System.err.println(msg);
		errorCount++;
	}

	private GoType checkBinary(GoType left, Token op, GoType right) {
		assert left != null;
		assert right != null;

		// since we got an error already, avoid reporting it repeatedly
		if (left == GoType.INVALID || right == GoType.INVALID)
			return GoType.INVALID;

		switch (op) {
		case PLUS:
			if (left == GoType.INT && right == GoType.INT)
				return GoType.INT;
			if (left == GoType.STRING && right == GoType.STRING)
				return GoType.STRING;
			break;

		case MINUS:
		case TIMES:
		case DIV:
		case MOD:
			if (left == GoType.INT && right == GoType.INT)
				return GoType.INT;
			break;

		case GT:
		case GE:
		case LT:
		case LE:
			if (left == GoType.INT && right == GoType.INT)
				return GoType.BOOL;
			break;

		case EQ:
		case NE:
			if (left == right)
				return GoType.BOOL;
			break;

		case AND:
		case OR:
			if (left == GoType.BOOL && right == GoType.BOOL)
				return GoType.BOOL;
			break;

		default:
			assert false;
		}

		reportError("incompatible types in binary expression: " + left + " " + op + " " + right);
		return GoType.INVALID;
	}

	private GoType checkUnary(Token op, GoType type) {
		assert type != null;

		if (type == GoType.INVALID)
			return GoType.INVALID;

		switch (op) {
		case PLUS:
		case MINUS:
			if (type == GoType.INT)
				return GoType.INT;
			break;
		default:
			assert false;
		}

		reportError("incompatible types in unary expression: " + op + " " + type);
		return GoType.INVALID;
	}

	// only strings "abc"[0]
	private GoType checkIndex(GoType left, GoType idx) {
		assert left != null;
		assert idx != null;

		if (left == GoType.INVALID || idx == GoType.INVALID)
			return GoType.INVALID;

		if (left != GoType.STRING) {
			reportError("expected string, got " + left);
			return GoType.INVALID;
		}

		if (idx != GoType.INT) {
			reportError("expected int, got " + idx);
			return GoType.INVALID;
		}

		return GoType.INT;
	}

	private GoType typeByTypename(String typename) {
		if (typename.equals("int"))
			return GoType.INT;
		if (typename.equals("string"))
			return GoType.STRING;
		if (typename.equals("bool"))
			return GoType.BOOL;
		return null;
	}

	private ArrayList<GoSymbol> typeCheckFuncArgs(GoFuncStmt func) {
		ArrayList<GoSymbol> args = new ArrayList<>();

		for (GoVarDecl decl : func.args) {
			GoSymbol sym = currentScope.lookupSymbol(decl.name);
			if (sym != null) {
				reportError("symbol " + sym + " already exists");
				continue;
			}

			GoType type = typeByTypename(decl.typename);

			if (type == null) {
				reportError("unknown type " + decl.typename);
				continue;
			}

			args.add(new GoSymbol(decl.name, type));
		}

		return args;
	}

	// yeah we are in a hurry, no visitor pattern folks...
	public void typeCheck(GoNode tree) {

		// unit
		if (tree instanceof GoUnit) {
			GoUnit unit = (GoUnit)tree;

			for (GoFuncStmt f : unit.funcs)
				typeCheck(f);

			return;
		}

		// func
		if (tree instanceof GoFuncStmt) {
			GoFuncStmt func = (GoFuncStmt)tree;

			ArrayList<GoSymbol> args = typeCheckFuncArgs(func);
			GoType retType = null;
			if (func.retTypename != null) {
				retType = typeByTypename(func.retTypename);
				if (retType == null) {
					reportError("invalid return typename");

					retType = GoType.INVALID;
				}
			}

			GoSymbol sym = new GoSymbol(func.name, new GoFuncType(args, retType));
			func.sym = sym;
			currentScope.add(sym);

			// to check return statements type
			currentFunc = func;

			pushScope();

			// insert function arguments on current scope
			for (GoSymbol s : args)
				currentScope.add(s);

			typeCheck(func.body);

			func.scope = popScope();
			return;
		}

		if (tree instanceof GoIfStmt) {
			GoIfStmt stmt = (GoIfStmt)tree;

			pushScope();

			if (stmt.assign != null)
				typeCheck(stmt.assign);

			typeCheck(stmt.condition);
			if (stmt.condition.type != GoType.BOOL)
				reportError("expected boolean expression, found " + stmt.condition.type);

			typeCheck(stmt.truePart);

			if (stmt.elsePart != null)
				typeCheck(stmt.elsePart);

			stmt.scope = popScope();
			return;
		}

		if (tree instanceof GoForStmt) {
			GoForStmt stmt = (GoForStmt)tree;

			pushScope();

			if (stmt.init != null)
				typeCheck(stmt.init);

			if (stmt.condition != null) {
				typeCheck(stmt.condition);
				if (stmt.condition.type != GoType.BOOL)
					reportError("expected boolean expression, found " + stmt.condition.type);
			}

			if (stmt.step != null)
				typeCheck(stmt.step);

			typeCheck(stmt.block);

			stmt.scope = popScope();
			return;
		}

		// block
		if (tree instanceof GoBlock) {
			GoBlock block = (GoBlock)tree;

			pushScope();

			for (GoStmt stmt : block.stmts)
				typeCheck(stmt);

			block.scope = popScope();
			return;
		}

		if (tree instanceof GoAssignStmt) {
			GoAssignStmt stmt = (GoAssignStmt)tree;

			if (stmt.op == Token.ASSIGN)
				typeCheck(stmt.lhs);

			typeCheck(stmt.rhs);

			// we dont have the concept of lvalue/rvalue well defined.
			// lets only accept GoVarExpr's and thats it

			if (!(stmt.lhs instanceof GoVarExpr)) {
				reportError("expected a variable at the left-hand side of assignment expression");
				return;
			}

			GoVarExpr v = (GoVarExpr)stmt.lhs;

			if (stmt.op == Token.ASSIGN) {
				if (v.sym == null) {
					// error already reported
					return;
				}
			} else {
				GoSymbol sym = currentScope.lookupSymbol(v.name);
				if (sym != null) {
					reportError("variable " + v.name + " already initialized");
					return;
				}
				// declare variable
				sym = new GoSymbol(v.name, stmt.rhs.type);
				currentScope.add(sym);
				v.sym = sym;

				stmt.lhs.type = sym.type;
			}

			assert stmt.lhs.type != null;
			assert stmt.rhs.type != null;

			if (stmt.lhs.type == GoType.INVALID || stmt.rhs.type == GoType.INVALID)
				return;

			if (stmt.lhs.type != stmt.rhs.type)
				reportError("incompatible types in assignment expression: " + stmt.lhs.type + " and " + stmt.rhs.type);

			return;
		}

		if (tree instanceof GoReturnStmt) {
			GoReturnStmt stmt = (GoReturnStmt)tree;

			assert currentFunc != null;

			typeCheck(stmt.expr);

			GoFuncType type = (GoFuncType)currentFunc.sym.type;
			if (type.retType == null) {
				if (stmt.expr != null)
					reportError("function have no return value");
				return;
			}

			if (stmt.expr == null) {
				reportError("return value expression expected");
				return;
			}

			if (stmt.expr.type != type.retType)
				reportError("return value incompatible, " + type.retType + " expected got " + stmt.expr.type);
			return;
		}

		// stmt <expr>
		if (tree instanceof GoExprStmt) {
			GoExprStmt stmt = (GoExprStmt)tree;

			typeCheck(stmt.expr);
			return;
		}

		// X a
		if (tree instanceof GoUnaryExpr) {
			GoUnaryExpr unary = (GoUnaryExpr)tree;

			typeCheck(unary.expr);

			unary.type = checkUnary(unary.op, unary.expr.type);
			return;
		}

		// a X b
		if (tree instanceof GoBinExpr) {
			GoBinExpr bin = (GoBinExpr)tree;

			typeCheck(bin.left);
			typeCheck(bin.right);

			bin.type = checkBinary(bin.left.type, bin.op, bin.right.type);
			return;
		}

		if (tree instanceof GoVarExpr) {
			GoVarExpr v = (GoVarExpr)tree;

			GoSymbol sym = currentScope.lookupSymbol(v.name);
			if (sym == null) {
				reportError("undeclared variable " + v.name);

				sym = new GoSymbol(v.name, GoType.INVALID);
			}

			v.sym = sym;
			v.type = sym.type;
			return;
		}

		if (tree instanceof GoFuncCallExpr) {
			GoFuncCallExpr call = (GoFuncCallExpr)tree;

			call.type = GoType.INVALID;

			if (!(call.expr instanceof GoVarExpr)) {
				reportError("expression not a valid function name");
				return;
			}

			String funcName = ((GoVarExpr)call.expr).name;

			for (GoExpr e : call.args)
				typeCheck(e);

			GoSymbol func = currentScope.lookupSymbol(funcName);
			if (func == null) {
				reportError("unknown function " + funcName);
				return;
			}
			if (!(func.type instanceof GoFuncType)) {
				reportError(func.name + " is not a function");
				return;
			}

			GoFuncType ft = (GoFuncType)func.type;

			call.type = ft.retType;

			if (call.args.size() != ft.args.size()) {
				reportError("expected " + ft.args.size() + " arguments, got " + call.args.size());
				return;
			}

			for (int i = 0; i < call.args.size(); i++) {
				if (call.args.get(i).type != ft.args.get(i).type) {
					reportError("incompatible types");
				}
			}

			return;
		}

		if (tree instanceof GoArrayExpr) {
			GoArrayExpr a = (GoArrayExpr)tree;

			typeCheck(a.expr);
			typeCheck(a.index);

			a.type = checkIndex(a.expr.type, a.index.type);
			return;
		}
	}

	public int errorCount() {
		return errorCount;
	}
}
