package exception;

public class ElitismBiggerThanPopulationException extends RuntimeException{
    private int elitismSize;

    public ElitismBiggerThanPopulationException(int elitismSize) {
        super("Elitism is bigger than population"+"("+elitismSize+")");
        this.elitismSize = elitismSize;
    }
}
