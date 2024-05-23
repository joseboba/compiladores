package org.umg.compiladores.ast;

import org.umg.compiladores.visitor.Visitor;

public class FloatLiteral implements Exp{
	private float value;

	public FloatLiteral(float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
