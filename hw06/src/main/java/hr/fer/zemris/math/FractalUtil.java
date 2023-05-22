package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class FractalUtil {


    public static Complex[] inputRoots(){

        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

        Scanner sc = new Scanner(System.in);

        List<Complex> complexList = new ArrayList<>();
        while(true){
            System.out.print("Root " + (complexList.size()+1) + " > ");

            String complexString = sc.nextLine();

            if(complexString.toLowerCase().trim().equals("done")){
                if(complexList.size() >= 2){
                    break;
                } else {
                    System.out.println("At least two roots must be entered.");
                    continue;
                }
            }

            Complex c = null;
            try {
                c = Complex.parseComplex(complexString);
            } catch (NumberFormatException ex){
                System.out.println("Error parsing given complex number.");
                System.exit(1);
            }

            complexList.add(c);
        }

        return complexList.toArray(new Complex[]{});
    }

    public static void calculate(Function<Complex, Short> mapComplexToData, double reMin, double reMax,
                                 double imMin, double imMax, int width, int height,
                                 int ymin, int ymax, short[] data, AtomicBoolean cancel) {
        int offset = ymin * width;

        for(int y = ymin; y <= ymax && !cancel.get(); ++y) {
            for(int x = 0; x < width; ++x) {

                Complex c = mapToComplexPlane(x, y, reMin, reMax, imMin, imMax, width, height);

                data[offset++] = mapComplexToData.apply(c);
            }
        }

    }

    public static Complex mapToComplexPlane(int x, int y, double reMin, double reMax,
                                            double imMin, double imMax, int width, int height) {
        double re = (double) x / ( (double) width - 1.0) * (reMax - reMin) + reMin;
        double im = (double) (height - 1 - y) / ( (double) height - 1.0) * (imMax - imMin) + imMin;

        return new Complex(re, im);
    }

}
