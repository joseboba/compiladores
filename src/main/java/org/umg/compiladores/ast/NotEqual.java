package org.umg.compiladores.ast;

import org.umg.compiladores.visitor.Visitor;

public class NotEqual implements Exp{
	private Exp lhs, rhs;

	public NotEqual(Exp lhs, Exp rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public Exp getLHS() {
		return lhs;
	}

	public Exp getRHS() {
		return rhs;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
