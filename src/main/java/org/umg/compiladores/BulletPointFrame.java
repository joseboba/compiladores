package org.umg.compiladores;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BulletPointFrame extends JFrame {

    public BulletPointFrame(ArrayList<String> messages) {
        setTitle("Messages with Bullet Points");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        var textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

        var messageWithBullets = new StringBuilder();
        for (var message : messages) {
            messageWithBullets.append("• ").append(message).append("\n");
        }

        textArea.setText(messageWithBullets.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(scrollPane);
    }

}