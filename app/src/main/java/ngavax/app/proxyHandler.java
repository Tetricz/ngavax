package ngavax.app;

import java.net.*;
import java.io.*;
import org.json.*;

public class proxyHandler {

  public void getHTML(String URL) {
    String output = getUrlContents(URL);
    System.out.println(output);
  }

  public String modifyHeader(JSONObject settings, String modifiedheader) {
    //This function will modify the header to match the config file
    //This will be done by reading the config file and then modifying the header
    //The config file will have a list of domains and their corresponding IP addresses
    //The header will be modified to match the IP address of the domain
    //This will be done by reading the header and then replacing the domain with the IP address
    //The header will then be returned to the caller
    JSONObject settings = new JSONObject("{\"serve\":\"http://localserver.tld:80\",\"type\":\"proxy\",\"directory\":\"/\"}"); //slice this up
    String HEADERS = "GET / HTTP/3\nHost: www.tetricz.com\nUser-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:105.0) Gecko/201001\nAccept: text/css,*/*;q=0.1\nAccept-Language: en-US,en;q=0.5\nAccept-Encoding: gzip, deflate, br\nAlt-Used: www.tetricz.com\nConnection: keep-alive\nCookie: _ga=GA1.2.582144703.1664840968\nSec-Fetch-Dest: style\nSec-Fetch-Mode: no-cors\nSec-Fetch-Site: same-origin\nPragma: no-cache\nCache-Control: no-cache\nTE: trailers";

    String modifiedheader = ""; //logic to modify header

    String newHost = settings.getString(key:"serve");
    String newDirectory = settings.getString(key:"directory")

    return modifiedheader;
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