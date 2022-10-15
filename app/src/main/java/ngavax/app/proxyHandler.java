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
    String modifiedheader = ProxyTests.test1; //test example we have

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