package listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class DisplayMessageServer extends Thread {

    private ObjectInputStream br;

    public DisplayMessageServer(ObjectInputStream _br){
        br = _br;
    }

    public void run(){
        try{
            while(!isInterrupted()){
                System.out.println(br.readObject());
            }
        }
        catch(IOException | ClassNotFoundException e){
            System.err.println("Error reading Object!");
            e.printStackTrace();
            this.interrupt();
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
