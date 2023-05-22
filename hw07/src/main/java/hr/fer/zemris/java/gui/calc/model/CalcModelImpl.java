package hr.fer.zemris.java.gui.calc.model;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

public class CalcModelImpl implements CalcModel {

    private final List<CalcValueListener> calcValueListeners = new ArrayList<>();

    private boolean isEditable = true;
    private boolean isPositive = true;
    private String digits = "";
    private double value = 0.0;
    private String label = null;
    private String frozenLabel = null;

    private Double activeOperand;
    private DoubleBinaryOperator pendingOperation;

    private void notifyValueListeners() {
        calcValueListeners.forEach(calcValueListener -> calcValueListener.valueChanged(this));
    }

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        calcValueListeners.add(l);
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        calcValueListeners.remove(l);
    }

    @Override
    public double getValue() {
        if(!isPositive){
            return -value;
        } else {
            return value;
        }
    }

    @Override
    public void setValue(double value) {
        if(value < 0.0){
            isPositive = false;
            value = -value;
        } else {
            isPositive = true;
        }

        if(hasFrozenValue()){
            freezeValue(null);
        }

        isEditable = false;
        this.value = value;
        this.digits = Double.toString(value);

        notifyValueListeners();
    }


    @Override
    public boolean isEditable() {
        return isEditable;
    }

    @Override
    public void clear() {
        isEditable = true;
        isPositive = true;
        digits = "";
        value = 0.0;
        label = null;
        frozenLabel = null;

        notifyValueListeners();
    }

    @Override
    public void clearAll() {
        activeOperand = null;
        pendingOperation = null;

        clear();
    }

    @Override
    public void swapSign() throws CalculatorInputException {
        if(!isEditable){
            throw new CalculatorInputException();
        }

        isPositive = !isPositive;

        notifyValueListeners();
    }

    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if(!isEditable ||
                digits.contains(".") ||
                digits.length() == 0){
            throw new CalculatorInputException();
        }

        digits += ".";

        notifyValueListeners();
    }

    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if(!isEditable){
            throw new CalculatorInputException();
        }

        if(hasFrozenValue()){
            freezeValue(null);
        }

        if(digits.equals("0") ){
            if(digit == 0){
                return;
            } else {
                digits = "";
            }
        }

        String tmpDigits = digits + digit;

        double parsedDigits = Double.parseDouble(tmpDigits);

        if(Double.isFinite(parsedDigits)){

            digits = tmpDigits;

            value = parsedDigits;

            notifyValueListeners();

        } else {
            throw new CalculatorInputException();
        }
    }

    @Override
    public boolean isActiveOperandSet() {
        return activeOperand != null;
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if(activeOperand == null){
            throw new IllegalStateException();
        }

        return activeOperand;
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
    }

    @Override
    public void clearActiveOperand() {
        activeOperand = null;
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return pendingOperation;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        pendingOperation = op;
    }

    @Override
    public void freezeValue(String value) {
        frozenLabel = value;
        notifyValueListeners();
    }

    @Override
    public boolean hasFrozenValue() {
        return frozenLabel != null;
    }

    @Override
    public String toString(){
        if(hasFrozenValue()){
            return frozenLabel;
        }

        if(Double.isNaN(value)){
            return "NaN";
        }

        if(Double.isInfinite(value)){
            if(isPositive){
                return "Infinity";
            } else {
                return "-Infinity";
            }
        }

        String returnValue;
        if(digits.length() > 0){
            returnValue = digits;
        } else {
            returnValue = "0";
        }

        if(!isPositive){
            returnValue = "-" + returnValue;
        }

        return returnValue;
    }
}
