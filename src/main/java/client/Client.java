package client;

import listeners.DisplayMessageClient;
import listeners.SendMessageClient;

import java.net.Socket;

/**
 * Created by Maciej Jankowicz on 27.06.18, 13:15
 * Contact: mj6367@gmail.com
 */
public class Client {

    private DisplayMessageClient display;
    private SendMessageClient send;
    private Socket clientSocket;

    public Client(Socket _clientSocket, DisplayMessageClient _display, SendMessageClient _send) {
        this.clientSocket = _clientSocket;
        this.display = _display;
        this.send = _send;
    }

    public DisplayMessageClient getDisplay() {
        return display;
    }

    public SendMessageClient getSend() {
        return send;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void startClient(){
        this.display.start();
        this.send.start();
    }
}
