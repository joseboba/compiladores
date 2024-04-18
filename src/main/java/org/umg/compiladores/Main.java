package org.umg.compiladores;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var fileService = new FileService();
        var resultService = new ResultService(fileService);
        var view = new View(fileService);
        var result = resultService.result();
        view.showResult(result);
    }
} 