package com.dabek.jakub.ezgui;

import java.awt.Font;
import java.awt.Menu;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Label;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.MenuBar;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.MenuItem;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dabek.jakub.primenumbers.PrimeNumbers;

public class PrimeNumbersGUI extends JFrame {

    JTextField rangeTextField;
    JTextField argumentCountTextField;
    JButton button;

    PrimeNumbersGUI(String title, Font font) {
        super(title);
        setFont(font);
        add(new Label("Enter range and number of test cases"), BorderLayout.NORTH);
        JPanel panel = new JPanel();
        //panel.setPreferredSize(new Dimension(350, 100));

        rangeTextField = new JTextField("range", 15);
        argumentCountTextField = new JTextField("argument count", 15);
        rangeTextField.setInputVerifier(new Moj2InputVerifier());
        argumentCountTextField.setInputVerifier(new Moj2InputVerifier());

        FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Object source = e.getSource();
                if (source instanceof JTextField) {
                    ((JTextField) source).selectAll();
                }
            }
        };
        rangeTextField.addFocusListener(focusListener);
        argumentCountTextField.addFocusListener(focusListener);
        panel.add(rangeTextField);
        panel.add(argumentCountTextField);
        button = new JButton("Accept");
        button.setFocusable(false);
        button.addActionListener(new MojActionListener());
        panel.add(button);
        add(panel, BorderLayout.CENTER);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setPreferredSize(new Dimension(640, 480));
        App.changeFont(this, font);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    class MojActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int range = Integer.parseInt(rangeTextField.getText());
                int argc = Integer.parseInt(argumentCountTextField.getText());
                Moj2Dialog1 dialog = new Moj2Dialog1(PrimeNumbersGUI.this, argc);
                dialog.setVisible(true);
                List<Integer> arguments = dialog.getResult();
                if (arguments != null) {
                    List<Integer> resultPrimes = new ArrayList<>();
                    try {
                        PrimeNumbers numbers = new PrimeNumbers(range);
                        for (int n : arguments) {
                            try {
                                resultPrimes.add(numbers.number(n - 1));
                            } catch (IndexOutOfBoundsException exception) {
                                resultPrimes.add(null);
                                JOptionPane.showMessageDialog(PrimeNumbersGUI.this, exception.getMessage());
                            }
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < arguments.size(); i++) {
                            Integer prime = resultPrimes.get(i);
                            sb.append(arguments.get(i));
                            if (prime != null) {
                                int index = arguments.get(i);
                                switch (index % 10) {
                                case 1:
                                    if (index % 100 / 10 != 1) {
                                        sb.append("st");
                                        break;
                                    }
                                case 2:
                                    if (index % 100 / 10 != 1) {
                                        sb.append("nd");
                                        break;
                                    }
                                case 3:
                                    if (index % 100 / 10 != 1) {
                                        sb.append("rd");
                                        break;
                                    }
                                default:
                                    sb.append("th");
                                    break;
                                }
                                sb.append(" prime is equal to ");
                                sb.append(prime);
                                sb.append("\n");
                            }
                        }
                        JOptionPane.showMessageDialog(PrimeNumbersGUI.this, sb.toString());
                    } catch (IndexOutOfBoundsException exception) {
                        JOptionPane.showMessageDialog(PrimeNumbersGUI.this, exception.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(PrimeNumbersGUI.this, "You didn't input the right arguments");
                }

            } catch (NumberFormatException exception) {
                System.err.println("xd");
            }
        }

    }

    static class Moj2InputVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            JTextField textField = (JTextField) input;
            try {
                Integer.parseInt(textField.getText());
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
    }

    class Moj2Dialog1 extends JDialog {

        JTextField argumentTextField;
        int argumentCount;

        Moj2Dialog1(JFrame window, int a) {
            super(window, "Enter " + a + " arguments", true);
            argumentCount = a;
            argumentTextField = new JTextField(15 + argumentCount * 4);
            Moj2SrodekInputVerifier verifier = new Moj2SrodekInputVerifier();
            argumentTextField.setInputVerifier(verifier);
            argumentTextField.addActionListener(verifier);
            add(argumentTextField);
            App.changeFont(this, window.getFont());
            pack();
            setLocationRelativeTo(null);
        }

        List<Integer> result;

        List<Integer> getResult() {
            return result;
        }

        class Moj2SrodekInputVerifier extends InputVerifier implements ActionListener {
            @Override
            public boolean verify(JComponent input) {
                JTextField textField = (JTextField) input;
                try {
                    result = new ArrayList<>();
                    String[] arguments = textField.getText().split("\\s");
                    System.err.println(Arrays.toString(arguments));
                    if (arguments.length < argumentCount)
                        return false;
                    for (String argument : arguments) {
                        result.add(Integer.parseInt(argument));
                    }
                } catch (NumberFormatException e) {
                    result = null;
                    return false;
                }
                return true;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (verify((JComponent) e.getSource()))
                    Moj2Dialog1.this.setVisible(false);
            }
        }

    }

}

