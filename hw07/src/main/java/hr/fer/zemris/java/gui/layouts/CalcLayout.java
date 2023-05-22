package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.function.Function;

/**
 * @author Ian Golob
 */
public class CalcLayout implements LayoutManager2 {

    public final static int CALC_ROWS = 5;
    public final static int CALC_COLUMNS = 7;
    public final static int FIRST_FIELD_COLUMN_SIZE = 5;

    private final Component[][] components = new Component[CALC_ROWS][CALC_COLUMNS];

    private final int rowGap;

    private final int columnGap;

    public CalcLayout(){
        this(0);
    }

    public CalcLayout(int gap) {
        this.rowGap = gap;
        this.columnGap = gap;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if(comp == null || constraints == null){
            throw new NullPointerException();
        }

        RCPosition position;
        if(constraints instanceof RCPosition){

            position = (RCPosition) constraints;

        } else if (constraints instanceof String){

            position = RCPosition.parse((String) constraints);

        } else {
            throw new IllegalArgumentException("The constraint is not of type RCPosition or String.");
        }

        int r  = position.getRow();
        int s = position.getColumn();
        if (r < 1 || r > CALC_ROWS ||
                s < 1 || s > CALC_COLUMNS ||
                (r == 1 && (s > 1 && s < 1 + FIRST_FIELD_COLUMN_SIZE)) ||
                components[r-1][s-1] != null) {

            throw new CalcLayoutException();
        }

        components[r-1][s-1] = comp;
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return calculateWeightedDimension(target, true, Component::getMaximumSize);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    /**
     * Unsupported in the CalcLayout
     * @throws UnsupportedOperationException when called
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        for(int i = 0; i < CALC_ROWS; i++){
            for(int j = 0; j < CALC_COLUMNS; j++){
                if(components[i][j] == comp){
                    components[i][j] = null;
                }
            }
        }
    }

    private Dimension calculateWeightedDimension(Container parent, boolean min, Function<Component, Dimension> dimFunction){

        int cellWidth;
        int cellHeight;

        if(min){
            cellWidth = Integer.MAX_VALUE;
            cellHeight = Integer.MAX_VALUE;
        } else {
            cellWidth = 0;
            cellHeight = 0;
        }

        if(components[0][0] != null){
            Dimension firstFieldDim = dimFunction.apply(components[0][0]);

            if(firstFieldDim != null){
                cellWidth = (int) Math.ceil((1.0 * firstFieldDim.width - (FIRST_FIELD_COLUMN_SIZE-1) * columnGap) / FIRST_FIELD_COLUMN_SIZE);
                cellHeight = firstFieldDim.height;
            }
        }

        for(int i = 0; i < CALC_ROWS; i++){
            for(int j = 0; j < CALC_COLUMNS; j++){

                if(i == 0 && j < FIRST_FIELD_COLUMN_SIZE){
                    continue;
                }

                Component component = components[i][j];

                if(component == null){
                    continue;
                }

                Dimension compDimension = dimFunction.apply(component);

                if(compDimension == null){
                    continue;
                }

                if(min){
                    cellWidth = Math.min(cellWidth, compDimension.width);
                    cellHeight = Math.min(cellHeight, compDimension.height);
                } else {
                    cellWidth = Math.max(cellWidth, compDimension.width);
                    cellHeight = Math.max(cellHeight, compDimension.height);
                }
            }
        }

        int totalHeight = cellHeight * CALC_ROWS + (CALC_ROWS - 1) * rowGap;
        int totalWidth = cellWidth * CALC_COLUMNS + (CALC_COLUMNS - 1 ) * columnGap;

        return new Dimension(
                totalWidth + parent.getInsets().left + parent.getInsets().right,
                totalHeight + parent.getInsets().top + parent.getInsets().bottom
        );
    }


    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calculateWeightedDimension(parent, false, Component::getPreferredSize);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calculateWeightedDimension(parent, false, Component::getMinimumSize);
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();

        int totalCellWidth = (parent.getWidth() - insets.left - insets.right - (CALC_COLUMNS - 1) * columnGap);
        int totalCellHeight = (parent.getHeight() - insets.top - insets.bottom - (CALC_ROWS - 1 ) * rowGap);

        Integer[] cellWidths = calculateUniformDivision(totalCellWidth, CALC_COLUMNS);
        Integer[] cellHeights = calculateUniformDivision(totalCellHeight, CALC_ROWS);

        if(components[0][0] != null){

            int width = (FIRST_FIELD_COLUMN_SIZE - 1) * columnGap;
            for(int i = 0; i < FIRST_FIELD_COLUMN_SIZE; i++){
                width += cellWidths[i];
            }

            int height = cellHeights[0];

            components[0][0].setBounds(
                    insets.left,
                    insets.top,
                    width,
                    height);
        }

        int y = insets.top;
        for(int i = 0; i < CALC_ROWS; i++){
            int x = insets.left;
            int height = cellHeights[i];

            for(int j = 0; j < CALC_COLUMNS; j++){
                int width = cellWidths[j];

                if((i != 0 || j >= FIRST_FIELD_COLUMN_SIZE ) &&
                        components[i][j] != null){

                    components[i][j].setBounds(
                            x,
                            y,
                            width,
                            height);
                }

                x += width + columnGap;
            }
            y += height + rowGap;
        }
    }



    /*
    // USING DOUBLE WIDTHS AND HEIGHTS

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();

        int totalCellWidth = (parent.getWidth() - insets.left - insets.right - (CALC_COLUMNS - 1) * columnGap);
        int totalCellHeight = (parent.getHeight() - insets.top - insets.bottom - (CALC_ROWS - 1 ) * rowGap);

        double cellWidth = 1.0 * totalCellWidth / CALC_COLUMNS;
        double cellHeight = 1.0 * totalCellHeight / CALC_ROWS;

        if(components[0][0] != null){

            double width = cellWidth * FIRST_FIELD_COLUMN_SIZE + (FIRST_FIELD_COLUMN_SIZE - 1) * columnGap;

            components[0][0].setBounds(
                    insets.left,
                    insets.top,
                    (int) Math.round(width),
                    (int) Math.round(cellHeight));
        }

        int y = insets.top;
        for(int i = 0; i < CALC_ROWS; i++){
            int x = insets.left;
            for(int j = 0; j < CALC_COLUMNS; j++){

                if((i != 0 || j >= FIRST_FIELD_COLUMN_SIZE ) &&
                        components[i][j] != null){

                    components[i][j].setBounds(
                            x,
                            y,
                            (int) Math.round(cellWidth),
                            (int) Math.round(cellHeight));
                }

                x += (int) Math.round(cellWidth) + columnGap;
            }
            y += (int) Math.round(cellHeight) + rowGap;
        }
    }

     */

    /**
     * Uniformly divides a number total into a list of n integer baskets.
     * @param total The number to divide into n integers.
     * @param n The number of baskets to divide the number into.
     * @return The uniformly divided number total.
     */
    private static Integer[] calculateUniformDivision(int total, int n) {
        Integer[] list = new Integer[n];

        for(int i = 0; i < n; i++){
            list[i] = 0;
        }

        int remainder = total % n;

        if(remainder != 0){
            if(n == 5){

                switch (remainder) {
                    case 1 -> list = new Integer[]{0, 0, 1, 0, 0};
                    case 2 -> list = new Integer[]{0, 1, 0, 1, 0};
                    case 3 -> list = new Integer[]{1, 0, 1, 0, 1};
                    case 4 -> list = new Integer[]{1, 1, 0, 1, 1};
                }

            } else if (n == 7){
                switch (remainder) {
                    case 1 -> list = new Integer[]{0, 0, 0, 1, 0, 0, 0};
                    case 2 -> list = new Integer[]{0, 1, 0, 0, 0, 1, 0};
                    case 3 -> list = new Integer[]{0, 1, 0, 1, 0, 1, 0};
                    case 4 -> list = new Integer[]{1, 0, 1, 0, 1, 0, 1};
                    case 5 -> list = new Integer[]{1, 1, 0, 1, 0, 1, 1};
                    case 6 -> list = new Integer[]{1, 1, 1, 0, 1, 1, 1};
                }

            } else {

                for(int i = 0; i < remainder; i++){

                    int pos = (int) Math.floor(Math.random() * (n - i));

                    for(int j = 0; j < list.length; j++){
                        if(pos == 0 && list[j] == 0){
                            list[j] = 1;
                            break;
                        } else {
                            if(list[j] == 0){
                                pos--;
                            }
                        }
                    }

                }
            }
        }

        int divisionResult = total / n;
        for(int i = 0; i < n; i++){
            list[i] += divisionResult;
        }

        return list;
    }
}
