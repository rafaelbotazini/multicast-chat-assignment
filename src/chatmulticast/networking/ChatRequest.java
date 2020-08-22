package chatmulticast.networking;

import java.security.InvalidParameterException;

public class ChatRequest {
    private String method;
    private String host;
    private String body;

    public ChatRequest(String method, String from, String body) {
        this.method = method;
        this.host = from;
        this.body = body;
    }

    public static ChatRequest parse(String data, String host) throws InvalidParameterException {
      String method = data.indexOf(' ') > -1 ? data.substring(0, data.indexOf(' ')) : data;
      String body = data.indexOf(' ') > -1 ? data.substring(data.indexOf(' ') + 1) : "";
      return new ChatRequest(method, host, body);
    }

    public String getMethod() {
        return method;
    }

    public String getHost() {
        return host;
    }

    public String getBody() {
        return body;
    }
}
