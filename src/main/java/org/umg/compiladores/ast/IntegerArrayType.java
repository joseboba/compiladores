package org.umg.compiladores.ast;

import org.umg.compiladores.visitor.Visitor;

public class IntegerArrayType implements Type {
	public void accept(Visitor v) {
		v.visit(this);
	}
}
