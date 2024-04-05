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
R=if else while for
N=("(-"{D}+")")|{D}+"."{D}+|{D}+
espacio=[ ,\t,\r,\n]+
%{
    public String lexeme;
%}
%%
{R} {return RESERVADAS;}
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