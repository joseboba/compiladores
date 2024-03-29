package org.umg.compiladores;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java_cup.parser;
import java_cup.runtime.*;

public class GenerateLexerService {

    public static void readLexer() throws Exception {
        var relativeRoute = "src/main/java/org/umg/compiladores/Lexer.flex";
        var relativeRouteToLexerCup = "src/main/java/org/umg/compiladores/LexerCup.flex";
        var relativeRouteToSintax = "src/main/java/org/umg/compiladores/Sintax.cup";
        
        var relativeSintaxFile = new File(relativeRouteToSintax);
        String[] routes = {"-parser","Sintax",relativeSintaxFile.getAbsolutePath()};
        var relativeFile = new File(relativeRoute);
        var relativeFileSyntax = new File(relativeRouteToLexerCup);
        generateLexerAndSyntax(relativeFile.getAbsolutePath(),relativeFileSyntax.getAbsolutePath(),routes);
    }

    private static void generateLexerAndSyntax(String route1, String route2, String[] routeToSyntax) throws IOException, Exception {
        
        var file = new File(route1);
        JFlex.Main.generate(file);
        var file2 = new File(route2);
        JFlex.Main.generate(file2);
        System.out.println("ruta " + routeToSyntax[2]);
        java_cup.Main.main(routeToSyntax);
   
        /*
        Path symbolsRoute = Paths.get("D:/Documentos 2024/UMG/Septimo semestre/Compiladores/Analizador sintactico/ProyectoArreglado/compiladores/src/main/java/org/umg/compiladores/sym.java");
        
        if(Files.exists(symbolsRoute)){
            Files.delete(symbolsRoute);
        }
        
      Files.move(
      Paths.get("D:/Documentos 2024/UMG/Septimo semestre/Compiladores/Analizador sintactico/ProyectoArreglado/compiladores/sym.java"), 
      Paths.get("D:/Documentos 2024/UMG/Septimo semestre/Compiladores/Analizador sintactico/ProyectoArreglado/compiladores/src/main/java/org/umg/compiladores/sym.java")
        );
        
        Files.move(
      Paths.get("D:/Documentos 2024/UMG/Septimo semestre/Compiladores/Analizador sintactico/ProyectoArreglado/compiladores/Syntax.java"), 
      Paths.get("D:/Documentos 2024/UMG/Septimo semestre/Compiladores/Analizador sintactico/ProyectoArreglado/compiladores/src/main/java/org/umg/compiladores/Syntax.java")
        );*/
    }

}
