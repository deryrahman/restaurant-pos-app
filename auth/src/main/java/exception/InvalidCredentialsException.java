package exception;

public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException() {
    }

    public InvalidCredentialsException(String s) {
        super(s);
    }

    public InvalidCredentialsException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidCredentialsException(Throwable throwable) {
        super(throwable);
    }

}
