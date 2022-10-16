package ngavax.app;

import org.json.*;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProxyTests {
    @Test void testModifyHeaders(){
        String HEADERS = "GET / HTTP/3\nHost: www.tetricz.com\nUser-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:105.0) Gecko/201001\nAccept: text/css,*/*;q=0.1\nAccept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate, br\nAlt-Used: www.tetricz.com\nConnection: keep-alive\nCookie: _ga=GA1.2.582144703.1664840968\nSec-Fetch-Dest: style\nSec-Fetch-Mode: no-cors\nSec-Fetch-Site: same-origin\nPragma: no-cache\nCache-Control: no-cache\nTE: trailers";
        JSONObject settings = new JSONObject("{\"serve\":\"localserver.tld:80\",\"type\":\"proxy\",\"directory\":\"/\"}");
        proxyHandler proxy = new proxyHandler();

        HashMap<String, String> expectedHeaders = new HashMap<String, String>();
        expectedHeaders.put("Host", "localserver.tld:80");
        expectedHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:105.0) Gecko/201001");
        expectedHeaders.put("Accept", "text/css,*/*;q=0.1");
        expectedHeaders.put("Accept-Language", "en-US,en;q=0.5");
        expectedHeaders.put("Accept-Encoding", "gzip, deflate, br");
        expectedHeaders.put("Alt-Used", "localserver.tld:80");
        expectedHeaders.put("Connection", "keep-alive");
        expectedHeaders.put("Cookie", "_ga=GA1.2.582144703.1664840968");
        expectedHeaders.put("Sec-Fetch-Dest", "style");
        expectedHeaders.put("Sec-Fetch-Mode", "no-cors");
        expectedHeaders.put("Sec-Fetch-Site", "same-origin");
        expectedHeaders.put("Pragma", "no-cache");
        expectedHeaders.put("Cache-Control", "no-cache");
        expectedHeaders.put("TE", "trailers");
        expectedHeaders.put(null, "GET /");

        assertEquals(expectedHeaders, proxy.modifyHeader(settings, HEADERS));
    }
}