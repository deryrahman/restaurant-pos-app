package exception;

public class BadDataException extends FailedCRUDOperationException {
    public BadDataException() {
        super();
    }

    public BadDataException(String s) {
        super(s);
    }

    public BadDataException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BadDataException(Throwable throwable) {
        super(throwable);
    }
}
