package cs340.getpet.http.rest;

public class ErrorResponse implements ResponseBody {
    public final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
