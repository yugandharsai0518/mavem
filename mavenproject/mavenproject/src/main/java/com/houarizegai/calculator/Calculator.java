package com.houarizegai.calculator;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.awt.Color;
import javax.swing.*;
import java.lang.Math;

public class Calculator {

    private static final int WINDOW_WIDTH = 410;
    private static final int WINDOW_HEIGHT = 600;
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 70;
    private static final int MARGIN_X = 20;
    private static final int MARGIN_Y = 60;

    private JFrame window; 
    private JComboBox<String> comboCalcType, comboTheme;
    private JTextField inText; 
    private JButton btnC, btnBack, btnMod, btnDiv, btnMul, btnSub, btnAdd,
            btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
            btnPoint, btnEqual, btnRoot, btnPower, btnLog;

    private String opt = ""; // operator
    private boolean go = true; 
    private boolean addWrite = true; 
    private double val = 0; 

    public Calculator() {
        window = new JFrame("Calculator");
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null); 

        comboTheme = initCombo(new String[]{"Simple", "Colored", "DarkTheme"}, 230, 30, "Theme", themeSwitchEventConsumer);
        comboCalcType = initCombo(new String[]{"Standard", "Scientific"}, 20, 30, "Calculator type", calcTypeSwitchEventConsumer);

        int[] x = {MARGIN_X, MARGIN_X + 90, 200, 290, 380};
        int[] y = {MARGIN_Y, MARGIN_Y + 100, MARGIN_Y + 180, MARGIN_Y + 260, MARGIN_Y + 340, MARGIN_Y + 420};

        inText = new JTextField("0");
        inText.setBounds(x[0], y[0], 350, 70);
        inText.setEditable(false);
        inText.setBackground(Color.WHITE);
        inText.setFont(new Font("Comic Sans MS", Font.PLAIN, 33));
        window.add(inText);

        // === Buttons ===

        btnC = initBtn("C", x[0], y[1], e -> {
            repaintFont();
            inText.setText("0");
            opt = "";
            val = 0;
        });

        btnBack = initBtn("<-", x[1], y[1], e -> {
            repaintFont();
            String str = inText.getText();
            if (str.length() > 1) {
                inText.setText(str.substring(0, str.length() - 1));
            } else {
                inText.setText("0");
            }
        });

