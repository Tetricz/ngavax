package ngavax.app;

public class workerThread extends Thread {
    public void run() {
        System.out.println("Worker thread running");
        //call proxyhandler or static handler

        //send response to client
        //Close thread
        System.out.println("Worker thread closing");
    }
}