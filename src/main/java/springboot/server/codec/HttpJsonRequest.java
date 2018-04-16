package springboot.server.codec;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author tangj
 * @date 2018/4/15 17:52
 */
public class HttpJsonRequest {
    private FullHttpRequest request;
    private Object body;

    public HttpJsonRequest(FullHttpRequest request, Object body) {
        this.request = request;
        this.body = body;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpJsonRequest{" +
                "request=" + request +
                ", body=" + body +
                '}';
    }
}
