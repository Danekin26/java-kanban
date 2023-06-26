package exceptions;

public class ExcludingCrossing extends RuntimeException {
    public ExcludingCrossing(String message) {
        super(message);
    }
}
