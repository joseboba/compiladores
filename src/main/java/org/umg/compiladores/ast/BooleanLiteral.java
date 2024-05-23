package org.umg.compiladores.ast;

import org.umg.compiladores.visitor.Visitor;

public class BooleanLiteral implements Exp {
	private boolean value;
	
	public BooleanLiteral(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}
}
