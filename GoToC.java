import java.util.ArrayList;

public class GoToC {

	public GoToC() {
	}

	private int indent = 0;

	private void printIndent() {
		for (int i = 0; i < indent; i++)
			System.out.print("\t");
	}

	public void dump(CNode tree) {

		if (tree instanceof CUnit) {
			CUnit cu = (CUnit)tree;

			System.out.println("#include <stdio.h>");
			System.out.println("#include <string.h>");

			System.out.println("static inline void __fmt_Print(const char *s) {");
			System.out.println("\tprintf(\"%s\", s);");
			System.out.println("}\n");

			System.out.println("static inline void __fmt_Println(const char *s) {");
			System.out.println("\tprintf(\"%s\\n\", s);");
			System.out.println("}\n");

			System.out.println("static inline int __int_to_int(int i) {");
			System.out.println("\treturn i;");
			System.out.println("}\n");

			System.out.println("static inline char *__int_to_string(int i) {");
			System.out.println("\tchar *buf;\n");
			System.out.println("\t(void)asprintf(&buf, \"%c\", i);");
			System.out.println("\treturn buf;");
			System.out.println("}\n");

			System.out.println("#define len(s) strlen(s)\n");

			System.out.println("static inline char *__string_plus_string(const char *a, const char *b) {");
			System.out.println("\tchar *buf;\n");
			System.out.println("\t(void)asprintf(&buf, \"%s%s\", a, b);");
			System.out.println("\treturn buf;");
			System.out.println("}\n");

			for (CFuncStmt f : cu.funcs)
				dump(f);

			return;
		}

		// func
		if (tree instanceof CFuncStmt) {
			CFuncStmt func = (CFuncStmt)tree;

			System.out.print(func.retTypename + " " + func.name + "(");
			for (int i = 0; i < func.args.size(); i++) {
				CVarDecl decl = func.args.get(i);

				System.out.print(decl.typename + " " + decl.name);
				if (i < func.args.size() - 1)
					System.out.print(", ");
			}
			System.out.print(")\n");

			dump(func.body);

			return;
		}

		if (tree instanceof CBlock) {
			CBlock block = (CBlock)tree;

			printIndent(); System.out.print("{\n");
			indent++;

			for (CVarDecl decl : block.vars) {
				printIndent(); System.out.print(decl.typename + " " + decl.name + ";");
			}
			System.out.println();

			for (CStmt stmt : block.stmts) {
				dump(stmt);
				System.out.println();
			}

			indent--;
			printIndent(); System.out.print("}\n");
			return;
		}

		if (tree instanceof CIfStmt) {
			CIfStmt stmt = (CIfStmt)tree;

			printIndent();

			if (stmt.assign != null) {
				System.out.print("{\n");
				indent++;

				for (CVarDecl decl : stmt.vars) {
					printIndent(); System.out.print(decl.typename + " " + decl.name + ";\n");
				}

				printIndent();
				dump(stmt.assign);
				System.out.print("; ");
			}

			System.out.print("if (");
			dump(stmt.condition);
			System.out.print(")\n");
			dump(stmt.truePart);
			if (stmt.elsePart != null) {
				printIndent();
				System.out.print("else\n");
				dump(stmt.elsePart);
			}

			if (stmt.assign != null) {
				indent--;
				printIndent(); System.out.print("}\n");
			}

			return;
		}

		if (tree instanceof CForStmt) {
			CForStmt stmt = (CForStmt)tree;

			printIndent();

			if (stmt.init != null) {
				System.out.print("{\n");
				indent++;

				for (CVarDecl decl : stmt.vars) {
					printIndent(); System.out.print(decl.typename + " " + decl.name + ";\n");
				}

				printIndent();
				dump(stmt.init);
				System.out.print("; ");
			}

			System.out.print("for (;");
			dump(stmt.condition);
			System.out.print(";");
			if (stmt.step != null)
				dump(stmt.step);
			System.out.print(")\n");
			dump(stmt.block);

			if (stmt.init != null) {
				indent--;
				printIndent(); System.out.print("}\n");
			}

			return;
		}

		if (tree instanceof CExprStmt) {
			CExprStmt stmt = (CExprStmt)tree;

			printIndent();
			dump(stmt.expr);
			System.out.print(";");
			return;
		}

		// a X b
		if (tree instanceof CBinExpr) {
			CBinExpr bin = (CBinExpr)tree;

			System.out.print("(");
			dump(bin.left);
			System.out.print(" " + bin.op + " ");
			dump(bin.right);
			System.out.print(")");

			return;
		}

		if (tree instanceof CUnaryExpr) {
			CUnaryExpr unary = (CUnaryExpr)tree;

			System.out.print(unary.op + "(");
			dump(unary.expr);
			System.out.print(")");

			return;
		}

		if (tree instanceof CFuncCallExpr) {
			CFuncCallExpr func = (CFuncCallExpr)tree;

			System.out.print(func.name);
			System.out.print("(");

			for (int i = 0; i < func.args.size(); i++) {
				CExpr expr = func.args.get(i);

				dump(expr);
				if (i < func.args.size() - 1)
					System.out.print(", ");
			}

			System.out.print(")");

			return;
		}

		if (tree instanceof CReturnStmt) {
			CReturnStmt stmt = (CReturnStmt)tree;

			printIndent();
			System.out.print("return ");
			if (stmt.expr != null)
				dump(stmt.expr);
			System.out.print(";");

			return;
		}

		if (tree instanceof CArrayExpr) {
			CArrayExpr a = (CArrayExpr)tree;

			dump(a.expr);
			System.out.print("[");
			dump(a.index);
			System.out.print("]");

			return;
		}

		if (tree instanceof CIntExpr) {
			CIntExpr i = (CIntExpr)tree;
			System.out.print(i.value);

			return;
		}

		if (tree instanceof CCharExpr) {
			CCharExpr c = (CCharExpr)tree;
			System.out.print("'" + c.value + "'");

			return;
		}

		if (tree instanceof CStringExpr) {
			CStringExpr s = (CStringExpr)tree;
			System.out.print("\"" + s.value + "\"");

			return;
		}

		if (tree instanceof CVarExpr) {
			CVarExpr v = (CVarExpr)tree;
			System.out.print(v.name);

			return;
		}

		assert false;

	}

