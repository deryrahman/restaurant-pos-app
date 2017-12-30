package exception;

public class FailedCRUDOperationException extends AuthenticationServiceException {
    public FailedCRUDOperationException() {
        super();
    }

    public FailedCRUDOperationException(String s) {
        super(s);
    }

    public FailedCRUDOperationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FailedCRUDOperationException(Throwable throwable) {
        super(throwable);
    }
}
