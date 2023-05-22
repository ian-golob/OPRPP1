package hr.fer.zemris.java.gui.calc.components;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.function.Function;

public class InvertibleUnaryOperationButton extends CalculatorButton implements ItemListener {

    private final String opName;
    private final Function<Double, Double> operation;

    private final String invOperationName;
    private final Function<Double, Double> invOperation;

    private boolean isInverted = false;

    public InvertibleUnaryOperationButton(String opName, Function<Double, Double> operation, String invOperationName, Function<Double, Double> invOp) {
        super(opName);
        this.opName = opName;
        this.operation = operation;
        this.invOperationName = invOperationName;
        this.invOperation = invOp;
    }

    public String getOpName() {
        return opName;
    }

    public Function<Double, Double> getOperation() {
        return operation;
    }

    public String getInvOperationName() {
        return invOperationName;
    }

    public Function<Double, Double> getInvOperation() {
        return invOperation;
    }

    public boolean isInverted() {
        return isInverted;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            isInverted = true;
            setText(invOperationName);
        } else if (e.getStateChange() == ItemEvent.DESELECTED){
            isInverted = false;
            setText(opName);
        }
    }
}
