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
    public static final ArrayList<Client> clientsList = new ArrayList<>();
    public static ArrayList<IncomingRequestHandler> irhs = new ArrayList<>();

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

            serverChat = new ServerInputListener(scanner, ServerController.clientsList);
            serverChat.start();

            while (true) {
                Socket client = this._serverSocket.accept();
                System.out.println("User connected at " + client.getLocalAddress() + ":" + client.getPort());

                IncomingRequestHandler irh = new IncomingRequestHandler(client);
                irhs.add(irh);
                irh.start();
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
                synchronized (clientsList) {
                    clientsList.add(thisClient);
                }

                while (true) {

                    Message message = checkForUserLeft(in);
                    if(message == null){
                        return;
                    }
                    if(thisClient.name.equals("NEW USER")){
                        out.writeObject(setClientName(thisClient, message));
                        continue;
                    }
                    if(message.getContent().startsWith("@")){
                        sendPrivateMessage(message);
                    } else if(!message.getContent().startsWith(".")){
                        sendNormalMessage(message);
                    }
                    if(message.getAuthor().equals("SERVER")){
                        continue;
                    }
                    System.out.println(message);
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
            for(Client c : clientsList){
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
            for(Client c : clientsList){
                if(c.name.equals(name)){
                    return c;
                }
            }
            return null;
        }

        private void sendPrivateMessage(Message message){
            String target = message.getContent().split(" ")[0].replaceFirst("@","");
            String content = message.getContent().replaceFirst("@" + target, "");
            clientsList.forEach(client -> {
                try{
                    if(client.name.equals(target)){
                        client.writeObject(new Message(content, "PRIVATE::" + message.getAuthor()));
                    }
                }catch(IOException e){
                    System.out.println("error sending!");
                }
            });
        }

        private void sendNormalMessage(Message message){
            clientsList.forEach(client -> {
                try {
                    client.writeObject(message);
                } catch (IOException e) {
                    System.out.println("Error with sending message to client!");
                    System.out.println(e.getMessage());
                    return;
                }
            });
        }

        private Message checkForUserLeft(ObjectInputStream in) throws ClassNotFoundException, IOException{
            Message input = new Message(".exit!", "SERVER");
            try {
                input = (Message) in.readObject();
                if(input.getContent().equals(".exit!")){
                    throw new SocketException();
                }
            }catch(SocketException userOut){
                String author = input.getAuthor();

                if(input.getAuthor().equals("SERVER")){
                    return null;
                }
                Client user = findUser(author);
                user.closeStream();
                clientsList.remove(user);
                user = null;
            }catch(EOFException ex){
                String message = thisClient.name + " left.";
                System.out.println(message);
                serverChat.sendMessageToAll(message);
                this.socket.close();
                this.interrupt();
                irhs.remove(this);
                clientsList.remove(thisClient);
                return null;
            }
            return input;
        }
    }
}