package ngavax.app;

import java.net.*;
import java.io.*;

public class socketListener extends Thread {
    private ServerSocket listener;
    private int port;
    private boolean running = false;

    public socketListener(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            this.listener = new ServerSocket(port);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        this.running = false;
        this.interrupt();
    }

    @Override
    public void run(){
        this.running = true;
        while(this.running){
            try{
                System.out.println("Listening for a connection on port " + this.port);
                Socket socket = this.listener.accept();
                RequestHandler requestHandler = new RequestHandler(socket);
                requestHandler.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}