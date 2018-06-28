package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Maciej Jankowicz on 27.06.18, 13:15
 * Contact: mj6367@gmail.com
 */
public class Client {

    private ObjectOutputStream out;
    public String name;
    public int id;

    public Client(ObjectOutputStream out, int id) {
        this.out = out;
        this.id = id;
        this.name = "NEW USER";
    }

    public void writeObject(Object object) throws IOException{
        this.out.writeObject(object);
    }

    public void closeStream()throws IOException{
        this.out.close();
    }
}
