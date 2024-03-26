package org.umg.compiladores;
import static org.umg.compiladores.Tokens.*;
%%
%class Lexer
%type Tokens
L=[a-zA-Z_]+
D=[0-9]+
espacio=[ ,\t,\r,\n]+
%{
    public String lexeme;
%}
%%
int |
if |
else |
while {lexeme=yytext(); return RESERVADAS;}
{espacio} {/*Ignore*/}
"//".* {/*Ignore*/}
"/*".*."*/" {/*Ignore*/}
"=" {return IGUAL;}
"+" {return SUMA;}
"-" {return RESTA;}
"*" {return MULTIPLICACION;}
"/" {return DIVISION;}
"(" {return PARENTESIS_INICIO;}
")" {return PARENTESIS_FINAL;}
"." {return PUNTO;}
{L}({L}|{D})* {lexeme=yytext(); return IDENTIFICADOR;}
("(-"{D}+")")|{D}+"."{D}+|{D}+ {lexeme=yytext(); return NUMERO;}
"" {return ERROR;}