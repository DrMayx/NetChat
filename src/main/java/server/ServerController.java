package server;

import client.Client;
import listeners.ServerInputListener;
import util.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.util.Scanner;

public class ServerController {
    private int portNumber;
    private ServerSocket _serverSocket;
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

            new ServerInputListener(scanner, ServerController._clientsList).start();

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

        public IncomingRequestHandler(Socket socket) {

            this.socket = socket;

        }

        @Override
        public void run() {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                Client thisClient = new Client(out);
                synchronized (_clientsList) {
                    _clientsList.add(thisClient);
                }

                while (true) {
                    Message message = (Message) in.readObject();
                    if(thisClient.name.equals("NEW USER")){
                        out.writeObject(setClientName(thisClient, message));
                        continue;
                    }
                    _clientsList.forEach(client -> {
                        try {
                            if(message.getContent().startsWith("@")){
                                String target = message.getContent().split(" ")[0].replace("@", "");
                                String content = message.getContent().replaceFirst("@"+target, "");
                                client.writeObject("Direct Message to " + target + " >> " + content);
                            }
                            else {
                                client.writeObject(message);
                            }
                            if(!message.getAuthor().equals("SERVER")) {
                                System.out.println(message);
                            }
                        } catch (IOException e) {
                            //e.printStackTrace();
                            System.out.println("Error with sending message to client!");
                            return;
                        }
                    });
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
    }
}