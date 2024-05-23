package org.umg.compiladores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class FileService {

    private HashMap<String, Integer> lines = new HashMap<>();
    private List<String> allLines = new ArrayList<>();
    public String datos;
    public FileService() {
    }

    File parseFiled(File file) throws IOException {
        var newFile = new File("temp.txt");
        var scanner = new Scanner(file, StandardCharsets.UTF_8);

        var oneLine = new StringBuilder();
        int i = 1;
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            if (line.isEmpty()) continue;
            setLines(line, i);
            allLines.add(line);
            line = line.replaceAll("\n", "");
            oneLine.append(line);
            i++;
        }

        System.out.println(oneLine);
        datos = oneLine.toString();
        scanner.close();
        if (newFile.exists()) {
            if (newFile.delete()) System.out.println("Eliminado");

            newFile = new File("temp.txt");
            var fw = new FileWriter(newFile);
            var bw = new BufferedWriter(fw);
            bw.write(String.valueOf(oneLine));
            bw.flush();
            return newFile;
        }

        var fw = new FileWriter(newFile);
        var bw = new BufferedWriter(fw);
        bw.write(String.valueOf(oneLine));
        bw.flush();
        return newFile;
    }

    private void setLines(String line, int i) {
        lines.put(line.trim(), i);
    }

    HashMap<String, Integer> getLines() {
        return lines;
    }

    public void cleanHasMap() {
        this.lines = new HashMap<>();
    }
    
    public List<String> getAllLines() {
        return this.allLines;
    }
    
    public void cleanAllLines() {
        this.allLines = new ArrayList<>();
    }
}
