package listeners;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class NewClientsListener extends Thread{

    private ServerSocket _serverSocket;
    private List<Socket> _usersList;

    public NewClientsListener(ServerSocket serverSocket, List<Socket> usersList){
        this._serverSocket = serverSocket;
        this._usersList = usersList;
    }

    public void start(){
        while(true){
            try {
                this._usersList.add(this._serverSocket.accept());
            }
            catch(IOException e){
                System.out.println("Exception caught when listening on port: " + this._serverSocket.getLocalPort() +
                        ". For more details check log file.");
            }
        }
    }
}
