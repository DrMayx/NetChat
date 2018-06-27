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
        while(!isInterrupted()){
            if((input = scan.nextLine()) != null) {
                if (input.equalsIgnoreCase("exit")) {
                    System.exit(901);
                }
                System.out.println(input);
                pw.write(input);
            }
        }
    }
}
