package org.umg.compiladores;
import static org.umg.compiladores.Token.*;
%%
%class Lexer
%type Token
L=[a-zA-Z\u00f1\u00d1]+
D=[0-9]+
O=[=|+|-|*|/]
A=[( | ) | { | }]
S=["!" | "@" | "#" | "$" | "%" | "^" | "&" | ":" | ";" | "," | "." | "~" | "¡" | "¿" | "?" | "'" | ","]
R=[if,else,while,for]
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
("(-"{D}+")")|{D}+"."{D}+|{D}+ {lexeme=yytext(); return NUMERO;}
"" {return ERROR;}