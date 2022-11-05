//Ngavax

package ngavax.app;

import org.json.*;
import java.io.*;
import java.util.*;


public class App {
    public static void main(String[] args) throws Exception {
        //The file location is hard coded here
        //Program needs to read the command args and get the file location from there
        //print out args
        System.out.println("Parameters: " + Arrays.toString(args));
        if(args.length == 0){
            System.out.println("No file location provided \nUsage: java -jar ngavax.jar <file location>");
            System.exit(-1);
        }
        try {
            FileReader file_config = new FileReader(args[0]);
            JSONObject jsonText = new JSONObject(new JSONTokener(file_config));
            parseConfig config = new parseConfig(jsonText);

            //Prints the config somewhat prettily
            //config.printConfig();
            System.out.println((config.getPorts()));

            //JSONObject directory = config.validateDirectory("uhhh.edu", "/");
            //System.out.println(config.getType(directory));
            //System.out.println(config.getServe(directory));
/*
            socketListener listen = new socketListener(180);
            listen.startServer();
            System.out.println("Waiting for connections");
            while(listen.isAlive()){
                Thread.sleep(1000);
            }
            System.out.println("Closing connections");
            listen.stopServer();
*/
        requestUndertow listen = new requestUndertow(8080, config);
        listen.start();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error reading file, check path");
            System.exit(-1);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error parsing config file, possible JSON syntax error");
            System.exit(-1);
        }

        //https://www.geeksforgeeks.org/java-util-concurrent-package/
        //https://www.geeksforgeeks.org/multithreading-in-java/

        //proxyHander proxy = new proxyHander();
        //proxy.start();

        //staticHandler staticFiles = new staticHandler();
        //staticFiles.start();

    //This thread will become the request handler, thus no need to spawn another thread

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
