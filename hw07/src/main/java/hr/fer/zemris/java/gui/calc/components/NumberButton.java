package hr.fer.zemris.java.gui.calc.components;

public class NumberButton extends CalculatorButton{

    private final int number;

    public NumberButton(int number) {
        super(String.valueOf(number));
        this.number = number;
        setFont(getFont().deriveFont(30f));
    }

    public int getNumber() {
        return number;
    }
}
