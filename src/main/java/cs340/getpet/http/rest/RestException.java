package cs340.getpet.http.rest;

public class RestException extends Exception {
    public final Code code;

    public RestException(Code code) {
        super(code.message);

        this.code = code;
    }

    public RestException(Code code, Throwable cause) {
        super(code.message, cause);

        this.code = code;
    }

    public RestException(String message) {
        super(message);

        this.code = null;
    }

    public RestException(Throwable cause) {
        super(cause);

        this.code = null;
    }

    public RestException(String message, Throwable cause) {
        super(message, cause);

        this.code = null;
    }

    public enum Code {
        INVALID_METHOD("Invalid method"), UNKNOWN_METHOD("Unknown method"), INVALID_PATH_PARAMETER("Invalid path parameter"), UNKNOWN_ENDPOINT("Unknown endpoint");

        public final String message;

        Code(String message) {
            this.message = message;
        }
    }
}
