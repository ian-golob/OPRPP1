package hr.fer.zemris.java.gui.calc.components;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;

import javax.swing.*;
import java.awt.*;

public class ScreenLabel extends JLabel implements CalcValueListener {

    public ScreenLabel(){
        setText("0");
        setFont(getFont().deriveFont(30f));
        setHorizontalAlignment(SwingConstants.RIGHT);
        setBorder(BorderFactory.createEmptyBorder(0,0, 0, 10));
        setBackground(Color.YELLOW);
        setOpaque(true);
    }

    @Override
    public void valueChanged(CalcModel model) {
        setText(model.toString());
    }
}
