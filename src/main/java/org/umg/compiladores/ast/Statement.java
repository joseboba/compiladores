package org.umg.compiladores.ast;

import org.umg.compiladores.visitor.Visitor;

public interface Statement {
	public void accept(Visitor v);
}
