package org.umg.compiladores.ast;

import org.umg.compiladores.visitor.Visitor;

public class False implements Exp {
	public void accept(Visitor v) {
		v.visit(this);
	}
}
