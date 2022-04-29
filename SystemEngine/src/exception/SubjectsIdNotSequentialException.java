package exception;

public class SubjectsIdNotSequentialException extends RuntimeException{

    public SubjectsIdNotSequentialException() {
        super("the subjects in the file are not arranged in sequential order");
    }
}
