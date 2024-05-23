package org.umg.compiladores.ast;

import org.umg.compiladores.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class VarDeclList {
	private List<VarDecl> list;
	
	public VarDeclList() {
		list = new ArrayList<VarDecl>();
	}
	
	public void addElement(VarDecl varDecl) {
		getList().add(varDecl);
	}
	
	public VarDecl elementAt(int index) {
		return getList().get(index);
	}
	
	public int size() {
		return getList().size();
	}
	
	public List<VarDecl> getList() {
		return list;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}

	
}
