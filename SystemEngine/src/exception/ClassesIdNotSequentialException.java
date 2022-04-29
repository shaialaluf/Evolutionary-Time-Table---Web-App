package exception;

public class ClassesIdNotSequentialException extends RuntimeException {

    public ClassesIdNotSequentialException() {
        super("the classes in the file are not arranged in sequential order");
    }
}
