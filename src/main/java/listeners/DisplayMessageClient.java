package listeners;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class DisplayMessageClient extends Thread {

    private ObjectInputStream br;

    public DisplayMessageClient(ObjectInputStream _br){
        br = _br;
    }

    public void run(){
        try{
            while(!isInterrupted()){
                System.out.println(br.readObject());
            }
        }catch(EOFException eof){
            System.out.println("Host disconnected. Bye");
            try {
                this.sleep(1000);
            }catch(InterruptedException ie){
                ///noting
            }
            System.exit(0);
        }catch(IOException | ClassNotFoundException e){
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
