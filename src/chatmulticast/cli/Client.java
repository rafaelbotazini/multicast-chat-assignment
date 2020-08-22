package chatmulticast.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.UnknownHostException;

import chatmulticast.networking.MulticastMessageReceivedHandler;
import chatmulticast.networking.MulticastPeer;
import chatmulticast.networking.UDPClient;
import chatmulticast.networking.UDPResponse;

public class Client {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final UDPClient udpClient;

    private Thread multicastThread;
    private MulticastPeer multicastPeer;

    private Client(String host, int port) throws IOException {
        this.udpClient = new UDPClient(host, port);
        connect();
    }

    public static Client create(String host, int port) throws IOException {
        return new Client(host, port);
    }

    private void connect() throws IOException {
        while(true) {
            String username = getUsername();
            UDPResponse response = makeRequest("CONNECT " + username);

            if (response.isOk()) {
                System.out.println("Conectando com o servidor...");
                startChat(response.getBody());
                return;
            } else {
                System.out.println("SERVIDOR: " + response.getBody() + " Tente novamente.");
            }
        }
    }

    private UDPResponse makeRequest(String request) throws IOException {
        return UDPResponse.parse(udpClient.send(request));
    }

    private String getUsername() {
        while (true) {
            try {
                System.out.println("Escolha um nome de usuário:");
                String username = reader.readLine().trim();
                return username;
            } catch (IOException e) {
                System.out.println("Ocorreu um erro. Tente novamente. " + e.getMessage());
            }
        }
    }

    private void startChat(String groupId) {
        // Start multicast receive thread

        try {
            multicastPeer = new MulticastPeer(groupId);
            multicastPeer.setReceiveHandler(new MulticastMessageReceivedHandler() {
                @Override
                public void handle(String message) {
                    System.out.println(message);
                }
            });

            multicastThread = new Thread(multicastPeer);
            multicastThread.start();

            System.out.println("Conectado.");
        } catch (Exception e) {
            System.out.println("[ERRO]: Erro ao tentar se conectar com o servidor...");
        }

        System.out.println("Digite uma mensagem.");

        // await message send input
        while (true) {
            try {
                String message = reader.readLine().trim();
                UDPResponse response;

                switch (message) {
                case "\\list":
                    response = makeRequest("LIST");
                    break;
                case "\\disconnect":
                    response = makeRequest("DISCONNECT");
                    System.exit(0);
                    break;
                default:
                    response = makeRequest("MESSAGE " + message);
                    break;
                }

                if (response.hasBody()) {
                    System.out.println(response.getBody());
                }

            } catch (IOException e) {
                System.out.println("[ERRO]: Erro ao processar a mensagem.\n\t" + e.getMessage());
            }
        }
    }
}
