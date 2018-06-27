package listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class NewClientsListener extends Thread{

    private ServerSocket _serverSocket;
    private Map<String, Socket> _usersList;

    public NewClientsListener(ServerSocket serverSocket, Map<String, Socket> usersList){
        this._serverSocket = serverSocket;
        this._usersList = usersList;
    }

    public void start(){
        while(true){
            Socket client;
            BufferedReader name;
            try {
                client = this._serverSocket.accept();
                name = new BufferedReader(new InputStreamReader(client.getInputStream()));

                this._usersList.put(name.readLine(), client);
            }
            catch(IOException e){
                System.out.println("Exception caught when listening on port: " + this._serverSocket.getLocalPort() +
                        ". For more details check log file.");
            }
        }
    }
}
