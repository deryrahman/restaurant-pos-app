package exception;

public class DataNotFoundException extends AuthenticationServiceException {
    public DataNotFoundException() {
    }

    public DataNotFoundException(String s) {
        super(s);
    }

    public DataNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DataNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
