package org.umg.compiladores.ast;

import org.umg.compiladores.visitor.Visitor;

public class IntegerLiteral implements Exp {
	private int value;
	
	public IntegerLiteral(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}
}
