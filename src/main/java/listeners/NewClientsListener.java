package listeners;

import client.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class NewClientsListener extends Thread{

    private ServerSocket _serverSocket;
    private Map<String, Socket> _usersList;
    private Scanner scanner;

    public NewClientsListener(ServerSocket serverSocket, Map<String, Socket> usersList, Scanner _scanner){
        this._serverSocket = serverSocket;
        this._usersList = usersList;
        this.scanner = _scanner;
    }

    public void start(){
        while(true){
            Socket client;
            try {
                String nick = null;
                client = this._serverSocket.accept();
                System.out.println(client.getLocalAddress() + " : " + client.getPort());

                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());

                Client newClient = new Client(client, new DisplayMessageClient(in), new SendMessageClient(out,this.scanner, "NEW USER"));

                newClient.startClient();

                newClient.getDisplay().

                while(nick == null){
                    nick = requestName(client);
                }



            }
            catch(IOException e){
                System.out.println("Exception caught when listening on port: " + this._serverSocket.getLocalPort() +
                        ". For more details check log file.");
            }
        }
    }

    private String requestName(Socket client){
        new ObjectOutputStream()
}
