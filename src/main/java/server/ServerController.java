package server;

import client.DisplayMessage;
import client.SendMessage;
import listeners.NewClientsListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        new DisplayMessage(in).start();
        new SendMessage(out, scanner).start();








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