package ngavax.app;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;

public class requestUndertow {
    Undertow server;
    staticHandler staticFiles = new staticHandler(".");

    public requestUndertow(int port) {
        this.server = Undertow.builder()
                .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                .addHttpListener(port, "0.0.0.0")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        HeaderMap HEADERS = exchange.getRequestHeaders();
                        //print HeaderMap
                        System.out.println("Request at " + HEADERS.get("Host") + " for " + exchange.getRelativePath());
                        String path = exchange.getRelativePath();
                        System.out.println("Path: " + path);

                        //pathing logic and request forwarding logic goes here

                        exchange.getResponseHeaders().put(Headers.SERVER, "Undertow");
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html; charset=UTF-8");
                        exchange.getResponseSender().send(staticFiles.indexPath(path));
                    }
                }).build();
    }

    private String getContent(){
        return "Hello World";
    }

    public void start(){
        this.server.start();
    }

    public void stop(){
        this.server.stop();
    }
}
