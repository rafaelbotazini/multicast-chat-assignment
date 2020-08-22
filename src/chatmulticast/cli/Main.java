package chatmulticast.cli;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import chatmulticast.networking.UDPRequestHandler;
import chatmulticast.networking.UDPServer;
import chatmulticast.server.ChatServer;

public class Main {

    public static void main(String[] args) {
        String command = args[0];

        try {

            switch (command) {
                case "connect":
                    startClient(args[1], args[2]);
                    break;
                case "serve":
                    startServer(args[1]);
                    break;
                default:
                    System.out.println("Commando não reconhecido");
                    break;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Número de argumentos insuficiente.");
        }
    }

    private static void startServer(String port) {
        try {
            ChatServer.create(Integer.parseInt(port), "228.2.3.4");
        } catch (NumberFormatException e) {
            System.out.println("[ERROR]: Porta invalida.");
        } catch (IOException e) {
            System.out.println("[ERROR | IO]: ");
            e.printStackTrace();
        }
    }

    private static void startClient(String host, String port) {
        try {
            System.out.println("Conectando...");
            Client.create(host, Integer.parseInt(port));
        } catch (NumberFormatException e) {
            System.out.println("[ERROR]: Porta invalida.");
        } catch (IOException e) {
            System.out.println("Erro ao tentar conectar: " + e.getMessage());
        }
    }
}
