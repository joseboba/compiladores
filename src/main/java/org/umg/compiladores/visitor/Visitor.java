package org.umg.compiladores.visitor;


import org.umg.compiladores.ast.*;

public interface Visitor {
	public void visit(Program prog);
	public void visit(VarDecl var);
	public void visit(VarDeclList varList);
	public void visit(Declarations dec);
	public void visit(Formal param);
	public void visit(IntegerArrayType intArrayT);
	public void visit(FloatArrayType floatArrayT);
	public void visit(BooleanArrayType booleanArrayT);
	public void visit(CharArrayType charArrayT);
	public void visit(BooleanType boolT);
	public void visit(IntegerType intT);
	public void visit(FloatType floatT);
	public void visit(CharType charT);
	public void visit(IdentifierType idT);
	public void visit(Block blockStm);
	public void visit(If ifStm);
	public void visit(While whileStm);
	public void visit(Assign assignStm);
	public void visit(ArrayAssign arrayAssignStm);
	public void visit(And andExp);
	public void visit(Or orExp);
	public void visit(MoreThan moreExp);
	public void visit(LessThan lessThanExp);
	public void visit(Equal equalExp);
	public void visit(NotEqual notEqualExp);
	public void visit(MoreThanEqual moreEqualExp);
	public void visit(LessThanEqual lessEqualExp);
	public void visit(Plus plusExp);
	public void visit(Minus minusExp);
	public void visit(Times timesExp);
	public void visit(Divide divExp);
	public void visit(Modules modExp);
	public void visit(ArrayLookup arrayLookup);
	public void visit(ArrayLength length);
	public void visit(IntegerLiteral intLiteral);
	public void visit(FloatLiteral floatLiteral);
	public void visit(BooleanLiteral booleanLiteral);
	public void visit(CharLiteral charLiteral);
	public void visit(True trueLiteral);
	public void visit(False falseLiteral);
	public void visit(IdentifierExp identExp);
	public void visit(NewArray array);
	public void visit(Not notExp);
	public void visit(Negative negExp);
	public void visit(Identifier id);
}