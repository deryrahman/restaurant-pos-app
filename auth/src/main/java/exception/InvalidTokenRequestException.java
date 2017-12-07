package exception;

public class InvalidTokenRequestException extends Exception {
    public InvalidTokenRequestException() {
    }

    public InvalidTokenRequestException(String s) {
        super(s);
    }

    public InvalidTokenRequestException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidTokenRequestException(Throwable throwable) {
        super(throwable);
    }
}
