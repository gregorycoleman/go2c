all:
	javac CArrayExpr.java CStmt.java GoForStmt.java GoToC.java \
		CBinExpr.java CStringExpr.java GoFuncCallExpr.java GoType.java \
		CBlock.java CUnaryExpr.java GoFuncStmt.java GoUnaryExpr.java \
		CCharExpr.java CUnit.java GoFuncType.java GoUnit.java \
		CExpr.java CVarDecl.java GoIfStmt.java GoVarDecl.java \
		CExprStmt.java CVarExpr.java GoIntExpr.java GoVarExpr.java \
		CForStmt.java GoArrayExpr.java GoInvalidExpr.java Lexer.java \
		CFuncCallExpr.java GoAssignStmt.java GoNode.java Main.java \
		CFuncStmt.java GoBinExpr.java GoReturnStmt.java Parser.java \
		CIfStmt.java GoBlock.java GoScope.java Token.java \
		CIntExpr.java GoCharExpr.java GoStmt.java TreeCheck.java \
		CNode.java GoExpr.java GoStringExpr.java CReturnStmt.java \
		GoExprStmt.java GoSymbol.java

clean:
	rm *.class
