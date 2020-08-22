package chatmulticast.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import chatmulticast.Config;

public class UDPServer {
    private DatagramSocket socket;
    private UDPRequestHandler requestHandler;

    public void listen(int port) throws SocketException, IOException {

        socket = new DatagramSocket(port);
        System.out.println("Servidor: ouvindo porta UDP/" + port + ".");

        while (true) {
            byte[] buffer = new byte[Config.MAX_BUFFER_SIZE];

            DatagramPacket request = new DatagramPacket(buffer, buffer.length);

            socket.receive(request);

            String data = new String(request.getData()).trim();

            System.out.println("[" + request.getAddress() + "]: " + data);

            String replyMessage = requestHandler != null 
                    ? requestHandler.reply(ChatRequest.parse(data, request.getAddress().toString())) 
                    : "NO CONTENT";

            DatagramPacket reply = new DatagramPacket(
                replyMessage.getBytes(),
                replyMessage.length(),
                request.getAddress(),
                request.getPort());

            socket.send(reply);
        }
    }

    public void close() {
        socket.close();
    }

    public void setRequestHandler(UDPRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }
}
