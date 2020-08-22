package chatmulticast.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import chatmulticast.networking.ChatRequest;
import chatmulticast.networking.MulticastPeer;
import chatmulticast.networking.UDPRequestHandler;
import chatmulticast.networking.UDPServer;

public class ChatServer {
    private final HashMap<String, User> users = new HashMap<>();
    private final String multicastGroupId;
    private final MulticastPeer multicastPeer;
    private final UDPServer udpServer;

    private ChatServer(int udpPort, String multicastGroupId) throws IOException {
        this.multicastGroupId = multicastGroupId;

        this.multicastPeer = new MulticastPeer(multicastGroupId);
        this.udpServer = new UDPServer();

        this.udpServer.setRequestHandler(new UDPRequestHandler() {
            @Override
            public String reply(ChatRequest message) {
                return ChatServer.this.process(message);
            }
        });

        this.udpServer.listen(udpPort);
    }

    public static ChatServer create(int udpPort, String multicastGroupId) throws IOException {
        return new ChatServer(udpPort, multicastGroupId);
    }

    private String process(ChatRequest request) {
        System.out.println(request.getMethod() + "\t" + request.getBody() + "\t" + request.getHost());
        switch(request.getMethod()) {
            case "LIST":
                return listUsers();
            case "CONNECT":
                return addUser(request);
            case "REMOVE":
                return removeUser(request);
            case "MESSAGE":
                return sendGroupMessage(request);
            default:
                return "NOK Método inválido.";
        }
    }

    private String addUser(ChatRequest request) {
        if (users.get(request.getHost()) != null) {
            return "NOK Usuário já está conectado";
        }

        if (request.getBody().toUpperCase().equals("SERVER")) {
            return "NOK Nome de usuário inválido";
        }

        User user = new User(request.getBody(), request.getHost());

        users.put(user.getAddress(), user);

        String message = user.getName() + " chegou. Seja bem vindo(a), " + user.getName() + "!";

        sendGroupMessage("SERVER", message);

        return "OK " + multicastGroupId;
    }

    private String removeUser(ChatRequest request) {
        User user = users.get(request.getHost());

        if (user == null) {
            return "NOK Usuário não conectado";
        }
        sendGroupMessage("SERVER", user.getName() + " saiu.");
        users.remove(request.getHost());

        return "OK";
    }

    private String listUsers() {
        StringBuilder sb = new StringBuilder();

        for (User user : users.values()) {
            sb.append(user.getName() + "\n");
        }

        return "OK " + sb.toString();
    }

    private String sendGroupMessage(ChatRequest request) {
        User user = users.get(request.getHost());

        if (user == null) {
            return "NOK Usuário não conectado";
        }

        return sendGroupMessage(user.getName(), request.getBody());
    }

    private String sendGroupMessage(String username, String message) {
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String date = df.format(now);

        try {
            multicastPeer.send("[" + date + " " + username + "]: " + message);
        } catch (IOException e) {
            return "NOK Erro interno do servidor.";
        }

        return "OK";
    }
}
