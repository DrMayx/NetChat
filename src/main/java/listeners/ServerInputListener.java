package listeners;

import client.Client;
import server.ServerController;
import util.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Maciej Jankowicz on 27.06.18, 15:28
 * Contact: mj6367@gmail.com
 */
public class ServerInputListener extends Thread{

    private Scanner scanner;
    private ArrayList<Client> clients;

    public ServerInputListener(Scanner scan, ArrayList<Client> clients){
        this.scanner = scan;
        this.clients = clients;
    }

    public void run(){
        String input;
        boolean isRunning = true;
        while(isRunning){
            input = scanner.nextLine();
            if(input.equalsIgnoreCase("exit")){
                System.out.println("Quitting in 5 seconds");
                sendMessageToAll("SERVER CLOSING IN 5 SECONDS!");
                try {
                    for(int i = 4; i >=0; i--){
                        this.sleep(996);
                        System.out.println("exit in " + i);
                    }
                }catch (InterruptedException e){
                    // do nothing... just wait till end
                }
                isRunning = false;
            }else if(input.startsWith("say")){
                sendMessageToAll(input.replaceFirst("say", ""));
            }else if(input.equalsIgnoreCase("!users")){
                System.out.println("Currently logged users: " + ServerController.irhs.size());
                int i = 0;
                for(Client c : ServerController.clientsList){
                    System.out.println(i++ + ". " + c.name);
                }
            }
        }
        System.exit(0);
    }

    public void sendMessageToAll(String message){
        clients.forEach(client -> {
            try {
                client.writeObject(new Message("\u001B[31m" + message + "\u001B[0m","\u001B[31mSERVER\u001B[0m"));
            }catch(SocketException e){
                //do nothin
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
