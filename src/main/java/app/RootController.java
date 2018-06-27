package app;

import client.ClientController;
import server.ServerController;

import java.io.IOException;

public class RootController {

    public static void main(String[] args) {

        try{
            switch(args[0]){
                case "server":
                    System.out.println("Logged as SERVER at port: " + args[1]);
                    // Launch server and start listening for new clients
                    new ServerController(Integer.parseInt(args[1])).run();
                    break;
                case "client":
                    System.out.println("Logged as client to: " + args[1] + " at port: " + args[2]);
                    // Launch client if server is working and start listening for output and input. If server is not working print message and exit the program.
                    new ClientController(args[1], Integer.parseInt(args[2])).run();
                    break;
                default:
                    System.out.println("Invalid mode!");
                    break;
            }
        }catch(IndexOutOfBoundsException e){
            System.out.println("Please enter correct mode : server OR client [host ip/name] [port]");
        }catch(IOException e){
            e.printStackTrace();
            System.exit(901);
        }
    }
}
