package org.umg.compiladores;

import org.umg.compiladores.dto.ClassifyTokenDTO;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class View {

    private final FileService fileService;
    String[] headers = {"Lexema", "Tipo", "Línea"};

    public View(FileService fileService) {
        this.fileService = fileService;
    }

    public void showResult(List<ClassifyTokenDTO> classifyTokenDTOS) {
        var data = formatData(classifyTokenDTOS);
        var table = new JTable(data, headers);
        var scrollPane = new JScrollPane(table);

        var frame = new JFrame("Resultado");
        frame.setSize(600, 400);
        frame.add(scrollPane);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileService.cleanHasMap();
            }
        });
    }

    private Object[][] formatData(List<ClassifyTokenDTO> dataList) {
        var data = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            var inlineData = dataList.get(i);
            data[i] = new Object[]{inlineData.getText(), inlineData.getToken(), inlineData.getLine()};
        }

        return data;
    }

}
