package exception;

public class DatabaseFailureException extends FailedCRUDOperationException {
    public DatabaseFailureException() {
        super();
    }

    public DatabaseFailureException(String s) {
        super(s);
    }

    public DatabaseFailureException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DatabaseFailureException(Throwable throwable) {
        super(throwable);
    }
}
