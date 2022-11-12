package ngavax.app;

import org.json.*;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class requestUndertow {
    private Undertow server;
    private parseConfig config;
    private staticHandler staticFiles;
    private proxyHandler proxy = new proxyHandler();

    public requestUndertow(int port, parseConfig config) {
        this.config = config;
        this.staticFiles = new staticHandler(this.config.getRoot());
        this.server = Undertow.builder()
                .addHttpListener(port, "0.0.0.0")
                .setWorkerThreads(config.getWorkerCount())
                .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        HeaderMap HEADERS = exchange.getRequestHeaders();
                        //print HeaderMap
                        //System.out.println("Request " + HEADERS.get("Host"));
                        String path = exchange.getRelativePath();
                        System.out.println("Path: " + path);

                        //pathing logic and request forwarding logic goes here

                        exchange.getResponseHeaders().put(Headers.SERVER, "Undertow");
                        //exchange.getResponseSender().send(staticFiles.indexPath(path));

                        System.out.println("Checking validity");
                        String hostname = HEADERS.get("Host").toString().substring(1,
                                                    HEADERS.get("Host").toString().length() - 1);
                        JSONObject domain = config.validateDomainPort(hostname, port);
                        System.out.println("directory");
                        if(domain != null){
                            System.out.println("Domain is valid... retrieving services on " + path);
                            JSONObject directory = config.validateDirectory(hostname, path);
                            if(directory != null){
                                String type = config.getType(directory);
                                //System.out.println("Type: " + type);
                                System.out.println("Directory is valid... retrieving services");
                                if(type.equals("static")){
                                    System.out.println("Serving static files");
                                    String serve = config.getServe(directory);
                                    if(config.validateAutoIndex(directory)){
                                        exchange.getResponseSender().send(staticFiles.indexPath(serve));
                                    }else{
                                        exchange.getResponseSender().send(staticFiles.getFile(serve));
                                    }
                                } else if(type.equals("proxy")){
                                    System.out.println("Proxying request");
                                    String serve = config.getServe(directory);
                                    String res = proxy.getUrlContents(serve + path);
                                    exchange.getResponseSender().send(res);
                                } else {
                                    System.out.println("Invalid type");
                                    exchange.setStatusCode(StatusCodes.NOT_FOUND);
                                    exchange.getResponseSender().send("Error 404: Invalid type");
                                }
                            }else{
                                System.out.println("Invalid directory");
                                boolean default_location = config.validateDirBlock(hostname);
                                if(!default_location){
                                    JSONObject default_dir = config.getDef(hostname);
                                    String type = config.getType(default_dir);
                                    System.out.println("Serving default location");
                                    if(type.equals("static")){
                                        String serve = default_dir.getString("serve");
                                        //String serve = config.getServeDefault(hostname);
                                        if(config.validateAutoIndex(default_dir)){
                                            //TODO: check whether it's a file or directory, then serve the item
                                            exchange.getResponseSender().send(staticFiles.indexPath(serve));
                                        }else{
                                            //check if path is "/", if so, serve index.html, else, try to serve path
                                            //if path is non existent, serve 404
                                            if(path.equals("/")){
                                                exchange.getResponseSender().send(staticFiles.getFile(serve + "/index.html"));
                                            } else {
                                                exchange.getResponseSender().send(staticFiles.getFile(serve + path));
                                            }
                                        }
                                    } else if(type.equals("proxy")){
                                        //Just check the dirblock, if it's true, then block any non specified request
                                        //else, just pass the extra path to the proxy
                                    } else {
                                        System.out.println("Invalid type");
                                        exchange.setStatusCode(StatusCodes.NOT_FOUND);
                                        exchange.getResponseSender().send("Error 404: Invalid type");
                                    }

                                } else {
                                    System.out.println("Serving 404");
                                    exchange.setStatusCode(StatusCodes.NOT_FOUND);
                                    exchange.getResponseSender().send("Error 404: Invalid directory");
                                }

                            }
                        }else{
                            System.out.println("Domain is not valid... return error page/code");
                            exchange.setStatusCode(StatusCodes.SERVICE_UNAVAILABLE);
                            exchange.getResponseSender().send("Domain is not valid");
                        }

                    }
                }).build();
    }

    public void start(){
        this.server.start();
    }

    public void stop(){
        this.server.stop();
    }
}
