package client;

import java.io.PrintWriter;
import java.util.Scanner;

public class SendMessage extends Thread{

    private PrintWriter pw;
    private Scanner scan;

    public SendMessage(PrintWriter _pw, Scanner _scan){
        pw=_pw;
        scan=_scan;
    }

    public void run(){
        String input;
        while(true){
            input = scan.nextLine();
            if(input.equalsIgnoreCase("exit")){
                System.exit(901);
            }
            pw.write(input);
        }
    }
}
