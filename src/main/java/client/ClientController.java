package client;

import listeners.DisplayMessageClient;
import listeners.SendMessageClient;
import util.Message;

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
        this._clientName = scan.nextLine();

        try {
            socket = new Socket(this._hostName, this._portNumber);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(new Message(this._clientName, this._clientName));
            try {
                while (! in.readObject().equals("true")) {
                    System.out.print("Nick already exists! Enter new one: ");
                    this._clientName = scan.nextLine();
                    System.out.println();
                    out.writeObject(new Message(this._clientName, this._clientName));
                }
            }catch (ClassNotFoundException e){
                System.out.println("Class not found");
            }
            new DisplayMessageClient(in, this._clientName).start();
            new SendMessageClient(out, scan, this._clientName).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
