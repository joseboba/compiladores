package org.umg.compiladores;

import java.io.File;

public class GenerateLexerService {

    public static void readLexer() {
        var relativeRoute = "src/main/java/org/umg/compiladores/Lexer.flex";
        var relativeFile = new File(relativeRoute);
        generateLexer(relativeFile.getAbsolutePath());
    }

    private static void generateLexer(String route) {
        var file = new File(route);
        JFlex.Main.generate(file);
    }

}
