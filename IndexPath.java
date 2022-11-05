import java.io.File;
import java.io.IOException;
import java.net.*;

public class IndexPath {
   public static void main(String[] args) throws Exception {
   // public String listFiles(String pathName) {
      String pathName = "c:\\Users\\ameri\\source\\repos\\TestingJava\\Testing";
      File dir = new File(pathName);
      //String for html
      String htmlIndex = "<html>\n<head><title>Index</title></head>\n<body>\n<h1>Index</h1><hr><pre>";
      htmlIndex += "<a href=\"" + "../" + "\">../</a>";

      //List of all files and directories
      String contents[] = dir.list();
      String htmlStr;
      for(int i=0; i<contents.length; i++) {
         htmlStr = contents[i];
         htmlIndex += "\n<a href=\"" + htmlStr + "\">" + htmlStr + "</a>";
      }

      htmlIndex += "\n</pre><hr></body>\n</html>";

      // return htmlIndex;
      System.out.println(htmlIndex);
   } 
}
