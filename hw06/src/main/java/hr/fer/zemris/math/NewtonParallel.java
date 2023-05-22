package hr.fer.zemris.math;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class NewtonParallel {
    public static void main(String[] args) {
        FractalArgumentParser argumentParser = new FractalArgumentParser(args);

        Complex[] roots = FractalUtil.inputRoots();

        FractalViewer.show(new NewtonParallelFractalProducer(argumentParser, roots));
    }

    public static class NewtonParallelFractalProducer implements IFractalProducer {

        private final FractalArgumentParser argumentParser;
        private final Complex[] roots;
        private final ComplexRootedPolynomial polynomial;
        private final NewtonRaphsonFractalFunction function;

        public static class Job implements Runnable {

            private Function<Complex, Short> mapComplexToData;
            private double reMin;
            private double reMax;
            private double imMin;
            private double imMax;
            private int width;
            private int height;
            private int yMin;
            private int yMax;
            private short[] data;
            private AtomicBoolean cancel;
            public static Job NO_JOB = new Job();

            private Job() {
            }

            public Job(Function<Complex, Short> mapComplexToData, double reMin, double reMax, double imMin,
                       double imMax, int width, int height, int yMin, int yMax,
                       short[] data, AtomicBoolean cancel) {
                this.mapComplexToData = mapComplexToData;
                this.reMin = reMin;
                this.reMax = reMax;
                this.imMin = imMin;
                this.imMax = imMax;
                this.width = width;
                this.height = height;
                this.yMin = yMin;
                this.yMax = yMax;
                this.data = data;
                this.cancel = cancel;
            }

            @Override
            public void run() {
                FractalUtil.calculate(mapComplexToData, reMin, reMax, imMin, imMax, width, height, yMin, yMax, data, cancel);
            }
        }

        public NewtonParallelFractalProducer(FractalArgumentParser argumentParser, Complex[] roots) {
            this.argumentParser = argumentParser;

            this.roots = roots;
            polynomial = new ComplexRootedPolynomial(Complex.ONE, roots);
            function = new NewtonRaphsonFractalFunction(16*16*16, polynomial, 0.0001, 0.01);
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer,
                            AtomicBoolean cancel) {

            int workerNumber = argumentParser.getWorkerNumber();
            int trackNumber = argumentParser.getTrackNumber(height);

            System.out.println("Workers: " + workerNumber + ", Tracks: " + trackNumber);

            short[] data = new short[width * height];

            int yPerTrack = height / trackNumber;

            final BlockingQueue<Job> queue = new LinkedBlockingQueue<>();

            Thread[] workers = new Thread[workerNumber];
            for(int i = 0; i < workers.length; i++) {
                workers[i] = new Thread(() -> {
                    while(true) {
                        Job p = null;
                        try {
                            p = queue.take();
                            if(p==Job.NO_JOB) break;
                        } catch (InterruptedException e) {
                            continue;
                        }
                        p.run();
                    }
                });
            }
            for (Thread worker : workers) {
                worker.start();
            }

            for(int i = 0; i < trackNumber; i++) {
                int yMin = i*yPerTrack;
                int yMax = (i+1)*yPerTrack-1;
                if(i==trackNumber-1) {
                    yMax = height-1;
                }
                Job job = new Job(function, reMin, reMax, imMin, imMax,
                        width, height, yMin, yMax, data, cancel);
                while(true) {
                    try {
                        queue.put(job);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            for(int i = 0; i < workers.length; i++) {
                while(true) {
                    try {
                        queue.put(Job.NO_JOB);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            for (Thread worker : workers) {
                while (true) {
                    try {
                        worker.join();
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            observer.acceptResult(data, (short) (roots.length+1), requestNo);
        }
    }
}
