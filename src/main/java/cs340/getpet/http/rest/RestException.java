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
        NOT_FOUND("Not found"),
        INVALID_DATA("Invalid input data"),
        INVALID_STRUCTURE("Invalid input structure"),
        INTERNAL("Internal error");

        public final String message;

        Code(String message) {
            this.message = message;
        }

        public int httpResponseCode() {
            switch (this) {
                case INVALID_METHOD:
                case UNKNOWN_METHOD:
                case INVALID_PATH_PARAMETER:
                case INVALID_DATA:
                case INVALID_STRUCTURE:
                    return 400;
                case NOT_FOUND:
                    return 404;
                case INTERNAL:
                default:
                    return 500;
            }
        }
    }
}
