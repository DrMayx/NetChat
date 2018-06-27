package server;

import client.Client;
import listeners.ServerInputListener;
import util.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import java.util.Scanner;

public class ServerController {
    private static int userID = 0;
    private int portNumber;
    private ServerSocket _serverSocket;
    private static ServerInputListener serverChat;
    private static final ArrayList<Client> _clientsList = new ArrayList<>();

    public ServerController(int port){
        this.portNumber = port;
        try {
            this._serverSocket = new ServerSocket(portNumber);
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error Initializing server!!");
            System.exit(901);
        }
    }

    public void run() throws IOException{
        Scanner scanner = new Scanner(System.in);

        try {

            serverChat = new ServerInputListener(scanner, ServerController._clientsList);
            serverChat.start();

            while (true) {
                Socket client = this._serverSocket.accept();
                System.out.println("User connected at " + client.getLocalAddress() + ":" + client.getPort());

                new IncomingRequestHandler(client).start();
            }

        }catch(IOException e ){
            System.out.println("Error handling client");
            e.printStackTrace();
        }

    }

    private static class IncomingRequestHandler extends Thread {

        private final Socket socket;
        private Client thisClient;

        public IncomingRequestHandler(Socket socket) {

            this.socket = socket;

        }

        @Override
        public void run() {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                thisClient = new Client(out, userID++);
                synchronized (_clientsList) {
                    _clientsList.add(thisClient);
                }

                while (true) {
                    Message input = new Message("null", "null");
                    try {
                        input = (Message) in.readObject();
                        if(input.getContent().equals(".exit!")){
                            throw new SocketException();
                        }
                    }catch(SocketException userOut){
                        String author = input.getAuthor();

                        Client user = findUser(author);
                        _clientsList.remove(user);
                        user = null;
                        try {
                            String message = "user " + author + " left the server.";
                            System.out.println(message);
                            serverChat.sendMessageToAll(message);
                        }catch(NullPointerException e){
                            String message = "Someone left the server";
                            System.out.println(message);
                            serverChat.sendMessageToAll(message);
                        }
                    }catch(EOFException ex){
                        System.out.println(thisClient.name + " left.");
                        _clientsList.remove(thisClient);
                        return;
                    }
                    Message message = input;
                    if(thisClient.name.equals("NEW USER")){
                        out.writeObject(setClientName(thisClient, message));
                        continue;
                    }
                    if(message.getContent().startsWith("@")){
                        String target = message.getContent().split(" ")[0].replaceFirst("@","");
                        String content = message.getContent().replaceFirst("@" + target, "");
                        _clientsList.forEach(client -> {
                            try{
                                if(client.name.equals(target)){
                                    client.writeObject(new Message(content, "PRIVATE::" + message.getAuthor()));
                                }
                            }catch(IOException e){
                                System.out.println("error sending!");
                            }
                        });
                    } else {
                        _clientsList.forEach(client -> {
                            try {
                                client.writeObject(message);
                            } catch (IOException e) {
                                System.out.println("Error with sending message to client!");
                                System.out.println(e.getMessage());
                                return;
                            }
                        });
                    }
                    if(!message.getAuthor().equals("SERVER")) {
                        System.out.println(message);
                    }
                }


            } catch (EOFException e) {
                String userAddress = socket.getInetAddress() + ":" + socket.getPort();
                System.out.println("User with adress: " + userAddress + " has been disconnected.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private String setClientName(Client client, Message message){
            boolean nameSet = true;
            for(Client c : _clientsList){
                if(c.name.equals(message.getAuthor())){
                    nameSet = false;
                }
            }
            if(nameSet){
                client.name = message.getAuthor();
            }
            return nameSet ? "true" : "false";
        }

        private Client findUser(String name){
            for(Client c : _clientsList){
                if(c.name.equals(name)){
                    return c;
                }
            }
            return null;
        }
    }
}