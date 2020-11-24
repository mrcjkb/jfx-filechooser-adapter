package com.github.mrcjkb.jfxfilechooseradapter.demo;

import com.github.mrcjkb.jfxfilechooseradapter.JfxFileChooserAdapterModuleContext;

import javax.swing.*;
import java.awt.*;

public class DemoApp01 extends JFrame {

    public DemoApp01() {
        getContentPane().setLayout(new BorderLayout());
        var fileChooserButton = new JButton("Choose file...");
        var textPane = new JTextPane();
        textPane.setEditable(false);
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
            textPane.setText(file.getAbsolutePath());
        });


    }

    public static void main(String[] args) {
        new DemoApp01().setVisible(true);
    }
}
