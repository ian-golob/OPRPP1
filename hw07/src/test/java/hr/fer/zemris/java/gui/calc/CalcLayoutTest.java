package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.CalcLayoutException;
import hr.fer.zemris.java.gui.layouts.RCPosition;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class CalcLayoutTest {

    @Test
    public void addLayoutComponentOutOfBoundsTest(){
        CalcLayout calcLayout = new CalcLayout();

        Component component = new JLabel("comp1");

        assertThrows(CalcLayoutException.class,
                () -> calcLayout.addLayoutComponent(component, new RCPosition(0, 4)));
        assertThrows(CalcLayoutException.class,
                () -> calcLayout.addLayoutComponent(component, new RCPosition(CalcLayout.CALC_ROWS+1, 1)));
        assertThrows(CalcLayoutException.class,
                () -> calcLayout.addLayoutComponent(component, new RCPosition(1, 0)));
        assertThrows(CalcLayoutException.class,
                () -> calcLayout.addLayoutComponent(component, new RCPosition(1, CalcLayout.CALC_COLUMNS+1)));
    }

    @Test
    public void addLayoutComponentFirstFieldIllegalTest(){
        CalcLayout calcLayout = new CalcLayout();

        Component component = new JLabel("comp1");

        for(int i = 2; i <= CalcLayout.FIRST_FIELD_COLUMN_SIZE; i++){
            RCPosition position = new RCPosition(1, i);

            assertThrows(CalcLayoutException.class,
                    () -> calcLayout.addLayoutComponent(component, position));

        }
    }

    @Test
    public void testPreferredSize1(){
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel("");
        l1.setPreferredSize(new Dimension(10,30));
        JLabel l2 = new JLabel("");
        l2.setPreferredSize(new Dimension(20,15));
        p.add(l1, new RCPosition(2,2));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();

        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }

    @Test
    public void testPreferredSize2(){
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel("");
        l1.setPreferredSize(new Dimension(108,15));
        JLabel l2 = new JLabel("");
        l2.setPreferredSize(new Dimension(16,30));
        p.add(l1, new RCPosition(1,1));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();

        assertEquals(152, dim.width);
        assertEquals(158, dim.height);
    }

    @Test
    public void addLayoutComponentMultipleComponentsWithEqualConstraint(){
        CalcLayout calcLayout = new CalcLayout();

        Component component1 = new JLabel("comp1");
        Component component2 = new JLabel("comp2");
        RCPosition position = new RCPosition(1, 1);

        calcLayout.addLayoutComponent(component1, position);

        assertThrows(CalcLayoutException.class,
                () -> calcLayout.addLayoutComponent(component2, position));

    }

}
