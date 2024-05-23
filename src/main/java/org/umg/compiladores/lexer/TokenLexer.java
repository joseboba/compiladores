package org.umg.compiladores.lexer;

public class TokenLexer {
	private TokenType type; //token type
	private TokenAttribute attribute; //token attribute 
	private int lineNumber; //token line number
	private int columnNumber; //token column number

	public TokenLexer(TokenType type, TokenAttribute attribute, int lineNumber, int columnNumber){
		this.type = type;
		this.attribute = attribute;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}

	public TokenType getType(){
		return type;
	}
	
	public TokenAttribute getAttribute(){
		return attribute;
	}

	public int getLineNumber(){
		return lineNumber;
	}

	public int getColumnNumber(){
		return columnNumber;
	}
}