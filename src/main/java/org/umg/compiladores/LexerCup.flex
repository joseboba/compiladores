package org.umg.compiladores;
import java_cup.runtime.Symbol;
%%
%class LexerCup
%type java_cup.runtime.Symbol
%cup
%full
%line
%char
L=[a-zA-Z\u00f1\u00d1]+
D=[0-9]+
O=[=|+|-|*|/,==]
A=[( | ) | { | }]
S=["!" | "@" | "#" | "$" | "%" | "^" | "&" | ":" | ";" | "," | "." | "~" | "¡" | "¿" | "?" | "'" | ","]
R=[if,else,while,for, true, false, int]
espacio=[ ,\t,\r,\n]+
%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }

    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
%}
%%
{R} {return new Symbol(sym.RESERVADAS, yychar, yyline, yytext());}
{espacio} {/*Ignore*/}
"//".* {/*Ignore*/}
"/*".*."*/" {/*Ignore*/}
{O} {return new Symbol(sym.OPERADOR, yychar, yyline, yytext());}
{A} {return new Symbol(sym.AGRUPADOR, yychar, yyline, yytext());} 
{S} {return new Symbol(sym.SIMBOLO, yychar, yyline, yytext());}
{L}({L}|{D})* {return new Symbol(sym.IDENTIFICADOR, yychar, yyline, yytext());}
("(-"{D}+")")|{D}+"."{D}+|{D}+ {return new Symbol(sym.NUMERO, yychar, yyline, yytext());}
"" {return new Symbol(sym.ERROR, yychar, yyline, yytext());}