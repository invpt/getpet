package cs340.getpet.http.rest;

import cs340.getpet.http.rest.ResponseBody.EmptyResponse;

public class Response<Body extends ResponseBody> {
    public final int code;
    public final Body body;

    private Response(int code, Body body) {
        this.code = code;
        this.body = body;
    }

    public static <Body extends ResponseBody> Response<Body> withBody(int code, Body body) {
        return new Response<>(code, body);
    }

    public static Response<EmptyResponse> empty(int code) {
        return new Response<>(code, ResponseBody.EMPTY_RESPONSE);
    }
}
