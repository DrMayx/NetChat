package client;

import java.io.BufferedReader;
import java.io.IOException;

public class DisplayMessage extends Thread {

    private BufferedReader br;

    public DisplayMessage(BufferedReader _br){
        br = _br;
    }

    public void run(){

        while(true){
            try {
                System.out.println(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
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
