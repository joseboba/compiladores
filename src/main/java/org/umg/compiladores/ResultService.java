package org.umg.compiladores;



import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class ResultService {

    public ResultService() {
        GenerateLexerService.readLexer();
    }

    public String result() throws IOException {
        var fileChooser = new JFileChooser();
        var result = "";
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            result = classifyToken(parseFiled(fileChooser.getSelectedFile()));
        }
        return result;
    }

    private File parseFiled(File file) throws IOException {
        int n;
        var newFile = new File("/tmp/temp.txt");
        var scanner = new Scanner(file, StandardCharsets.UTF_8);

        var oneLine = new StringBuilder();
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            if (line.isEmpty()) continue;
            line = line.replaceAll("\n", "");
            oneLine.append(line);
        }

        System.out.println(oneLine);
        scanner.close();

        if (newFile.exists()) {
            if (newFile.delete()) System.out.println("Eliminado");;
        } else {
            newFile = new File("/tmp/temp.txt");
            var fw = new FileWriter(newFile);
            var bw = new BufferedWriter(fw);
            bw.write(String.valueOf(oneLine));
            bw.flush();
        }

        return newFile;
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
