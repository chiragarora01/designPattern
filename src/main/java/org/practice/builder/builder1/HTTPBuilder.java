package org.practice.builder.builder1;

import java.util.Objects;

public final class HTTPBuilder {
    private final String url;
    private final String method;
    private final String headers;
    private final String query;
    private final String body;
    private final String timeout;

    private HTTPBuilder(Builder builder){
        if (builder.url == null || builder.url.isBlank()) {
            throw new IllegalStateException("URL is required");
        }
        if ("GET".equalsIgnoreCase(builder.method) && builder.body != null && !builder.body.isBlank()) {
            throw new IllegalStateException("GET request cannot have a body");
        }
        this.url = builder.url;
        this.method = Objects.requireNonNullElse(builder.method,"GET");
        this.query = Objects.requireNonNullElse(builder.query,"");
        this.headers = Objects.requireNonNullElse(builder.headers,"");
        this.body = Objects.requireNonNullElse(builder.body,"");
        this.timeout = Objects.requireNonNullElse(builder.timeout,"30s");
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getHeaders() {
        return headers;
    }

    public String getQuery() {
        return query;
    }

    public String getBody() {
        return body;
    }

    public String getTimeout() {
        return timeout;
    }

    public static class Builder{
        private String url;
        private String method;
        private String headers;
        private String query;
        private String body;
        private String timeout;

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder method(String method){
            this.method = method;
            return this;
        }

        public Builder headers(String headers){
            this.headers = headers;
            return this;
        }

        public Builder query(String query){
            this.query = query;
            return this;
        }


        public Builder body(String body){
            this.body = body;
            return this;
        }


        public Builder timeout(String timeout){
            this.timeout = timeout;
            return this;
        }

        public HTTPBuilder build(){
            return new HTTPBuilder(this);
        }


    }

}
