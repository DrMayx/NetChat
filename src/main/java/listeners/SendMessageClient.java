package listeners;

import util.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class SendMessageClient extends Thread{

    private ObjectOutputStream pw;
    private Scanner scan;
    private String name;

    public SendMessageClient(ObjectOutputStream _oos, Scanner _scan, String _name){
        pw=_oos;
        scan=_scan;
        name = _name;
    }

    public void run(){
        String input;
        try{
            while(!isInterrupted()){
                if((input = scan.nextLine()) != null) {
                    if (input.equalsIgnoreCase("exit")) {
                        System.exit(901);
                    }
                    pw.writeObject(new Message(input, name));
                }
            }
        }catch (IOException e){
            System.err.println("Message couldn't be sent!");
            e.printStackTrace();
            this.interrupt();
        }

    }
}
