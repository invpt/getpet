package cs340.getpet.http.rest;

import cs340.getpet.http.rest.Endpoint.PathVariables;

public class Request<Body extends RequestBody> {
    public final PathVariables pathVariables;
    public final Body body;

    Request(PathVariables pathVariables, Body body) {
        this.pathVariables = pathVariables;
        this.body = body;
    }
}
