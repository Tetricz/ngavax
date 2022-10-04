//Ngavax

package ngavax.app;

import static ngavax.app.MessageUtils.*;

public class App {
    public static void main(String[] args) {
        System.out.println(getMessage());
        //System.out.println(getOtherMessage());

        //The file location is hard coded here
        //Program needs to read the command args and get the file location from there
        parseConfig config = new parseConfig("..\\config_example.json");

        //Prints the config somewhat prettily
        //config.printConfig();


        //https://www.geeksforgeeks.org/java-util-concurrent-package/
        //https://www.geeksforgeeks.org/multithreading-in-java/
        //Start the proxy thread
        //proxyThread proxy = new proxyThread(data);
        //proxy.start();
        proxyHander proxy = new proxyHander();
        proxy.loadData(config);
        proxy.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        proxy.incTest(5);

        //Start the static thread
        //staticThread static = new staticThread(data);
        //static.start();
        staticHandler staticFiles = new staticHandler();
        staticFiles.start();

        



    
    //This thread will become the request handler, thus no need to spawn another thread
        //This is the request handler
        //HEADERS are important
        //When a client makes a request, the server will recieve a request, in teh request there will be HEADERS
        //These headers will have a Host: field, this is what we will read to determine where the request gets passed to
        //There is also a GET/POST/PUT/DELETE field, this is the directory field that you see in a URL.

        //Example:
        //GET /api HTTP/1.1
        //Host: example.com
        //There are also user-agents and other fun things in the HEADERS, but we will ignore those for the most part

        //When looking at the Host, we should start at the back and work our way forward
        //First match the .com, then the .example, then the www
        //If at any point the domain is not correct, we should return a 404 error
    }
}