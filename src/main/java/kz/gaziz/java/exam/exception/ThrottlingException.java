package kz.gaziz.java.exam.exception;

public class ThrottlingException extends RuntimeException {
    public ThrottlingException(String message) {
        super(message);
    }
}
