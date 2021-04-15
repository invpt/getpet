package cs340.getpet.http.rest;

public interface RequestBody {
    /**
     * Validates the data contained in the request.
     * <p>
     * The default implementation of this method accepts all input.
     * 
     * @throws ValidationException when the data in the request is invalid
     */
    default void validate() throws ValidationException {}

    public static final class EmptyRequest implements RequestBody {
        private EmptyRequest() {}
    }
}
