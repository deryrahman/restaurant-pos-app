package exception;

public class DuplicateDataException extends FailedCRUDOperationException {
    public DuplicateDataException() {
        super();
    }

    public DuplicateDataException(String s) {
        super(s);
    }

    public DuplicateDataException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DuplicateDataException(Throwable throwable) {
        super(throwable);
    }
}
