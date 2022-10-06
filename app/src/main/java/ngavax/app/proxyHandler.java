package ngavax.app;


public class proxyHandler {
    public static void main(String[] args) throws Exception {
        try {
            String Proxy_Host = "Our Proxy Server";
            int Remote_Port = 1025;
            int Local_Port = 1026;
            // Printing the start-up message
            System.out.println("Starting proxy for " + Proxy_Host + ":" + Remote_Port
                + " on port " + Local_Port);
            // start the server
            Run_Server(Proxy_Host, Remote_Port, Local_Port);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }


    public static void Run_Server(String Proxy_Host, int Remote_Port, int Local_Port) throws Exception {
        // Create a ServerSocket to listen connections
        ServerSocket Server_Socket = new ServerSocket(Local_Port);
        final byte[] Request = new byte[1024];
        byte[] Reply = new byte[4096];
        while (true) {
            Socket Socket_Client = null, Socket_Server = null;
            try {
                // wait for a connection on the local port
                Socket_Client = Server_Socket.accept();
                final InputStream InputStreamClient = Socket_Client.getInputStream();
                final OutputStream OutputStreamClient = Socket_Client.getOutputStream();

                // Create the connection to the real server.
            try {
                Socket_Server = new Socket(Proxy_Host, Remote_Port);
            }
            catch (Exception e) {
                PrintWriter out = new PrintWriter(OutputStreamClient);
                out.print("The Proxy Server could not connect to " + Proxy_Host + ":"
                 + Remote_Port + ":\n" + e + "\n");
                out.flush();
                Socket_Client.close();
                continue;
            }


            final InputStream InputStreamServer = Socket_Server.getInputStream();
            final OutputStream OutputStreamServer = Socket_Server.getOutputStream();

            // The thread to read the client's requests and to pass them
            Thread New_Thread = new Thread() {
            public void run() {
                int Bytes_Read;
                try {
                    while ((Bytes_Read = InputStreamClient.read(Request)) != -1) {
                        OutputStreamServer.write(Request, 0, Bytes_Read);
                        OutputStreamServer.flush();
                    }
                }
                catch (Exception e) {
                }

                // Close the connections
                try {
                    OutputStreamServer.close();
                }
                catch (Exception e) {
                }
            }
        };

            // client-to-server request thread
            New_Thread.start();
            // Read server's responses and pass them to the client.
            int Bytes_Read;
            try {
                while ((Bytes_Read = InputStreamServer.read(Reply)) != -1) {
                    OutputStreamClient.write(Reply, 0, Bytes_Read);
                    OutputStreamClient.flush();
                }
            }
            catch (Exception e) {
            }
            // Close the connection
            OutputStreamClient.close();
           }
           catch (Exception e) {
               System.err.println(e);
           }
           finally {
               try {
                   if (Socket_Server != null)
                       Socket_Server.close();
                   if (Socket_Client != null)
                       Socket_Client.close();
               }
               catch (Exception e) {
               }
           }
       }
   }
}
