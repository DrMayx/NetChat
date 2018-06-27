package listeners;

import client.Client;
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
                sendMessageToAll("SERVER CLOSING IN 5 SECONDS!");
                try {
                    this.sleep(5000);
                }catch (InterruptedException e){
                    // do nothing... just wait till end
                }
                isRunning = false;
            }else if(input.startsWith("say")){
                sendMessageToAll(input.replaceFirst("say", ""));
            }
        }
        System.exit(0);
    }

    public void sendMessageToAll(String message){
        clients.forEach(client -> {
            try {
                client.writeObject(new Message(message, "SERVER"));
            }catch(SocketException e){
                //do nothin
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
