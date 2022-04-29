package exception;

public class FileErrorException extends RuntimeException {

    public FileErrorException(String msg) {
        super(msg);
    }
}
