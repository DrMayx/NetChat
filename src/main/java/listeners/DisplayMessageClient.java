package listeners;

import util.Message;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class DisplayMessageClient extends Thread {

    private ObjectInputStream br;
    String clientName;

    public DisplayMessageClient(ObjectInputStream _br, String name){
        br = _br;
        clientName = name;
    }

    public void run(){
        try{
            Message message;
            while(!isInterrupted()){
                message = (Message) br.readObject();
                if(!message.getAuthor().equals(clientName)){
                    System.out.println(message);
                }
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
