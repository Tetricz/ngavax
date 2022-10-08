package ngavax.app;

public class proxyHandler extends Thread {
    //Spawn a thread and create requests to other servers
    //Once they respond send the response back to the client

    private parseConfig data = null;
    private int test = 0;

    //these are examples for loading data into the thread from the main thread
    public void loadData(parseConfig data) {
        this.data = data;
    }

    public void incTest(int i){
        this.test += i;
    }

    public void run(){
        try {
            // Displaying the thread that is running
            System.out.println("Thread " + Thread.currentThread().getId() + " is running");
            //this.data.printConfig();
            System.out.println("Test: " + this.test);
            sleep(1000);
            System.out.println("Test: " + this.test);
        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
            e.printStackTrace();
        }
    }
}