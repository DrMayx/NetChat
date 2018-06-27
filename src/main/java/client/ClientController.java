package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            socket  = new Socket(this._hostName, this._portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new DisplayMessage(in).start();
            new SendMessage(out, scan).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
