package org.umg.compiladores.ast;

import org.umg.compiladores.visitor.Visitor;

public class CharLiteral implements Exp {
	private char value;
	
	public CharLiteral(char value) {
		this.value = value;
	}
	
	public char getValue() {
		return value;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}

}
