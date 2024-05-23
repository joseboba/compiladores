package org.umg.compiladores.ast;


import org.umg.compiladores.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class Declarations {
	private List<VarDeclList> list;

	public Declarations() {
		list = new ArrayList<VarDeclList>();
	}

	public void addElement(VarDeclList varDecl) {
		getList().add(varDecl);
	}

	public VarDeclList elementAt(int index) {
		return getList().get(index);
	}

	public int size() {
		return getList().size();
	}

	public List<VarDeclList> getList() {
		return list;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}
