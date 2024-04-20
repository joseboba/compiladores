package org.umg.compiladores;
import static org.umg.compiladores.Token.*;
%%
%class Lexer
%type Token
L=[a-zA-Z\u00f1\u00d1]+
D=[0-9]+
O=[=|+|-|*|/|-|==]
A=[( | ) | { | }]
S=[!@#\$%\^&:;,.~¡¿?'`"\""]
N=("(-"{D}+")")|{D}+"."{D}+|{D}+
espacio=[ ,\t,\r,\n]+
%{
    public String lexeme;
%}
%%
"while" {lexeme=yytext(); return RESERVADAS; }
"for"   {lexeme=yytext(); return RESERVADAS; }
"if"    {lexeme=yytext(); return RESERVADAS; }
"do"    {lexeme=yytext(); return RESERVADAS; }
"else"  {lexeme=yytext(); return RESERVADAS; }
"int"   {lexeme=yytext(); return RESERVADAS; }
"class" {lexeme=yytext(); return RESERVADAS; }
"public" {lexeme=yytext(); return RESERVADAS; }
"true" {lexeme=yytext(); return RESERVADAS; }
"false" {lexeme=yytext(); return RESERVADAS; }
{espacio} {/*Ignore*/}
"//".* {/*Ignore*/}
"/*".*."*/" {/*Ignore*/}
{O} {return OPERADOR;}
{A} {return AGRUPADOR;}
{S} {return SIMBOLO;}
{L}({L}|{D})* {lexeme=yytext(); return IDENTIFICADOR;}
{N} {lexeme=yytext(); return NUMERO;}
{L}({L}|{N})*{S}+ { return ERROR; }
{N}({N}|{L})*{S}+ { return ERROR; }
{N}{L}+ { return ERROR; }
{D}+"."{L}+ { return ERROR; }
{L}+{D}+"."{D}+ {return ERROR;}
. {return ERROR;}