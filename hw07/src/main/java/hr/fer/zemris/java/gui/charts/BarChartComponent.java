package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class BarChartComponent extends JComponent {

    public final BarChart barChart;

    public BarChartComponent(BarChart barChart) {
        this.barChart = barChart;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Dimension componentDim = getSize();
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Font defaultFont = g2d.getFont();
        AffineTransform defaultTransform = g2d.getTransform();

        List<String> yNumbers = new ArrayList<>();

        int numbersMaxLength = 0;
        for(int i = barChart.getMinY(); i <= barChart.getMaxY(); i += barChart.getDeltaY()){
            String numberString = String.valueOf(i);
            yNumbers.add(numberString);
            numbersMaxLength = Math.max(numbersMaxLength, fm.stringWidth(numberString));
        }

        final int topGraphPadding = 20;
        final int rightGraphPadding = 20;
        final int yDescriptionWidth = fm.getHeight() + 40;
        final int yNumbersWidth = numbersMaxLength;
        final int xDescriptionHeight = 20;
        final int xNumbersHeight = fm.getHeight() + 20;

        final int  lineEnding = 4;

        // Y number line
        final int yCells = (barChart.getMaxY() - barChart.getMinY()) / barChart.getDeltaY();

        double currentYPosition = componentDim.height - xDescriptionHeight - xNumbersHeight;
        double yCellHeight = (1.0 * componentDim.height - xDescriptionHeight - xNumbersHeight - topGraphPadding) / yCells;
        for(int i = 0; i < yNumbers.size(); i++){

            g2d.drawString(yNumbers.get(i),
                    yDescriptionWidth + yNumbersWidth - g.getFontMetrics().stringWidth(yNumbers.get(i)) - 6,
                    (int) Math.round(currentYPosition + 4));

            if(i != 0){
                g2d.setColor(Color.LIGHT_GRAY);

                g2d.drawLine(
                        yDescriptionWidth + yNumbersWidth - lineEnding,
                        (int) Math.round(currentYPosition),
                        componentDim.width - rightGraphPadding + lineEnding,
                        (int) Math.round(currentYPosition)
                );
                g2d.setColor(Color.BLACK);
            }

            currentYPosition -= yCellHeight;
        }

        String yDescription = barChart.getyDescription();

        AffineTransform at = new AffineTransform();
        at.rotate(- Math.PI / 2);
        g2d.setTransform(at);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.drawString(
                yDescription,
                -(topGraphPadding + componentDim.height + 16),
                 //       + g2d.getFontMetrics().stringWidth(yDescription)/2),
                yDescriptionWidth - 6);
        g2d.setTransform(defaultTransform);
        g2d.setFont(defaultFont);

        // X number line
        final int xCells = barChart.getValues().size();
        final double xCellWidth = (1.0 * componentDim.width - yDescriptionWidth - yNumbersWidth - rightGraphPadding) / xCells;

        double currentXPosition = yDescriptionWidth + yNumbersWidth;
        List<XYValue> values = barChart.getValues();
        for(int i = 0; i < values.size(); i++){

            String xNumberString = String.valueOf(values.get(i).getX());

            g2d.drawString(xNumberString,
                    Math.round(currentXPosition + xCellWidth / 2 - 1.0 * fm.stringWidth(xNumberString) / 2),
                    componentDim.height - xDescriptionHeight - xNumbersHeight + fm.getAscent() + 2);

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(
                    (int) Math.round(currentXPosition + xCellWidth),
                    topGraphPadding - lineEnding,
                    (int) Math.round(currentXPosition + xCellWidth),
                    componentDim.height - xDescriptionHeight - xNumbersHeight + lineEnding
            );
            g2d.setColor(Color.BLACK);


            currentXPosition += xCellWidth;
        }

        String xDescription = barChart.getxDescription();

        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString(
                xDescription,
                (componentDim.width - yDescriptionWidth - yNumbersWidth - rightGraphPadding) / 2 + yDescriptionWidth + yNumbersWidth - getFontMetrics(g2d.getFont()).stringWidth(xDescription)/2,
                componentDim.height - fm.getHeight() + 3);
        g2d.setFont(defaultFont);


        // draw rectangles
        currentXPosition = yDescriptionWidth + yNumbersWidth;
        for(int i = 0; i < values.size(); i++){
            int y = values.get(i).getY() / barChart.getDeltaY();

            g2d.setColor(Color.ORANGE);
            g2d.fillRect(
                    (int) Math.round(currentXPosition),
                    (int) Math.round(componentDim.height - xDescriptionHeight - xNumbersHeight - y * yCellHeight),
                    (int) Math.round(xCellWidth),
                    (int) Math.round(y * yCellHeight));

            g2d.setColor(Color.RED);
            g2d.drawRect(
                    (int) Math.round(currentXPosition),
                    (int) Math.round(componentDim.height - xDescriptionHeight - xNumbersHeight - y * yCellHeight),
                    (int) Math.round(xCellWidth),
                    (int) Math.round(y * yCellHeight));
            g2d.setColor(Color.BLACK);
            currentXPosition += xCellWidth;
        }


        // number line arrows

        // Y
        g2d.drawLine(
                yDescriptionWidth + yNumbersWidth,
                topGraphPadding - lineEnding,
                yDescriptionWidth + yNumbersWidth,
                componentDim.height - xDescriptionHeight - xNumbersHeight + lineEnding
        );
        Polygon yArrow = new Polygon();
        yArrow.addPoint(-3, 0);
        yArrow.addPoint(3, 0);
        yArrow.addPoint(0, -6);
        yArrow.translate(
                yDescriptionWidth + yNumbersWidth,
                topGraphPadding - lineEnding
        );
        g2d.fillPolygon(yArrow);

        // X
        g2d.drawLine(
                yDescriptionWidth + yNumbersWidth - lineEnding,
                componentDim.height - xDescriptionHeight - xNumbersHeight,
                componentDim.width - rightGraphPadding + lineEnding,
                componentDim.height - xDescriptionHeight - xNumbersHeight
        );

        Polygon xArrow = new Polygon();
        xArrow.addPoint(0, -3);
        xArrow.addPoint(0, 3);
        xArrow.addPoint(6, 0);
        xArrow.translate(
                componentDim.width - rightGraphPadding + lineEnding,
                componentDim.height - xDescriptionHeight - xNumbersHeight
        );
        g2d.fillPolygon(xArrow);

    }
}
