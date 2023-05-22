package hr.fer.zemris.math;

public class FractalArgumentParser {

    private Integer specifiedWorkerNumber;
    private Integer specifiedTrackNumber;

    public FractalArgumentParser(String[] args) {
        parse(args);
    }

    private void parse(String[] args) {
        try {

            for(int i = 0; i < args.length; i++){
                String arg = args[i];

                if(arg.startsWith("--workers=")){
                    requireNull(specifiedWorkerNumber);

                    specifiedWorkerNumber = requirePositive(
                            Integer.parseInt(arg.substring("--workers=".length())));

                } else if(arg.startsWith("--tracks=")){
                    requireNull(specifiedTrackNumber);

                    specifiedTrackNumber = requirePositive(
                            Integer.parseInt(arg.substring("--tracks=".length())));

                } else if(arg.equals("-w")) {
                    requireNull(specifiedWorkerNumber);

                    if(i+1 == args.length){
                        throw new IllegalArgumentException();
                    }

                    specifiedWorkerNumber = requirePositive(
                            Integer.parseInt(args[++i]));
                } else if(arg.equals("-t")) {
                    requireNull(specifiedTrackNumber);

                    if(i+1 == args.length){
                        throw new IllegalArgumentException();
                    }

                    specifiedTrackNumber = requirePositive(
                            Integer.parseInt(args[++i]));
                } else {
                    throw new IllegalArgumentException();
                }
            }

        } catch (IllegalArgumentException ex){
            System.out.println("Illegal command line arguments given.");
            System.exit(1);
        }
    }

    private Integer requirePositive(int integer) {
        if(integer < 1){
            throw new IllegalArgumentException();
        }

        return integer;
    }

    private void requireNull(Object obj) {
        if(obj != null){
            throw new IllegalArgumentException();
        }
    }


    public Integer getWorkerNumber() {
        if(specifiedWorkerNumber == null){
            return Runtime.getRuntime().availableProcessors()*4;
        }

        return specifiedWorkerNumber;
    }

    public Integer getTrackNumber(int rows) {
        if(specifiedTrackNumber == null){
            return Runtime.getRuntime().availableProcessors()*4;
        }

        return Math.min(specifiedTrackNumber, rows);
    }
}
