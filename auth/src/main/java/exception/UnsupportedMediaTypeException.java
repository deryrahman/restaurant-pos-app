package exception;

public class UnsupportedMediaTypeException extends Exception {
    public UnsupportedMediaTypeException() {
    }

    public UnsupportedMediaTypeException(String s) {
        super(s);
    }

    public UnsupportedMediaTypeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public UnsupportedMediaTypeException(Throwable throwable) {
        super(throwable);
    }
}
