package org.umg.compiladores.semantic;

import lombok.Getter;
import org.umg.compiladores.ast.*;
import org.umg.compiladores.parser.Parser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SemanticAnalyzer {

	private Parser parser;
	private ArrayList<VarDecl> declerations;
	private ArrayList<Identifier> identifiers;
	private ArrayList<Assign> assigns;
	private ArrayList<Exp> conditions;
	@Getter
	private ArrayList<String> listErrors = new ArrayList<>();

	// get number of errors
	@Getter
	private int errors;

	public SemanticAnalyzer(FileReader file) throws IOException{
		this.parser = new Parser(file);
	}

	//start semantic analyzer
	public void analyzeProgram() throws IOException{
		this.parser.parseProgram();
		this.declerations = this.parser.getDecelarations();
		checkDeclerations();
		this.identifiers = this.parser.getIdentifiers();
		checkIdenifiers();
		this.assigns = this.parser.getAssigns();
		checkAssigns();
		this.conditions = this.parser.getConditions();
		checkConditions();	
	}

	// check program declarations
	private void checkDeclerations(){
		for(int i = 0; i < declerations.size(); i++){
			VarDecl varDecl = declerations.get(i);
			String idName = varDecl.getId().getName();

			for(int j = i + 1; j < declerations.size(); j ++){
				VarDecl _varDecl = declerations.get(j);
				String _idName = _varDecl.getId().getName();

				if(idName.equals(_idName))
					error(ErrorType.MULTIPLE_DECLARATION, _idName);
			}
		}
	}

	// check program identifiers
	private void checkIdenifiers(){
		for (Identifier identifier : identifiers) {
			if(!isIdentifierExists(identifier.getName()))
				error(ErrorType.NO_DECLARATION, identifier.getName());
		}
	}
	
	// check program conditions
	private void checkConditions(){
		for (Exp exp : conditions) {
			if((exp instanceof MoreThan || exp instanceof MoreThanEqual ||exp instanceof LessThan ||
					exp instanceof LessThanEqual || exp instanceof NotEqual || exp instanceof Equal))
				error(ErrorType.INVALID_CONDITION, null);
		}
		
	}
	
	// check if a specific identifier name is exists
	private boolean isIdentifierExists(String name){
		for (VarDecl varDecl : declerations) {
			String idName = varDecl.getId().getName();

			if(idName.equals(name))
				return true;
		}
		return false;
	}

	// type checking of all the assign expressions
	private void checkAssigns(){
		for (Assign assign : assigns) {
			Exp type = assign.getValue();
			String idName = assign.getId().getName();
			Type idType = getIdentifierType(idName);

			// assign to int
			if(idType != null && (idType instanceof IntegerType || idType instanceof IntegerArrayType)){

				// float to int
				if(type instanceof FloatLiteral)
					error(ErrorType.FLOAT_INT_CASTING, idName);

				// boolean to int
				if(type instanceof BooleanLiteral)
					error(ErrorType.BOOLEAN_INT_CASTING, idName);

				// type(id) to int
				if(type instanceof IdentifierExp){
					String _idName = ((IdentifierExp) type).getName();
					Type _idType = getIdentifierType(_idName);

					if(_idType != null){
						// float to int
						if (_idType instanceof FloatType)
							error(ErrorType.FLOAT_INT_CASTING, idName);

						// boolean to int
						else if( _idType instanceof BooleanType)
							error(ErrorType.BOOLEAN_INT_CASTING, idName);

						if(idType instanceof IntegerType)
							// identifier with array type
							if (_idType instanceof FloatArrayType || _idType instanceof BooleanArrayType
									|| _idType instanceof IntegerArrayType || _idType instanceof CharArrayType)
								error(ErrorType.ARRAY_TO_SINGLE, idName);

						if(idType instanceof IntegerArrayType)
							// identifier with single type
							if (_idType instanceof FloatType || _idType instanceof BooleanType
									|| _idType instanceof IntegerType || _idType instanceof CharType)
								error(ErrorType.SINGLE_TO_ARRAY, idName);

					}

				}
			}

			// assign to float
			if(idType != null && (idType instanceof FloatType || idType instanceof FloatArrayType)){

				// boolean to float
				if(type instanceof BooleanLiteral)
					error(ErrorType.BOOLEAN_FLOAT_CASTING, idName);

				// char to float
				if(type instanceof CharLiteral)
					error(ErrorType.CHAR_FLOAT_CASTING, idName);

				if(type instanceof IdentifierExp){

					String _idName = ((IdentifierExp) type).getName();
					Type _idType = getIdentifierType(_idName);

					if(_idType != null){
						// boolean to float
						if (_idType instanceof BooleanType)
							error(ErrorType.BOOLEAN_FLOAT_CASTING, idName);

						// char to float
						else if( _idType instanceof CharType)
							error(ErrorType.CHAR_FLOAT_CASTING, idName);

						if(idType instanceof FloatType)
							// identifier with array type
							if (_idType instanceof FloatArrayType || _idType instanceof BooleanArrayType
									|| _idType instanceof IntegerArrayType || _idType instanceof CharArrayType)
								error(ErrorType.ARRAY_TO_SINGLE, idName);

						if(idType instanceof FloatArrayType)
							// identifier with single type
							if (_idType instanceof FloatType || _idType instanceof BooleanType
									|| _idType instanceof IntegerType || _idType instanceof CharType)
								error(ErrorType.SINGLE_TO_ARRAY, idName);
					}
				}
			}

			// assign to char
			if(idType != null && (idType instanceof CharType || idType instanceof CharArrayType)){

				//int to char
				if(type instanceof IntegerLiteral)
					error(ErrorType.INT_CHAR_CASTING, idName);

				// float to char
				if(type instanceof FloatLiteral)
					error(ErrorType.FLOAT_CHAR_CASTING, idName);

				// boolean to char
				if(type instanceof BooleanLiteral)
					error(ErrorType.BOOLEAN_CHAR_CASTING, idName);

				// type(id) to int
				if(type instanceof IdentifierExp){
					String _idName = ((IdentifierExp) type).getName();
					Type _idType = getIdentifierType(_idName);

					if(_idType != null){

						// float to char
						if (_idType instanceof FloatType)
							error(ErrorType.FLOAT_CHAR_CASTING, idName);

						// int to char
						else if (_idType instanceof IntegerType)
							error(ErrorType.INT_CHAR_CASTING, idName);

						// boolean to char
						else if( _idType instanceof BooleanType)
							error(ErrorType.BOOLEAN_CHAR_CASTING, idName);

						if(idType instanceof CharType)
							// identifier with array type
							if (_idType instanceof FloatArrayType || _idType instanceof BooleanArrayType
									|| _idType instanceof IntegerArrayType || _idType instanceof CharArrayType)
								error(ErrorType.ARRAY_TO_SINGLE, idName);

						if(idType instanceof CharArrayType)
							// identifier with single type
							if (_idType instanceof FloatType || _idType instanceof BooleanType
									|| _idType instanceof IntegerType || _idType instanceof CharType)
								error(ErrorType.SINGLE_TO_ARRAY, idName);
					}

				}
			}

			// assign to boolean
			if(idType != null && (idType instanceof BooleanType || idType instanceof BooleanArrayType)){
				//int to boolean
				if(type instanceof IntegerLiteral)
					error(ErrorType.INT_BOOLEAN_CASTING, idName);

				// float to boolean
				if(type instanceof FloatLiteral)
					error(ErrorType.FLOAT_BOOLEAN_CASTING, idName);

				// char to boolean
				if(type instanceof CharLiteral)
					error(ErrorType.CHAR_BOOLEAN_CASTING, idName);

				// type(id) to int
				if(type instanceof IdentifierExp){
					String _idName = ((IdentifierExp) type).getName();
					Type _idType = getIdentifierType(_idName);

					if(_idType != null){

						// float to boolean
						if (_idType instanceof FloatType)
							error(ErrorType.FLOAT_CHAR_CASTING, idName);

						// int to boolean
						else if (_idType instanceof IntegerType)
							error(ErrorType.INT_CHAR_CASTING, idName);

						// char to boolean
						else if( _idType instanceof CharType)
							error(ErrorType.CHAR_BOOLEAN_CASTING, idName);

						if(idType instanceof BooleanType)
							// identifier with array type
							if (_idType instanceof FloatArrayType || _idType instanceof BooleanArrayType
									|| _idType instanceof IntegerArrayType || _idType instanceof CharArrayType)
								error(ErrorType.ARRAY_TO_SINGLE, idName);

						if(_idType instanceof BooleanArrayType)
							// identifier with single type
							if (_idType instanceof FloatType || _idType instanceof BooleanType
									|| _idType instanceof IntegerType || _idType instanceof CharType)
								error(ErrorType.SINGLE_TO_ARRAY, idName);
					}

				}
			}

		}
	}

	// get identifier type (IntegerType | IntegerArrayType | FloatType | FloatArrayType | CharType | CharArrayType)
	private Type getIdentifierType(String name){
		for (VarDecl dec : declerations) {
			Identifier id = dec.getId();
			if(id.getName().equals(name))
				return dec.getType();
		}

		return null;
	}

	// print errors report
	private void error(ErrorType errorType, Object parm){
		errors++;
		switch (errorType) {
		case MULTIPLE_DECLARATION:
			listErrors.add("Error: Multiple declaración, variable (" + (String) parm + ")");
			System.err.println("Declaration Error: MULTIPLE_DECLARATION, variable (" + (String) parm + ")");
			break;
		case NO_DECLARATION:
			listErrors.add("Error: No hubo declaración, variable (" + (String) parm + ")");
			System.err.println("Declaration Error: NO_DECLARATION, variable (" + (String) parm + ")");
			break;
		case FLOAT_INT_CASTING:
			listErrors.add("Error: float no compatible con int, variable (" + (String) parm + ")");
			System.err.println("Casting Error: FLOAT_INT_CASTING, variable (" + parm + ")");
			break;
		case BOOLEAN_INT_CASTING:
			listErrors.add("Error: boolean no compatible con int, variable (" + (String) parm + ")");
			System.err.println("Casting Error: BOOLEAN_INT_CASTING, variable (" + parm + ")");
			break;
		case INT_BOOLEAN_CASTING:
			listErrors.add("Error: int no compatible con boolean, variable (" + (String) parm + ")");
			System.err.println("Casting Error: INT_BOOLEAN_CASTING, variable (" + parm + ")");
			break;
		case BOOLEAN_FLOAT_CASTING:
			listErrors.add("Error: boolean no compatible con float, variable (" + (String) parm + ")");
			System.err.println("Casting Error: BOOLEAN_FLOAT_CASTING, variable (" + parm + ")");
			break;
		case FLOAT_BOOLEAN_CASTING:
			listErrors.add("Error: float no compatible con boolean, variable (" + (String) parm + ")");
			System.err.println("Casting Error: FLOAT_BOOLEAN_CASTING, variable (" + parm + ")");
			break;
		case CHAR_FLOAT_CASTING:
			listErrors.add("Error: char no compatible con float, variable (" + (String) parm + ")");
			System.err.println("Casting Error: CHAR_FLOAT_CASTING, variable (" + parm + ")");
			break;
		case CHAR_BOOLEAN_CASTING:
			listErrors.add("Error: char no compatible con boolean, variable (" + (String) parm + ")");
			System.err.println("Casting Error: CHAR_BOOLEAN_CASTING, variable (" + parm + ")");
			break;
		case FLOAT_CHAR_CASTING:
			listErrors.add("Error: float no compatible con char, variable (" + (String) parm + ")");
			System.err.println("Casting Error: FLOAT_CHAR_CASTING, variable (" + parm + ")");
			break;
		case BOOLEAN_CHAR_CASTING:
			listErrors.add("Error: boolean no compatible con char, variable (" + (String) parm + ")");
			System.err.println("Casting Error: BOOLEAN_CHAR_CASTING, variable (" + parm + ")");
			break;
		case INT_CHAR_CASTING:
			listErrors.add("Error: int no compatible con char, variable (" + (String) parm + ")");
			System.err.println("Casting Error: INT_CHAR_CASTING, variable (" + parm + ")");
			break;
		case ARRAY_TO_SINGLE:
			System.err.println("Invalid Assignment: ARRAY_TO_SINGLE, variable (" + parm + ")");
			break;
		case SINGLE_TO_ARRAY:
			System.err.println("Invalid Assignment: SINGLE_TO_ARRAY, variable (" + parm + ")");
			break;
		case INVALID_CONDITION:
			System.err.println("Invalid Condition: INVALID_CONDITION");
			break;
		default:
			break;
		}
	}
}