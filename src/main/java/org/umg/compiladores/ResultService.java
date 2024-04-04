package org.umg.compiladores;


import org.umg.compiladores.dto.ClassifyTokenDTO;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResultService {

    private final FileService fileService;

    public ResultService(FileService fileService) {
        GenerateLexerService.readLexer();
        this.fileService = fileService;
    }

    public List<ClassifyTokenDTO> result() throws IOException {
        var fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return classifyToken(fileService.parseFiled(fileChooser.getSelectedFile()));
        }

        result();
        return new ArrayList<>();
    }

    private List<ClassifyTokenDTO> classifyToken(File file) throws IOException {
        var reader = new BufferedReader(new FileReader(file));
        var lexer = new Lexer(reader);
        var result = new ArrayList<ClassifyTokenDTO>();

        while (true) {
            var token = lexer.yylex();
            if (token == null) {
                break;
            }
            switch (Objects.requireNonNull(token)) {
                case RESERVADAS:
                case OPERADOR:
                case IDENTIFICADOR:
                case NUMERO:
                case AGRUPADOR:
                case SIMBOLO:
                    var classifyToken = new ClassifyTokenDTO(lexer.yytext().trim(), token, searchLine(lexer.yytext().trim()));
                    result.add(classifyToken);
                    break;
                default:
                    System.out.println(token.name() + " " + lexer.yytext());
                    break;
            }
        }

        return result;
    }

    private Integer searchLine(String line) {
        for (var entry : this.fileService.getLines().entrySet()) {
            var key = entry.getKey();
            if (key.contains(line)) {
                return entry.getValue();
            }
        }

        return null;
    }

}
