package exception;

public class TeachersIdNotSequentialException extends RuntimeException {

    public TeachersIdNotSequentialException() {
        super("the teachers in the file are not arranged in sequential order");
    }
}
