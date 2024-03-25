package org.umg.compiladores;



import javax.swing.*;
import java.io.*;
import java.util.Objects;

public class ResultService {

    public ResultService() {
        GenerateLexerService.readLexer();
    }

    public String result() throws IOException {
        var fileChooser = new JFileChooser();
        var result = "";
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            result = classifyToken(fileChooser.getSelectedFile());
        }
        return result;
    }

    private String classifyToken(File file) throws IOException {
        var reader = new BufferedReader(new FileReader(file));
        var lexer = new Lexer(reader);
        var result = new StringBuilder();
        while (true) {
            var tokens = lexer.yylex();
            if (tokens == null) {
                break;
            }
            switch (Objects.requireNonNull(tokens)) {
                case RESERVADAS:
                case IGUAL:
                case SUMA:
                case RESTA:
                case MULTIPLICACION:
                case DIVISION:
                case IDENTIFICADOR:
                case NUMERO:
                case NUMERO_DECIMAL:
                case PARENTESIS_INICIO:
                case PARENTESIS_FINAL:
                case PUNTO:
                    result.append(lexer.yytext()).append(": Es ").append(tokens).append("\n");
                    break;
                default:
                    result.append("Simbolo no definido ").append(lexer.yytext()).append("\n");
                    break;
            }
        }
        return String.valueOf(result);
    }

}
