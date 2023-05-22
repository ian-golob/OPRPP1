package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class BarChartDemo extends JFrame {

    private final BarChart model;
    public BarChartDemo(BarChart model) {
        this.model = model;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initGUI();
        pack();
    }
    private void initGUI() {
        Container cp = getContentPane();
        setMinimumSize(new Dimension(500, 500));
        cp.setLayout(new BorderLayout());

        BarChartComponent chart = new BarChartComponent(model);
        cp.add(chart, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Expected 1 argument, got: " + args.length);
            System.exit(1);
        }

        try(Scanner sc = new Scanner(Path.of(args[0]))){

            String xDescription = sc.nextLine().trim();
            String yDescription = sc.nextLine().trim();
            String xyValuesString = sc.nextLine();
            int minY = Integer.parseInt(sc.nextLine());
            int maxY = Integer.parseInt(sc.nextLine());
            int deltaY = Integer.parseInt(sc.nextLine());

            Scanner xySc = new Scanner(xyValuesString);
            List<XYValue> xyValues = new ArrayList<>();
            while(xySc.hasNext()){
                String[] xy = xySc.next().split(",");

                if(xy.length != 2){
                    throw new IllegalArgumentException();
                }

                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                xyValues.add(new XYValue(x, y));
            }

            BarChart model = new BarChart(
                    xyValues,
                    xDescription,
                    yDescription,
                    minY,
                    maxY,
                    deltaY
            );

            SwingUtilities.invokeLater(()->{
                new BarChartDemo(model).setVisible(true);
            });

        } catch (NoSuchElementException | IllegalArgumentException | IOException ex){
            System.out.println("Error reading file.");
        }
    }


}
