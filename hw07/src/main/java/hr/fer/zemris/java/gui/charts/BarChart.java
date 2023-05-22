package hr.fer.zemris.java.gui.charts;

import java.util.List;

public class BarChart {

    private final List<XYValue> values;

    private final String xDescription;

    private final String yDescription;

    private final int minY;

    private final int maxY;

    private final int deltaY;

    public BarChart(List<XYValue> values, String xDescription, String yDescription, int minY, int maxY, int deltaY) {
        if(minY < 0){
            throw new IllegalArgumentException();
        }

        if(minY >= maxY){
            throw new IllegalArgumentException();
        }

        values.forEach(xyValue -> {
            if(xyValue.getY() < minY){
                throw new IllegalArgumentException();
            }
        });

        this.values = values;
        this.xDescription = xDescription;
        this.yDescription = yDescription;
        this.maxY = maxY;
        this.deltaY = deltaY;

        if((maxY - minY) % deltaY != 0){
            this.minY = maxY - (maxY - minY) / deltaY * deltaY;
        } else {
            this.minY = minY;
        }
    }

    public List<XYValue> getValues() {
        return values;
    }

    public String getxDescription() {
        return xDescription;
    }

    public String getyDescription() {
        return yDescription;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getDeltaY() {
        return deltaY;
    }
}
