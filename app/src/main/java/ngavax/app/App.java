//Ngavax

package ngavax.app;

import org.json.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Semaphore;

///////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////  Socket handler  ////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////

class socketListener extends Thread{
    private ServerSocket listener;
    private boolean running = false;

    socketListener(int port) {
        try {
            listener = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopListener(){
        this.running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        this.running = true;
        while (this.running) {
            try {
                Socket socket = listener.accept();
                //LOG.info("New connection from " + socket.getRemoteSocketAddress());

                App.waitOnOtherSockets();
                SocketExchange.setSocket(socket);
                LOG.debug("Notifying worker threads");
                App.notifyWorker();
                App.waitOnOtherSockets();
                App.notifySocket();
                //System.out.println("Wait for worker to finish");
                //

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////  Request Handler  ////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////


class RequestHandler extends Thread{
    private Socket socket;
    private boolean running = false;

    private String HOST = new String();
    private String METHOD = new String();
    private String PATH = new String();
    private String USERAGENT = new String();
    private int PORT = 0;

    RequestHandler(){
        LOG.info("Created thread: " + this.getId());
    }

    private void loadSocket(Socket socket){
        this.socket = socket;
    }

    public void stopWorkers(){
        this.running = false;
        this.interrupt();
    }

    @Override
    public void run()
    {
        try
        {
            this.running = true;
            while (this.running) {
                LOG.debug("Thread " + this.getId() + " is waiting");
                App.waitForSocket();
                loadSocket(SocketExchange.getSocket());
                App.notifySocket();
                //LOG.debug("Request from " + socket.getPort());
                String remoteAddress = socket.getRemoteSocketAddress().toString().split(":")[0].replace("/", "");;
                LOG.info("Request from " + remoteAddress);


                // Read request Headers from client
                BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
                // Character stream for Response Headers to client
                PrintWriter out = new PrintWriter( socket.getOutputStream() );
                // Get binary output stream to client (for requested data)
                BufferedOutputStream dataOut = new BufferedOutputStream(socket.getOutputStream());


                // Echo lines back to the client until the client closes the connection or we receive an empty line
                StringBuilder HEADERS = new StringBuilder();

                String line = in.readLine();
                //out.println("Request Headers:");
                //out.flush();
                while( line != null && line.length() > 0 )
                {
                    HEADERS.append(line).append("\n");
                    line = in.readLine();
                }
                //LOG.debug(HEADERS);

                //byte[] data = HEADERS.toString().getBytes();
                //int bytelength = data.length;

                // Interpret HEADERS
                String[] headerLines = HEADERS.toString().split("\n");
                this.PORT = socket.getLocalPort();
                LOG.debug("Port: " + this.PORT);
                for (String key : headerLines) {
                    //LOG.debug(key);
                    if(key.contains("Host:")){
                        LOG.debug(key);
                        this.HOST = key.split(" ")[1];
                        if(this.HOST.contains(":")){
                            this.HOST = this.HOST.split(":")[0];
                        }
                        this.HOST = this.HOST.toLowerCase();
                    }
                    if(key.contains("GET")){
                        this.METHOD = "GET";
                        this.PATH = key.split(" ")[1];
                        LOG.debug("Method: " + this.METHOD + " Path: " + this.PATH);
                    }
                    else if(key.contains("POST")){
                        this.METHOD = "POST";
                        this.PATH = key.split(" ")[1];
                    }
                    else if(key.contains("PUT")){
                        this.METHOD = "PUT";
                        this.PATH = key.split(" ")[1];
                    }
                    if(key.contains("User-Agent:")){
                        this.USERAGENT = key;
                    }
                }

                // Decision tree for what to do with request

                LOG.debug("Checking validity of request");
                LOG.info(this.METHOD + " " + this.PATH + " " + this.HOST);
                LOG.info(this.USERAGENT);
                JSONObject domain = App.config.validateDomainPort(this.HOST, this.PORT);
                // Check if domain is valid
                if(domain == null){
                    LOG.warn("No domain found for " + this.HOST + ":" + this.PORT);
                    byte[] data = "501 - Not implemented".getBytes();
                    sendResponseHeaders(out, data.length, status.NOT_IMPLEMENTED);
                    dataOut.write(data, 0, data.length);
                    dataOut.flush();
                }else{
                    LOG.debug("Request is valid...");
                    JSONObject dir = App.config.validateDirectory(this.HOST, this.PATH);
                    byte[] data = null;
                    // Check if directory/path is valid
                    if(dir == null){
                        data = "403 - Forbidden".getBytes();
                    }else{
                        // Check if autoindex is enabled
                        if(App.config.validateAutoIndex(dir)){
                            LOG.debug("Autoindex is enabled");
                            data = App.ss.autoFileDir(dir.getString("serve") + this.PATH);
                        }else if(!App.config.validateDirBlock(this.HOST)){
                            switch (dir.getString("type")) {
                                case "static":
                                    LOG.debug("Static file");
                                    LOG.debug(dir);
                                    if(dir.getString("directory").equals(this.PATH))
                                        data = App.ss.getFile(dir.getString("serve"));
                                    else
                                        data = App.ss.getFile(dir.getString("serve") + this.PATH);
                                    break;
                                case "proxy":
                                    LOG.debug("Proxy");
                                    if(dir.getString("directory").equals(this.PATH))
                                        data = App.pp.proxyPass(dir.getString("serve"), App.pp.headerArray(HEADERS.toString(), remoteAddress));
                                    else
                                        data = App.pp.proxyPass(dir.getString("serve") + this.PATH, App.pp.headerArray(HEADERS.toString(), remoteAddress));
                                    break;
                                default:
                                    break;
                            }
                        }else{
                            switch (dir.getString("type")) {
                                case "static":
                                    LOG.debug("Static file");
                                    LOG.debug(dir);
                                    if(dir.getString("directory").equals(this.PATH))
                                        data = App.ss.getFile(dir.getString("serve"));
                                    else
                                        data = "403 - Forbidden".getBytes();
                                    break;
                                case "proxy":
                                    LOG.debug("Proxy");
                                    if(dir.getString("directory").equals(this.PATH))
                                        data = App.pp.proxyPass(dir.getString("serve"), App.pp.headerArray(HEADERS.toString(), remoteAddress));
                                    else
                                        data = "403 - Forbidden".getBytes();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if(data == null){
                        data = "404 - Not Found".getBytes();
                        sendResponseHeaders(out, data.length, status.NOT_FOUND);
                        dataOut.write(data, 0, data.length);
                        dataOut.flush();
                    }else if(data == "403 - Forbidden".getBytes()){
                        sendResponseHeaders(out, data.length, status.FORBIDDEN);
                        dataOut.write(data, 0, data.length);
                        dataOut.flush();
                    }else if(data == "502 - Bad Gateway".getBytes()){
                        sendResponseHeaders(out, data.length, status.BAD_GATEWAY);
                        dataOut.write(data, 0, data.length);
                        dataOut.flush();
                    }else{
                        sendResponseHeaders(out, data.length);
                        dataOut.write(data, 0, data.length);
                        dataOut.flush();
                    }
                }

                // Close our connection
                in.close();
                out.close();
                socket.close();
                LOG.debug( "Connection closed" );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally{
            try{
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private static void sendResponseHeaders(PrintWriter out, int bytelength, String s){
        out.println( "HTTP/1.1 " + s);
        out.println( "Server: NGAVAX" );
        out.println( "Content-Length: " + bytelength );

        out.println(); //blank line to end HEADERS
        out.flush();
    }
    private static void sendResponseHeaders(PrintWriter out, int bytelength){
        sendResponseHeaders(out, bytelength, status.OK);
    }
}


public class App {

    public static parseConfig config;
    public static staticHandler ss = new staticHandler();
    public static proxyHandler pp = new proxyHandler();

///////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////  SEMAPHORES FOR SYNCHRONIZATION OF THREADS  /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////

    static Semaphore workers = new Semaphore(0);
    static Semaphore sockets = new Semaphore(1);

    //static Socket currentSocket = new Socket();

    static void notifyWorker(){
        workers.release();
    }

    static void waitForSocket() throws InterruptedException{
        workers.acquire();
    }

    static void notifySocket(){
        sockets.release();
    }

    static void waitOnOtherSockets() throws InterruptedException{
        sockets.acquire();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static void main(String[] args) throws Exception {
        //The file location is hard coded here
        //Program needs to read the command args and get the file location from there
        LOG.info("Parameters: " + Arrays.toString(args));
        LOG.warn("Amount of threads spawned, is determined by the amount of ports + specified amount of threads");
        LOG.toggleDebug();

        if(args.length == 0){
            LOG.error("No file location provided");
            LOG.warn("Usage: java -jar ngavax.jar <file location>");
            System.exit(-1);
        }
        try {
            FileReader file_config = new FileReader(args[0]);
            JSONObject jsonText = new JSONObject(new JSONTokener(file_config));
            config = new parseConfig(jsonText);

            //Prints the config somewhat prettily
            //config.printConfig();
            LOG.info("Listening on ports: " + (config.getPorts()));

            // Spawn threads to listen on each port
            socketListener[] listen = new socketListener[config.getPorts().length()];
            for(int i = 0; i < config.getPorts().length(); i++){
                listen[i] = new socketListener(config.getPorts().getInt(i));
                listen[i].start();
            }

            LOG.info("Spawning " + ( (config.getWorkerCount() > 0) ? config.getWorkerCount() : 1 ) + " worker threads");
            // Spawn all worker threads
            RequestHandler[] requestHandlers = new RequestHandler[ (config.getWorkerCount() > 0) ? config.getWorkerCount() : 1 ]; //If worker count is 0, spawn 1 thread
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
        } catch (Exception e){
            e.printStackTrace();
            LOG.error("Unknown error");
            System.exit(-1);
        }
    }
}
