package ngavax.app;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProxyTests {

    @Test void proxyTest() throws IOException, InterruptedException{
        proxyHandler pp = new proxyHandler();
        String headers = """
GET / HTTP/1.0
Host: test1.david-windows.localdomain
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:107.0) Gecko/20100101 Firefox/107.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
Upgrade-Insecure-Requests: 1
Sec-Fetch-Dest: document
Sec-Fetch-Mode: navigate
Sec-Fetch-Site: none
Sec-Fetch-User: ?1
Pragma: no-cache
Cache-Control: no-cache
""";
        byte[] test = pp.proxyPass("http://localhost:8080", pp.headerArray(headers, "10.0.0.146"));
        LOG.info(new String(test));
    }

    @Test void headerArrayTest(){
        proxyHandler pp = new proxyHandler();
        String headers = """
GET / HTTP/1.0
Host: test1.david-windows.localdomain
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:107.0) Gecko/20100101 Firefox/107.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8
Accept-Language: en-US,en;q=0.5
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
Upgrade-Insecure-Requests: 1
Sec-Fetch-Dest: document
Sec-Fetch-Mode: navigate
Sec-Fetch-Site: none
Sec-Fetch-User: ?1
Pragma: no-cache
Cache-Control: no-cache
""";
        ArrayList<String> test = pp.headerArray(headers, "10.0.0.146");
        LOG.info(test.toString());
    }

}
