package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

public class RCPosition {

    private final int row;

    private final int column;

    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Parses the given text into a RCPosition object, valid text is for example "1,7".
     * @param text The text to parse into a RCPosition object.
     * @return The parsed RCPosition object.
     * @throws IllegalArgumentException If the given text is invalid.
     */
    public static RCPosition parse(String text){
        try{

            String[] split = text.split(",");

            if(split.length != 2){
                throw new IllegalArgumentException();
            }

            int row = Integer.parseInt(split[0].trim());
            int column = Integer.parseInt(split[1].trim());

            return new RCPosition(row, column);

        } catch (Exception e){
            throw new IllegalArgumentException("Unable to parse given text to RCPosition.");
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RCPosition that = (RCPosition) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
