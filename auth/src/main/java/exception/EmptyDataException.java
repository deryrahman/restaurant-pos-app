package exception;

public class EmptyDataException extends FailedCRUDOperationException {
    public EmptyDataException() {
    }

    public EmptyDataException(String s) {
        super(s);
    }

    public EmptyDataException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EmptyDataException(Throwable throwable) {
        super(throwable);
    }
}
