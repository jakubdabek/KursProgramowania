package com.dabek.jakub.ezgui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class RomanArabicGUI extends JFrame {

    JTextField input;
    JTextArea output;
    JRadioButton romanToArabicCheckBox, arabicToRomanCheckBox;

    RomanArabicGUI(String title, Font font, Color backgroundColor) {
        super(title);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        input = new JTextField();
        input.addActionListener(new MyRzybArabListener());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(input, constraints);

        JPanel checkBoxPanel = new JPanel();
        ButtonGroup group = new ButtonGroup();
        romanToArabicCheckBox = new JRadioButton("Roman to arabic", true);
        arabicToRomanCheckBox = new JRadioButton("Arabic to roman");
        group.add(romanToArabicCheckBox);
        group.add(arabicToRomanCheckBox);
        checkBoxPanel.add(romanToArabicCheckBox);
        checkBoxPanel.add(arabicToRomanCheckBox);

        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.LINE_START;
        panel.add(checkBoxPanel, constraints);
        
        output = new JTextArea(20, 80);
        output.setEditable(false);
        constraints.gridy = 2;
        constraints.weighty = 0.5;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(output, constraints);

        add(panel);

        App.changeFont(this, font);
        App.changeBackgroundColor(this, backgroundColor);

        pack();
        setLocationRelativeTo(null);
    }


    class MyRzybArabListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String mode = romanToArabicCheckBox.isSelected() ? "R" : "A";
            Process process = null;
            InputStream stream = null;
            Scanner s = null;
            try {
                //System.err.println("Working Directory = " + System.getProperty("user.dir"));
                File file = new File("src/RomanArabic");
                File currentDir = new File(".");
                System.err.println(file.getAbsolutePath());
                System.err.println(currentDir.getAbsolutePath());
                process = Runtime.getRuntime().exec("./program.exe " + mode + " " + input.getText(), null, file);
                stream = process.getInputStream();
                s = new Scanner(stream).useDelimiter("\\A");
                output.setText(s.hasNext() ? s.next() : "");
                stream.close();
                stream = process.getErrorStream();
                s = new Scanner(stream).useDelimiter("\\A");
                output.append(s.hasNext() ? s.next() : "");
                process.waitFor();
            } catch (IOException | InterruptedException ex) {
                output.setText(ex.getMessage());
            } finally {
                if(stream != null)
                    s.close();
            }
        }

    }

}