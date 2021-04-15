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

    public enum Code {
        INVALID_METHOD("Invalid method"),
        UNKNOWN_METHOD("Unknown method"),
        INVALID_PATH_PARAMETER("Invalid path parameter"),
        UNKNOWN_ENDPOINT("Unknown endpoint"),
        INVALID_DATA("Invalid input data"),
        INVALID_STRUCTURE("Invalid input structure"),
        INTERNAL("Internal error");

        public final String message;

        Code(String message) {
            this.message = message;
        }
    }
}
