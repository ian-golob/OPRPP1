package hr.fer.zemris.math;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.util.concurrent.atomic.AtomicBoolean;

public class Newton {
    public static void main(String[] args) {
        Complex[] roots = FractalUtil.inputRoots();

        FractalViewer.show(new NewtonFractalProducer(roots));
    }

    public static class NewtonFractalProducer implements IFractalProducer {
        private final Complex[] roots;
        private final ComplexRootedPolynomial polynomial;
        private final NewtonRaphsonFractalFunction function;

        public NewtonFractalProducer(Complex[] roots) {
            this.roots = roots;
            polynomial = new ComplexRootedPolynomial(Complex.ONE, roots);
            function = new NewtonRaphsonFractalFunction(16*16*16, polynomial, 0.00001, 0.01);
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer,
                            AtomicBoolean cancel) {

            short[] data = new short[width * height];

            FractalUtil.calculate(function, reMin, reMax, imMin, imMax, width, height, 0, height-1, data, cancel);

            observer.acceptResult(data, (short) (roots.length+1), requestNo);
        }
    }
}
