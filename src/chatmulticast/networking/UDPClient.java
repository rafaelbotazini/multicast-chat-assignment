package chatmulticast.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import chatmulticast.Config;

public class UDPClient {
    private final InetAddress serverAdress;
    private final int serverPort;

    private DatagramSocket serverSocket;

    public UDPClient(String host, int port) throws UnknownHostException, SocketException {
        this.serverAdress = InetAddress.getByName(host);
        this.serverPort = port;
        serverSocket = new DatagramSocket();
    }

    public synchronized String send(String message) throws IOException {
        DatagramPacket request = new DatagramPacket(
            message.getBytes(),
            message.length(),
            serverAdress,
            serverPort);

        serverSocket.send(request);

        byte[] buffer = new byte[Config.MAX_BUFFER_SIZE];

        DatagramPacket response = new DatagramPacket(buffer, buffer.length);
        serverSocket.receive(response);

        String responseMessage = new String(response.getData()).trim();

        return responseMessage;
    }
}
