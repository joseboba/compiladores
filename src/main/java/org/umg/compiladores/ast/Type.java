package org.umg.compiladores.ast;


import org.umg.compiladores.visitor.Visitor;

public interface Type {
	public void accept(Visitor v);
}