	private ArrayList<CVarDecl> goScopeToCVarDecl(GoScope sc) {
		ArrayList<CVarDecl> decls = new ArrayList<>();
		for (GoSymbol sym : sc.symbols) {
			if (sym.type == GoType.INT || sym.type == GoType.BOOL)
				decls.add(new CVarDecl(sym.name, "int"));
			else if (sym.type == GoType.STRING)
				decls.add(new CVarDecl(sym.name, "const char *"));
			else
				assert false;
		}
		return decls;
	}

	// yeah we are in a hurry, no visitor pattern folks...
	public CNode translate(GoNode tree) {

		// unit
		if (tree instanceof GoUnit) {
			GoUnit unit = (GoUnit)tree;

			CUnit cu = new CUnit();

			for (GoFuncStmt f : unit.funcs)
				cu.add((CFuncStmt)translate(f));

			return cu;
		}

		// func
		if (tree instanceof GoFuncStmt) {
			GoFuncStmt func = (GoFuncStmt)tree;

			String ret = "void";
			if (func.retTypename != null) {
				if (func.retTypename.equals("int") || func.retTypename.equals("bool"))
					ret = "int";
				else if (func.retTypename.equals("string"))
					ret = "const char *";
				else
					assert false;
			}

			ArrayList<CVarDecl> args = goScopeToCVarDecl(func.scope);

			CBlock body = (CBlock)translate(func.body);

			return new CFuncStmt(func.name, args, ret, body);
		}

		if (tree instanceof GoIfStmt) {
			GoIfStmt stmt = (GoIfStmt)tree;

			CExpr assign = null;
			if (stmt.assign != null) {
				CExprStmt es = (CExprStmt)translate(stmt.assign);
				assign = es.expr;
			}

			CExpr cond = (CExpr)translate(stmt.condition);
			CBlock truePart = (CBlock)translate(stmt.truePart);
			CBlock elsePart = null;
			if (stmt.elsePart != null)
				elsePart = (CBlock)translate(stmt.elsePart);

			CIfStmt ci = new CIfStmt(assign, cond, truePart, elsePart);
			ci.vars = goScopeToCVarDecl(stmt.scope);
			return ci;
		}

		if (tree instanceof GoForStmt) {
			GoForStmt stmt = (GoForStmt)tree;

			CExpr init = null;
			if (stmt.init != null) {
				CExprStmt es = (CExprStmt)translate(stmt.init);
				init = es.expr;
			}

			CExpr cond = null;
			if (stmt.condition != null)
				cond = (CExpr)translate(stmt.condition);

			CExpr step = null;
			if (stmt.step != null) {
				CExprStmt es = (CExprStmt)translate(stmt.step);
				step = es.expr;
			}

			CBlock body = (CBlock)translate(stmt.block);

			CForStmt cf = new CForStmt(init, cond, step, body);
			cf.vars = goScopeToCVarDecl(stmt.scope);
			return cf;
		}

		// block
		if (tree instanceof GoBlock) {
			GoBlock block = (GoBlock)tree;

			CBlock cb = new CBlock();

			for (GoStmt stmt : block.stmts)
				cb.add((CStmt)translate(stmt));

			cb.vars = goScopeToCVarDecl(block.scope);

			return cb;
		}

		if (tree instanceof GoAssignStmt) {
			GoAssignStmt stmt = (GoAssignStmt)tree;

			return new CExprStmt(new CBinExpr((CExpr)translate(stmt.lhs), "=", (CExpr)translate(stmt.rhs)));
		}

		if (tree instanceof GoReturnStmt) {
			GoReturnStmt stmt = (GoReturnStmt)tree;

			CExpr expr = null;
			if (stmt.expr != null)
				expr = (CExpr)translate(stmt.expr);

			return new CReturnStmt(expr);
		}

		// stmt <expr>
		if (tree instanceof GoExprStmt) {
			GoExprStmt stmt = (GoExprStmt)tree;

			return new CExprStmt((CExpr)translate(stmt.expr));
		}

		// X a
		if (tree instanceof GoUnaryExpr) {
			GoUnaryExpr unary = (GoUnaryExpr)tree;

			return new CUnaryExpr(unary.op.toString(), (CExpr)translate(unary.expr));
		}

		// a X b
		if (tree instanceof GoBinExpr) {
			GoBinExpr bin = (GoBinExpr)tree;

			CExpr left = (CExpr)translate(bin.left);
			CExpr right = (CExpr)translate(bin.right);

			// string + string
			if (bin.left.type == GoType.STRING) {
				ArrayList<CExpr> cargs = new ArrayList<>();
				cargs.add(left);
				cargs.add(right);
				return new CFuncCallExpr("__string_plus_string", cargs);
			}

			return new CBinExpr(left, bin.op.toString(), right);
		}

		if (tree instanceof GoVarExpr) {
			GoVarExpr v = (GoVarExpr)tree;

			return new CVarExpr(((GoVarExpr)tree).name);
		}

		if (tree instanceof GoFuncCallExpr) {
			GoFuncCallExpr call = (GoFuncCallExpr)tree;

			ArrayList<CExpr> cargs = new ArrayList<>();
			for (GoExpr expr : call.args)
				cargs.add((CExpr)translate(expr));

			String name = ((GoVarExpr)call.expr).name;
			if (name.equals("int"))
				name = "__int_to_int";
			else if (name.equals("string"))
				name = "__int_to_string";
			else if (name.equals("fmt.Print"))
				name = "__fmt_Print";
			else if (name.equals("fmt.Println"))
				name = "__fmt_Println";

			return new CFuncCallExpr(name, cargs);

		}

		if (tree instanceof GoArrayExpr) {
			GoArrayExpr a = (GoArrayExpr)tree;

			CExpr e = (CExpr)translate(a.expr);
			CExpr i = (CExpr)translate(a.index);

			return new CArrayExpr(e, i);
		}

		if (tree instanceof GoIntExpr) {
			return new CIntExpr(((GoIntExpr)tree).value);
		}

		if (tree instanceof GoStringExpr) {
			return new CStringExpr(((GoStringExpr)tree).value);
		}

		if (tree instanceof GoCharExpr) {
			return new CCharExpr(((GoCharExpr)tree).value);
		}

		assert false;

		return null;
	}
}
