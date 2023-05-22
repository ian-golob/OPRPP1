package hr.fer.zemris.java.gui.calc.components;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class PowButton extends CalculatorButton implements ItemListener {
    private boolean isInverted = false;

    private static final String OP_NAME = "x^n";

    private static final String INV_OP_NAME = "nsqrt(x)";

    public PowButton() {
        super(OP_NAME);
    }

    public boolean isInverted() {
        return isInverted;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            isInverted = true;
            setText(INV_OP_NAME);
        } else if (e.getStateChange() == ItemEvent.DESELECTED){
            isInverted = false;
            setText(OP_NAME);
        }
    }
}
