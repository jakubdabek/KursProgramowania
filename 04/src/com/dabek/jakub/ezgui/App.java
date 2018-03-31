package com.dabek.jakub.ezgui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.MenuBar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import say.swing.JFontChooser;

public class App extends JFrame {

    Font mainFont;
    JButton primeNumbersButton, romanArabicButton;

    public static void changeFont(Component component, Font f) {
        //Font f = component.getFont();
        component.setFont(f);//new Font(f.getName(),f.getStyle(),f.getSize() + fontSize));
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                changeFont(child, f);
            }
        }
    }

    App(String title) {
        super(title);
        mainFont = getFont();
        setPreferredSize(new Dimension(300, 200));
        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("View");
        JMenuItem changeFontMenuItem = new JMenuItem("Change font");
        changeFontMenuItem.addActionListener(e -> {
            String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

            // for (int i = 0; i < fonts.length; i++) {
            //     System.err.println(fonts[i]);
            // }

            JFontChooser fontChooser = new JFontChooser();
            int result = fontChooser.showDialog(App.this);
            if (result == JFontChooser.OK_OPTION)
            {
                mainFont = fontChooser.getSelectedFont(); 
                System.out.println("Selected Font : " + mainFont);
                changeFont(App.this, mainFont);
                // invalidate();
                // repaint();
            }
        });
        viewMenu.add(changeFontMenuItem);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        
        add(panel, BorderLayout.CENTER);
        primeNumbersButton = new JButton("Prime numbers");
        primeNumbersButton.addActionListener(e -> {
            PrimeNumbersGUI gui = new PrimeNumbersGUI(e.getActionCommand(), mainFont);
            gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gui.setVisible(true);
        });
        romanArabicButton = new JButton("Roman arabic conversion");
        romanArabicButton.addActionListener(e -> {
            RomanArabicGUI gui = new RomanArabicGUI(e.getActionCommand(), mainFont);
            gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gui.setVisible(true);
        });
        
        panel.add(primeNumbersButton);
        primeNumbersButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(romanArabicButton);
        romanArabicButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        final App frame = new App("App xd");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Display the window.
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
 
    //The standard main method.
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }

}