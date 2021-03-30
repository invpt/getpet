package cs340.getpet.http.rest;

public interface ResponseBody {
    public static final EmptyResponse EMPTY_RESPONSE = new EmptyResponse();

    /**
     * Represents a response with no body. An instance is available at {@code Response.EMPTY_RESPONSE}.
     */
    public static final class EmptyResponse implements ResponseBody {
        private EmptyResponse() {}
    }

    /**
     * Represents a response that will never be constructed.
     */
    public static final class NeverResponse implements ResponseBody {
        private NeverResponse() {}
    }
}
