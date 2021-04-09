package cs340.getpet.http.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Endpoint {
    final Path path;
    final MethodHandler<?, ?> getHandler;
    final MethodHandler<?, ?> postHandler;
    final MethodHandler<?, ?> putHandler;
    final MethodHandler<?, ?> deleteHandler;

    static final class Path {
        private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\w+\\}");
    
        public final String readablepath;
    
        private final Pattern pathPattern;
        private final String[] pathVariables;
    
        public Path(final String path) {
            this.readablepath = path;
    
            ArrayList<String> pathVariables = new ArrayList<>();
            final String pathPatternString = VARIABLE_PATTERN.matcher(path).replaceAll(match -> {
                String matchGroup = match.group();
                String variableName = matchGroup.substring(1, matchGroup.length() - 1);
                pathVariables.add(variableName);
    
                return "(?<" + variableName + ">[^/\\?#]+)";
            });
    
            this.pathPattern = Pattern.compile(pathPatternString);
            this.pathVariables = pathVariables.toArray(new String[0]);
        }
    
        public PathVariables match(final String path) {
            final Matcher matcher = pathPattern.matcher(path);
    
            if (matcher.matches()) {
                Map<String, String> variablesMap = new HashMap<>();
    
                for (String pathVariable : pathVariables)
                    variablesMap.put(pathVariable, matcher.group(pathVariable));
                
                return new PathVariables(variablesMap);
            } else return null;
        }
    }

    public static final class PathVariables {
        private final Map<String, String> variables;
    
        PathVariables(Map<String, String> variables) {
            this.variables = variables;
        }
    
        public String get(String s) {
            return variables.get(s);
        }

        public int getInt(String s) throws RestException {
            try {
                return Integer.parseInt(get(s));
            } catch (NumberFormatException e) {
                throw new RestException(RestException.Code.INVALID_PATH_PARAMETER, e);
            }
        }
    }

    static final class MethodHandler<Req extends RequestBody, Resp extends ResponseBody> {
        public final RequestHandler<Req, Resp> handler;
        public final Class<Req> requestClass;
        public final Class<Resp> responseClass;

        public MethodHandler(Class<Req> requestClass, Class<Resp> responseClass, RequestHandler<Req, Resp> handler) {
            this.requestClass = requestClass;
            this.responseClass = responseClass;
            this.handler = handler;
        }
    }

    public static final class Builder {
        private String path;
        private MethodHandler<?, ?> getHandler;
        private MethodHandler<?, ?> postHandler;
        private MethodHandler<?, ?> putHandler;
        private MethodHandler<?, ?> deleteHandler;

        public Builder() {}

        public Endpoint build() {
            return new Endpoint(this);
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public <Resp extends ResponseBody> Builder get(Class<Resp> responseClass, RequestHandler<RequestBody.EmptyRequest, Resp> handler) {
            this.getHandler = new MethodHandler<>(RequestBody.EmptyRequest.class, responseClass, handler);
            return this;
        }

        public <Req extends RequestBody, Resp extends ResponseBody> Builder post(Class<Req> requestClass, Class<Resp> responseClass, RequestHandler<Req, Resp> handler) {
            this.postHandler = new MethodHandler<>(requestClass, responseClass, handler);
            return this;
        }

        public <Req extends RequestBody, Resp extends ResponseBody> Builder put(Class<Req> requestClass, Class<Resp> responseClass, RequestHandler<Req, Resp> handler) {
            this.putHandler = new MethodHandler<>(requestClass, responseClass, handler);
            return this;
        }

        public <Req extends RequestBody, Resp extends ResponseBody> Builder delete(Class<Req> requestClass, Class<Resp> responseClass, RequestHandler<Req, Resp> handler) {
            this.deleteHandler = new MethodHandler<>(requestClass, responseClass, handler);
            return this;
        }
    }

    private Endpoint(Builder b) {
        this.path = new Path(b.path);
        this.getHandler = b.getHandler;
        this.postHandler = b.postHandler;
        this.putHandler = b.putHandler;
        this.deleteHandler = b.deleteHandler;
    }
}