package cs340.getpet.http.rest;

public class ValidationException extends RestException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}