/* 

 /$$$$$$$  /$$           /$$                     /$$$$$$$                                   
| $$__  $$|__/          | $$                    | $$__  $$                                  
| $$  \ $$ /$$  /$$$$$$ | $$  /$$$$$$   /$$$$$$ | $$  \ $$  /$$$$$$  /$$$$$$/$$$$   /$$$$$$ 
| $$  | $$| $$ |____  $$| $$ /$$__  $$ /$$__  $$| $$  | $$ /$$__  $$| $$_  $$_  $$ /$$__  $$
| $$  | $$| $$  /$$$$$$$| $$| $$  \ $$| $$  \ $$| $$  | $$| $$$$$$$$| $$ \ $$ \ $$| $$  \ $$
| $$  | $$| $$ /$$__  $$| $$| $$  | $$| $$  | $$| $$  | $$| $$_____/| $$ | $$ | $$| $$  | $$
| $$$$$$$/| $$|  $$$$$$$| $$|  $$$$$$/|  $$$$$$$| $$$$$$$/|  $$$$$$$| $$ | $$ | $$|  $$$$$$/
|_______/ |__/ \_______/|__/ \______/  \____  $$|_______/  \_______/|__/ |__/ |__/ \______/ 
                                       /$$  \ $$                                            
                                      |  $$$$$$/                                            
                                       \______/                                             
*/

class DialogDemo extends Frame implements ActionListener {
    private Dialog myDialog;
    private Button myFrameButton, myDialogButton;

    public DialogDemo() {
        myFrameButton = new Button(" Dialog ");
        myFrameButton.addActionListener(this);
        myDialogButton = new Button("OK");
        myDialogButton.addActionListener(this);
        myDialog = new Dialog(this, " Dialog Window ", true);
        myDialog.setSize(320, 240);
        myDialog.add(myDialogButton);
        add(myFrameButton);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK"))
            myDialog.setVisible(false);
        else
            myDialog.setVisible(true);
    }

    public static void main(String[] args) {
        Frame f = new DialogDemo();
        f.setBounds(100, 100, 640, 480);
        f.setVisible(true);
    }
}

/*

 /$$      /$$                               /$$$$$$$                                   
| $$$    /$$$                              | $$__  $$                                  
| $$$$  /$$$$  /$$$$$$  /$$$$$$$  /$$   /$$| $$  \ $$  /$$$$$$  /$$$$$$/$$$$   /$$$$$$ 
| $$ $$/$$ $$ /$$__  $$| $$__  $$| $$  | $$| $$  | $$ /$$__  $$| $$_  $$_  $$ /$$__  $$
| $$  $$$| $$| $$$$$$$$| $$  \ $$| $$  | $$| $$  | $$| $$$$$$$$| $$ \ $$ \ $$| $$  \ $$
| $$\  $ | $$| $$_____/| $$  | $$| $$  | $$| $$  | $$| $$_____/| $$ | $$ | $$| $$  | $$
| $$ \/  | $$|  $$$$$$$| $$  | $$|  $$$$$$/| $$$$$$$/|  $$$$$$$| $$ | $$ | $$|  $$$$$$/
|__/     |__/ \_______/|__/  |__/ \______/ |_______/  \_______/|__/ |__/ |__/ \______/ 
                                                                                       
*/

class MenuDemo extends Frame implements ActionListener {
    private Label myLabel;
    private MenuBar myMenu;
    private Menu menu1, menu2, submenu;

