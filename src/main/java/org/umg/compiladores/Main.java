package org.umg.compiladores;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var resultService = new ResultService();
        var result = resultService.result();

        var frame = new JFrame("Resultado");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var textPane = new JTextPane();
        var documentStyle = textPane.getStyledDocument();
        var centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_JUSTIFIED);
        textPane.setText(result);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

        frame.add(textPane);
        frame.setVisible(true);
    }
}