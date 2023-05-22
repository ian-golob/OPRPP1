package hr.fer.zemris.java.gui.calc.components;

import java.util.function.DoubleBinaryOperator;

public class BinaryOperationButton extends CalculatorButton {
    private final DoubleBinaryOperator op;

    public BinaryOperationButton(String opName, DoubleBinaryOperator op) {
        super(opName);
        this.op = op;
    }


    public DoubleBinaryOperator getOperation() {
        return op;
    }
}
