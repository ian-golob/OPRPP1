package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.components.*;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.function.DoubleBinaryOperator;


public class Calculator extends JFrame {
    public Calculator() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initGUI();
        pack();
    }
    private void initGUI()  {
        CalcModelImpl model = new CalcModelImpl();

        Container cp = getContentPane();
        cp.setLayout(new CalcLayout(3));

        ScreenLabel screen = new ScreenLabel();
        screen.setPreferredSize(new Dimension(500, 100));
        cp.add(screen,"1, 1");
        model.addCalcValueListener(screen);

        JCheckBox invCheckBox = new JCheckBox("Inv", false);
        cp.add(invCheckBox, "5, 7");

        List<NumberButton> numberButtons = new ArrayList<>(9);
        ActionListener numberActionListener = e -> {
            NumberButton nb = (NumberButton) e.getSource();

            if(model.hasFrozenValue()){
                model.freezeValue(null);
            }

            try{
                model.insertDigit(nb.getNumber());
            } catch (CalculatorInputException ex){
                JOptionPane.showMessageDialog(
                        this,
                        "The number is not editable.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        };
        for(int i = 0; i < 10; i++){
            NumberButton nb = new NumberButton(i);
            numberButtons.add(nb);
            nb.addActionListener(numberActionListener);
        }

        Iterator<NumberButton> numberButtonIterator = numberButtons.iterator();
        for(int i = 5; i >= 2; i--){
            for(int j = 3; j <= 5; j++){
                if(i == 5 && j > 3){
                    continue;
                }

                cp.add(numberButtonIterator.next(), new RCPosition(i, j));
            }
        }


        CalculatorButton equalsButton = new CalculatorButton("=");
        equalsButton.addActionListener(e -> {
            if(model.hasFrozenValue()){
                JOptionPane.showMessageDialog(
                        this,
                        "You must specify a number first.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }else {
                DoubleBinaryOperator pendingOperator = model.getPendingBinaryOperation();
                if(pendingOperator != null){

                    model.setValue(pendingOperator.applyAsDouble(model.getActiveOperand(), model.getValue()));
                    model.clearActiveOperand();
                    model.setPendingBinaryOperation(null);

                }
            }
        });
        cp.add(equalsButton, "1,6");


        CalculatorButton clrButton = new CalculatorButton("clr");
        clrButton.addActionListener(e -> model.clear());
        cp.add(clrButton, "1,7");


        List<InvertibleUnaryOperationButton> invertibleUnaryOperationButtons = List.of( new InvertibleUnaryOperationButton(
                        "1/x",
                        x -> 1 / x,
                        "1/x",
                        x -> 1 / x),

                new InvertibleUnaryOperationButton(
                        "sin",
                        Math::sin,
                        "arcsin",
                        Math::asin),
                new InvertibleUnaryOperationButton(
                        "log",
                        Math::log10,
                        "10^x",
                        x -> Math.pow(10, x)),

                new InvertibleUnaryOperationButton(
                        "cos",
                        Math::cos,
                        "arccos",
                        Math::acos),

                new InvertibleUnaryOperationButton(
                        "ln",
                        Math::log,
                        "e^x",
                        Math::exp),

                new InvertibleUnaryOperationButton(
                        "tan",
                        Math::tan,
                        "arctan",
                        Math::atan),
                new InvertibleUnaryOperationButton(
                        "ctg",
                        x -> 1.0 / Math.tan(x),
                        "arcctg",
                        x -> Math.PI / 2 - Math.atan(x))
                );
        ActionListener invertableUnaryOperationActionListener = e -> {
            InvertibleUnaryOperationButton iuob = (InvertibleUnaryOperationButton) e.getSource();

            DoubleBinaryOperator pendingOperator = model.getPendingBinaryOperation();
            if(pendingOperator != null){

                model.setValue(pendingOperator.applyAsDouble(model.getActiveOperand(), model.getValue()));
                model.clearActiveOperand();
                model.setPendingBinaryOperation(null);

            }

            if(!iuob.isInverted()){
                model.setValue(iuob.getOperation().apply(model.getValue()));
            } else {
                model.setValue(iuob.getInvOperation().apply(model.getValue()));
            }
        };
        invertibleUnaryOperationButtons.forEach(b ->{
                    b.addActionListener(invertableUnaryOperationActionListener);
                    invCheckBox.addItemListener(b);
                });
        Iterator<InvertibleUnaryOperationButton> invertibleUnaryOperationButtonIterator
                = invertibleUnaryOperationButtons.iterator();
        for(int i = 2; i <= 5; i++){
            for(int j = 1; j <= 2; j++){
                if(i == 5 && j == 1){
                    continue;
                }

                cp.add(invertibleUnaryOperationButtonIterator.next(), new RCPosition(i, j));
            }
        }


        List<BinaryOperationButton> binaryOperationButtons = List.of(
                new BinaryOperationButton("/", (x, y) -> x / y),
                new BinaryOperationButton("*", (x, y) -> x * y),
                new BinaryOperationButton( "-", (x, y) -> x - y),
                new BinaryOperationButton( "+", Double::sum)
        );
        ActionListener binaryOperationActionListener = e -> {
            if(model.hasFrozenValue()){
                JOptionPane.showMessageDialog(
                        this,
                        "You must specify a number first.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                BinaryOperationButton bob = (BinaryOperationButton) e.getSource();

                DoubleBinaryOperator pendingOperator = model.getPendingBinaryOperation();
                if(pendingOperator != null){

                    model.setValue(pendingOperator.applyAsDouble(model.getActiveOperand(), model.getValue()));
                    model.clearActiveOperand();
                    model.setPendingBinaryOperation(null);

                }

                String frozenValue = model.toString();
                model.setActiveOperand(model.getValue());
                model.setPendingBinaryOperation(bob.getOperation());
                model.clear();
                model.freezeValue(frozenValue);
            }

        };
        binaryOperationButtons.forEach(b -> b.addActionListener(binaryOperationActionListener));
        Iterator<BinaryOperationButton> binaryOperationButtonIterator = binaryOperationButtons.iterator();
        for(int i = 2; i <= 5; i++){
            cp.add(binaryOperationButtonIterator.next(), new RCPosition(i, 6));
        }


        PowButton powButton = new PowButton();
        invCheckBox.addItemListener(powButton);
        powButton.addActionListener(e -> {
            if(model.hasFrozenValue()){
                JOptionPane.showMessageDialog(
                        this,
                        "You must specify a number first.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }else {
                PowButton pb = (PowButton) e.getSource();

                DoubleBinaryOperator pendingOperator = model.getPendingBinaryOperation();
                if(pendingOperator != null){

                    model.setValue(pendingOperator.applyAsDouble(model.getActiveOperand(), model.getValue()));
                    model.clearActiveOperand();
                    model.setPendingBinaryOperation(null);

                }

                model.setActiveOperand(model.getValue());
                if(pb.isInverted()){
                    model.setPendingBinaryOperation((left, right) -> Math.pow(left, 1/right));
                } else {
                    model.setPendingBinaryOperation(Math::pow);
                }
                model.clear();
            }


        });
        cp.add(powButton, "5,1");


        CalculatorButton swapSignButton = new CalculatorButton("+/-");
        swapSignButton.addActionListener(e -> model.swapSign());
        cp.add(swapSignButton, "5, 4");

        CalculatorButton insertDecimalPointButton = new CalculatorButton(".");
        insertDecimalPointButton.addActionListener(e -> model.insertDecimalPoint());
        cp.add(insertDecimalPointButton, "5, 5");

        Stack<Double> numberStack = new Stack<>();
        CalculatorButton pushButton = new CalculatorButton("push");
        pushButton.addActionListener(e -> {
            if(model.hasFrozenValue()){
                JOptionPane.showMessageDialog(
                        this,
                        "You must specify a number first.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }else {
                numberStack.add(model.getValue());
            }
        });
        cp.add(pushButton, "3, 7");

        CalculatorButton popButton = new CalculatorButton("pop");
        popButton.addActionListener(e -> {
            if(!numberStack.empty()){
                model.setValue(numberStack.pop());
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "The stack is empty.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        cp.add(popButton, "4, 7");


        CalculatorButton resButton = new CalculatorButton("res");
        resButton.addActionListener(e -> {
            model.clearAll();
            numberStack.clear();
        });
        cp.add(resButton, "2, 7");


    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new Calculator().setVisible(true);
        });
    }
}
