package client;

import listeners.DisplayMessageClient;
import listeners.SendMessageClient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {

    private String _hostName;
    private String _clientName;
    private int _portNumber;
    private Scanner scan;
    private Socket socket;

    public ClientController(String hostName, int port){
        this._portNumber = port;
        this._hostName = hostName;
        this.scan = new Scanner(System.in);
    }

    public void run(){
        System.out.print("Enter Your nick: ");
        String nick = scan.nextLine();
        this._clientName = nick;

        try {
            socket = new Socket(this._hostName, this._portNumber);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            new DisplayMessageClient(in).start();
            new SendMessageClient(out, scan, this._clientName).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
