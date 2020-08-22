package chatmulticast.networking;

public class UDPResponse {
    String status;
    String body;
    
    private UDPResponse() {}
    
    public static UDPResponse parse(String data) {
        UDPResponse response= new UDPResponse(); 
        String[] parts = data.split(" ");

        response.status = parts[0];
        response.body = parts.length > 1 ? data.substring(data.indexOf(' ')).trim() : "";
        
        return response;
    }
    
    public boolean isOk() {
        return status.equals("OK"); 
    }
    
    public boolean hasBody() {
        return getBody().length() > 0;
    }

    public String getStatus() {
        return status;
    }
    public String getBody() {
        return body;
    }
}
