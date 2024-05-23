package org.umg.compiladores.ast;

import org.umg.compiladores.visitor.Visitor;

public interface Exp {
	public void accept(Visitor v);
}
