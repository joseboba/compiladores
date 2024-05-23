package org.umg.compiladores.ast;

import org.umg.compiladores.visitor.Visitor;

public class True implements Exp {
	public void accept(Visitor v) {
		v.visit(this);
	}
}
