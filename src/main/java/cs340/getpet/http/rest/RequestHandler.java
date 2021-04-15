package cs340.getpet.http.rest;

@FunctionalInterface
public interface RequestHandler<Req extends RequestBody, Resp extends ResponseBody> {
    Response<Resp> handle(Request<Req> request) throws RestException;
}
