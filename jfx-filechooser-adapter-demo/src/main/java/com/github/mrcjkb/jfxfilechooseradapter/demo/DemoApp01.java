package com.github.mrcjkb.jfxfilechooseradapter.demo;

import com.github.mrcjkb.jfxfilechooseradapter.JfxFileChooserAdapterModuleContext;

import javax.swing.*;
import java.awt.*;

public class DemoApp01 extends JFrame {

    public DemoApp01() {
        getContentPane().setLayout(new BorderLayout());
        var fileChooserButton = new JButton("Choose file...");
        var textPane = new JTextPane();
        var label = new JLabel("Please choose a file...");
        textPane.setEditable(false);
        getContentPane().add(label, BorderLayout.NORTH);
        getContentPane().add(textPane, BorderLayout.CENTER);
        getContentPane().add(fileChooserButton, BorderLayout.EAST);
        var fileChooserAdapter = new JfxFileChooserAdapterModuleContext()
                .getFileChooserBuilder()
                .addToSwingParent(panel -> getContentPane().add(panel, BorderLayout.WEST)); // Invisible panel
        fileChooserButton.addActionListener(e -> {
            var file = fileChooserAdapter.init()
                    .withTitle("Choose a file...")
                    .showOpenFileDialog()
                    .getFile();
            if (null != file) {
                textPane.setText(file.getAbsolutePath());
                pack();
            }
        });
    }

    public static void main(String[] args) {
        var app = new DemoApp01();
        app.setVisible(true);
        app.pack();
    }
}
