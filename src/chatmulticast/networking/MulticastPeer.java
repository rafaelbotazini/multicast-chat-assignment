package chatmulticast.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import chatmulticast.Config;

public class MulticastPeer implements Runnable {
    private final InetAddress groupId;
    private final MulticastSocket socket;

    private MulticastMessageReceivedHandler receiveHandler;

    public MulticastPeer(String groupId) throws IOException {
        this.groupId = InetAddress.getByName(groupId);
        socket = new MulticastSocket(Config.DEFAULT_MULTICAST_CLIENT_PORT);
        socket.joinGroup(this.groupId);
    }

    public synchronized void send(String message) throws IOException {
        DatagramPacket request = new DatagramPacket(
            message.getBytes(),
            message.length(),
            groupId,
            Config.DEFAULT_MULTICAST_CLIENT_PORT);

        socket.send(request);
    }

    private synchronized void receive() {
        byte[] buffer = new byte[Config.MAX_BUFFER_SIZE];

        String message;

        try {
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            message = new String(response.getData()).trim();
        } catch (IOException e) {
            message = "[ERRO]: Não foi possível obter a mensagem do servidor.";
        }

        if (receiveHandler != null)
            receiveHandler.handle(message);
    }

    public void setReceiveHandler(MulticastMessageReceivedHandler receiveHandler) {
        this.receiveHandler = receiveHandler;
    }

    @Override
    public void run() {
        while (true) {
            receive();
        }
    }
}