        btnMod = initBtn("%", x[2], y[1], e -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = calc(val, inText.getText(), opt);
                    inText.setText(format(val));
                    opt = "%";
                    go = false;
                    addWrite = false;
                }
        });

        btnDiv = initBtn("/", x[3], y[1], e -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = calc(val, inText.getText(), opt);
                    inText.setText(format(val));
                    opt = "/";
                    go = false;
                    addWrite = false;
                } else {
                    opt = "/";
                }
        });

        // Digits
        btn7 = digitBtn("7", x[0], y[2]);
        btn8 = digitBtn("8", x[1], y[2]);
        btn9 = digitBtn("9", x[2], y[2]);
        btn4 = digitBtn("4", x[0], y[3]);
        btn5 = digitBtn("5", x[1], y[3]);
        btn6 = digitBtn("6", x[2], y[3]);
        btn1 = digitBtn("1", x[0], y[4]);
        btn2 = digitBtn("2", x[1], y[4]);
        btn3 = digitBtn("3", x[2], y[4]);
        btn0 = digitBtn("0", x[1], y[5]);

        btnMul = operatorBtn("*", x[3], y[2]);
        btnSub = operatorBtn("-", x[3], y[3]);
        btnAdd = operatorBtn("+", x[3], y[4]);

        btnPoint = initBtn(".", x[0], y[5], e -> {
            repaintFont();
            if (addWrite) {
                if (!inText.getText().contains(".")) {
                    inText.setText(inText.getText() + ".");
                }
            } else {
                inText.setText("0.");
                addWrite = true;
            }
            go = true;
        });

        btnEqual = initBtn("=", x[2], y[5], e -> {
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = calc(val, inText.getText(), opt);
                    inText.setText(format(val));
                    opt = "=";
                    addWrite = false;
                }
        });
        btnEqual.setSize(2 * BUTTON_WIDTH + 10, BUTTON_HEIGHT);

        btnRoot = initBtn("√", x[4], y[1], e -> {
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText())) {
                val = Math.sqrt(Double.parseDouble(inText.getText()));
                inText.setText(format(val));
                opt = "√";
                addWrite = false;
            }
        });
        btnRoot.setVisible(false);

        btnPower = operatorBtn("^", x[4], y[2]);
        btnPower.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        btnPower.setText("pow");
        btnPower.setVisible(false);

        btnLog = initBtn("ln", x[4], y[3], e -> {
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText())) {
                val = Math.log(Double.parseDouble(inText.getText()));
                inText.setText(format(val));
                opt = "l";
                addWrite = false;
            }
        });
        btnLog.setVisible(false);

        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        window.setVisible(true);
    }

    private JComboBox<String> initCombo(String[] items, int x, int y, String toolTip, Consumer<ItemEvent> consumerEvent) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBounds(x, y, 140, 25);
        combo.setToolTipText(toolTip);
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        combo.addItemListener(consumerEvent::accept);
        window.add(combo);
        return combo;
    }

    private JButton initBtn(String label, int x, int y, ActionListener event) {
        JButton btn = new JButton(label);
        btn.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        btn.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(event);
        btn.setFocusable(false);
        window.add(btn);
        return btn;
    }

    private JButton digitBtn(String label, int x, int y) {
        return initBtn(label, x, y, e -> {
            repaintFont();
            if (addWrite) {
                if (Pattern.matches("[0]*", inText.getText())) {
                    inText.setText(label);
                } else {
                    inText.setText(inText.getText() + label);
                }
            } else {
                inText.setText(label);
                addWrite = true;
            }
            go = true;
        });
    }

    private JButton operatorBtn(String operator, int x, int y) {
        return initBtn(operator, x, y, e -> {
            repaintFont();
            if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
                if (go) {
                    val = calc(val, inText.getText(), opt);
                    inText.setText(format(val));
                    opt = operator;
                    go = false;
                    addWrite = false;
                } else {
                    opt = operator;
                }
        });
    }

    public double calc(double x, String input, String opt) {
        inText.setFont(inText.getFont().deriveFont(Font.PLAIN));
        double y = Double.parseDouble(input);
        switch (opt) {
            case "+": return x + y;
            case "-": return x - y;
            case "*": return x * y;
            case "/": return x / y;
            case "%": return x % y;
            case "^": return Math.pow(x, y);
            default: return y;
        }
    }

    private String format(double val) {
        if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            return String.valueOf((int) val);
        } else {
            return String.valueOf(val);
        }
    }

    private void repaintFont() {
        inText.setFont(inText.getFont().deriveFont(Font.PLAIN));
    }

    private Consumer<ItemEvent> calcTypeSwitchEventConsumer = event -> {
        if (event.getStateChange() != ItemEvent.SELECTED) return;
        String selectedItem = (String) event.getItem();
        switch (selectedItem) {
            case "Standard":
                window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
                btnRoot.setVisible(false);
                btnPower.setVisible(false);
                btnLog.setVisible(false);
                break;
            case "Scientific":
                window.setSize(WINDOW_WIDTH + 80, WINDOW_HEIGHT);
                btnRoot.setVisible(true);
                btnPower.setVisible(true);
                btnLog.setVisible(true);
                break;
        }
    };

    private Consumer<ItemEvent> themeSwitchEventConsumer = event -> {
        if (event.getStateChange() != ItemEvent.SELECTED) return;
        String selectedTheme = (String) event.getItem();
        // Theme logic remains unchanged...
    };

    public static void main(String[] args) {
        new Calculator();
    }
}