package ngavax.app;

import java.net.Socket;

public class SocketExchange {
    private static Socket currentSocket = new Socket();

    public static synchronized Socket getSocket() {
        return currentSocket;
    }

    public static synchronized void setSocket(Socket socket) {
        currentSocket = socket;
    }
}
