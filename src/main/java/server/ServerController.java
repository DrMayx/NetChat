package server;


import listeners.DisplayMessageServer;
import listeners.SendMessageServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ServerController {
    private int portNumber;
    private ServerSocket _serverSocket;
    private Map<String, Socket> _clientsList;

    public ServerController(int port){
        this.portNumber = port;
        this._clientsList = new HashMap<>();
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
        Socket clientSocket = this._serverSocket.accept();

        System.out.println(clientSocket.getLocalAddress() + " : " + clientSocket.getPort());

        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

        new DisplayMessageServer(in).start();
        new SendMessageServer(out, scanner, "SERVER").start();








        //new NewClientsListener(this._serverSocket, this._clientsList);
//        while(true){
//            if(scanner.nextLine().equalsIgnoreCase("exit")){
//                for(String client : this._clientsList.keySet()){
//                    this._clientsList.get(client).close();
//                }
//                this._serverSocket.close();
//                System.exit(900);
//            }
//        }
    }
}