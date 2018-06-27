package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

public class DisplayMessage extends Thread {

    private BufferedReader br;

    public DisplayMessage(BufferedReader _br){
        br = _br;
    }

    public void run(){

        while(!this.isInterrupted()){
            try {
                System.out.println("----1");
                System.out.println(br.readLine());
                System.out.println("----2");
            } catch (IOException e) {
                e.printStackTrace();
                this.interrupt();
                try {
                    br.close();
                }catch(IOException er){
                    System.err.println("Cannot close buffered rider");
                    this.interrupt();
                }
            }
        }
    }

    public void close(){

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
