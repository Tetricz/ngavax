//Ngavax

package ngavax.app;

import org.json.*;
import java.io.*;
import java.util.*;


public class App {

///////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////  SEMAPHORES FOR SYNCHRONIZATION OF THREADS  /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////

    static Semaphore workers = new Semaphore(0);
    static Semaphore sockets = new Semaphore(0);

    static Socket currentSocket = new Socket();

    static void notifyWorker(){
        workers.release();
    }

    static void waitForSocket() throws InterruptedException{
        workers.acquire();
    }

    static void notifySocket(){
        sockets.release();
    }

    static void waitForWorker() throws InterruptedException{
        sockets.acquire();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static void main(String[] args) throws Exception {
        //The file location is hard coded here
        //Program needs to read the command args and get the file location from there
        LOG.info("Parameters: " + Arrays.toString(args));
        LOG.toggleDebug();

        if(args.length == 0){
            LOG.error("No file location provided");
            LOG.warn("Usage: java -jar ngavax.jar <file location>");
            System.exit(-1);
        }
        try {
            FileReader file_config = new FileReader(args[0]);
            JSONObject jsonText = new JSONObject(new JSONTokener(file_config));
            parseConfig config = new parseConfig(jsonText);

            //Prints the config somewhat prettily
            //config.printConfig();
            LOG.info("Listening on ports: " + (config.getPorts()));

            // Spawn threads to listen on each port
            socketListener[] listen = new socketListener[config.getPorts().length()];
            for(int i = 0; i < config.getPorts().length(); i++){
                listen[i] = new socketListener(config.getPorts().getInt(i));
                listen[i].start();
            }
            System.out.println("Closing connections");
            listen.stopServer();
*/


            // Spawn all worker threads
            LOG.debug("Spawning " + (config.getWorkerCount() - listen.length) + " worker threads");
            RequestHandler[] requestHandlers = new RequestHandler[config.getWorkerCount() - listen.length];
            for(int i = 0; i < requestHandlers.length; i++){
                requestHandlers[i] = new RequestHandler();
                requestHandlers[i].start();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOG.error("Error reading file, check path");
            System.exit(-1);
        } catch (JSONException e) {
            e.printStackTrace();
            LOG.error("Error parsing config file, possible JSON syntax error");
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
