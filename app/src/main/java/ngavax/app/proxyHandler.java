package ngavax.app;

import java.net.*;
import java.util.HashMap;
import java.io.*;
import org.json.*;

public class proxyHandler {

  public void getHTML(String URL) {
    String output = getUrlContents(URL);
    System.out.println(output);
  }

  public HashMap<String, String> modifyHeader(JSONObject settings, String HEADERS) {
    //HEADERS = "GET / HTTP/3\nHost: www.tetricz.com\nUser-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:105.0) Gecko/201001\nAccept: text/css,*/*;q=0.1\nAccept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate, br\nAlt-Used: www.tetricz.com\nConnection: keep-alive\nCookie: _ga=GA1.2.582144703.1664840968\nSec-Fetch-Dest: style\nSec-Fetch-Mode: no-cors\nSec-Fetch-Site: same-origin\nPragma: no-cache\nCache-Control: no-cache\nTE: trailers";
    
    String[] arrofHEADERS = HEADERS.split("\n");

    HashMap<String, String> modheaders = new HashMap<String, String>();

    modheaders.put(null, arrofHEADERS[0]);
    for (int i = 1; i < arrofHEADERS.length; i++) {
      String[] header = arrofHEADERS[i].split(": ");
      modheaders.put(header[0], header[1]);
    }

    modheaders.remove("Host");
    modheaders.put("Host", settings.getString("serve"));

    modheaders.remove(null);
    modheaders.put(null, "GET " + settings.getString("directory"));

    modheaders.remove("Alt-Used");
    modheaders.put("Alt-Used", settings.getString("serve"));

    return modheaders;
  }

  

  public String getUrlContents(String theUrl) {
    StringBuilder content = new StringBuilder();
    // Use try and catch to avoid the exceptions
    try {
      URL url = new URL(theUrl); // creating a url object
      URLConnection urlConnection = url.openConnection(); // creating a urlconnection object

      // wrapping the urlconnection in a bufferedreader
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
      String line;
      // reading from the urlconnection using the bufferedreader
      while ((line = bufferedReader.readLine()) != null) {
        content.append(line + "\n");
      }
      bufferedReader.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    return content.toString();
  }
}