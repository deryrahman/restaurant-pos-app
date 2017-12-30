package exception;

public class AuthenticationServiceException extends Exception {
    public AuthenticationServiceException() {
        super();
    }

    public AuthenticationServiceException(String s) {
        super(s);
    }

    public AuthenticationServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AuthenticationServiceException(Throwable throwable) {
        super(throwable);
    }
}