    private MenuItem i1, i2, i3, i4, i5, exit;

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals(" Exit "))
            System.exit(0);

        else
            myLabel.setText(" Wybrano " + e.getActionCommand());
    }

    public MenuDemo() {
        myLabel = new Label(" Start ", Label.CENTER);
        myMenu = new MenuBar();
        menu1 = new Menu(" Menu 1");
        menu2 = new Menu(" Menu 2");
        myMenu.add(menu1);
        myMenu.add(menu2);
        submenu = new Menu(" Podmenu ");
        menu1.add(submenu);
        menu1.addSeparator();
        i1 = new MenuItem(" Akcja 1");
        i1.addActionListener(this);
        i2 = new MenuItem(" Akcja 2");
        i2.addActionListener(this);
        i3 = new MenuItem(" Akcja 3");
        i3.addActionListener(this);
        i4 = new MenuItem(" Akcja 4");
        i4.addActionListener(this);
        i5 = new MenuItem(" Akcja 5");
        i5.addActionListener(this);
        exit = new MenuItem(" Exit ");
        exit.addActionListener(this);
        submenu.add(i1);
        submenu.add(i2);
        submenu.add(i3);
        menu1.add(exit);
        menu2.add(i4);
        menu2.add(i5);
        setLayout(new GridLayout(1, 1));
        setMenuBar(myMenu);
        add(myLabel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        Frame okno = new MenuDemo();
        okno.setBounds(100, 100, 640, 480);
        okno.setVisible(true);
    }
}

/*

 /$$$$$$$                                                            /$$$$$$$                                   
| $$__  $$                                                          | $$__  $$                                  
| $$  \ $$ /$$$$$$   /$$$$$$   /$$$$$$$  /$$$$$$   /$$$$$$$ /$$$$$$$| $$  \ $$  /$$$$$$  /$$$$$$/$$$$   /$$$$$$ 
| $$$$$$$//$$__  $$ /$$__  $$ /$$_____/ /$$__  $$ /$$_____//$$_____/| $$  | $$ /$$__  $$| $$_  $$_  $$ /$$__  $$
| $$____/| $$  \__/| $$  \ $$| $$      | $$$$$$$$|  $$$$$$|  $$$$$$ | $$  | $$| $$$$$$$$| $$ \ $$ \ $$| $$  \ $$
| $$     | $$      | $$  | $$| $$      | $$_____/ \____  $$\____  $$| $$  | $$| $$_____/| $$ | $$ | $$| $$  | $$
| $$     | $$      |  $$$$$$/|  $$$$$$$|  $$$$$$$ /$$$$$$$//$$$$$$$/| $$$$$$$/|  $$$$$$$| $$ | $$ | $$|  $$$$$$/
|__/     |__/       \______/  \_______/ \_______/|_______/|_______/ |_______/  \_______/|__/ |__/ |__/ \______/ 

 */

class MyButtonAdapter implements ActionListener {
    ProcessDemo p;

    MyButtonAdapter(ProcessDemo p) {
        this.p = p;
    }

    public void actionPerformed(ActionEvent e) {
        p.action();
    }
}

class MyButton extends Button {
    MyButton(ProcessDemo p) {
        super(" Wykonaj ");
        addActionListener(new MyButtonAdapter(p));
    }
}

class MyFrame extends Frame {
    MyFrame(ProcessDemo p) {
        super(" Program ");
        setBounds(100, 100, 640, 480);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        setLayout(new BorderLayout());
        MyButton akcja = new MyButton(p);
        add(akcja, BorderLayout.SOUTH);
        p.dane = new TextField(80);
        p.dane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    for (ActionListener al : akcja.getActionListeners()) {
                        al.actionPerformed(new ActionEvent(akcja, 1, akcja.getActionCommand()));
                    }
                }
            }
        });
        add(p.dane, BorderLayout.NORTH);
        p.wynik = new TextArea(25, 80);
        add(p.wynik, BorderLayout.CENTER);
        setResizable(false);
    }
}

class ProcessDemo {
    MyFrame frame;
    TextField dane;
    TextArea wynik;

    void action() {
        try {
            Process child = Runtime.getRuntime().exec(dane.getText());
            System.err.println(child);
            BufferedReader in = new BufferedReader(new InputStreamReader(child.getInputStream()));
            String c;
            wynik.setText("");
            while ((c = in.readLine()) != null)
                wynik.append(c + "\n");
            in.close();
        } catch (IOException e) {
            wynik.setText(e.getMessage());
        } catch (IllegalArgumentException e) {
            wynik.setText(e.getMessage());
        }
        dane.setText("");
    }

    public static void main(String[] args) {
        ProcessDemo p = new ProcessDemo();
        p.frame = new MyFrame(p);
        p.frame.setVisible(true);
    }
}