package ngavax.app;

import java.io.File;
import java.io.IOException;

public class staticHandler {

    private String root_path;

    public staticHandler(String root) {
        this.root_path = root;
    }

    public String indexPath(String path){
        String href = path;
        path = this.root_path + path;

        System.out.println(path);
        File dir = new File(path);
        //String for html
        String htmlIndex = "<html>\n<head><title>Index</title></head>\n<body>\n<h1>Index</h1><hr><pre>\n<a href=\"" + "../" + "\">../</a>\n";

        //List of all files and directories
        String contents[] = dir.list();
        String htmlFil = "";
        String htmlDir = "";
        for(int i=0; i < contents.length; i++) {
            if(new File(path + contents[i]).isDirectory()){
                htmlDir += "<a href=\"" + href + contents[i] + "/\">" + contents[i] + "</a>\n";
            } else {
                htmlFil += "<a href=\"" + href + contents[i] + "\">" + contents[i] + "</a>\n";
            }
        }
        htmlIndex += htmlDir;
        htmlIndex += htmlFil;
        htmlIndex += "</pre><hr></body>\n</html>";

        return htmlIndex;
    }

    public File getFile(String path) throws IOException {
        File file = new File(path);
        if(file.exists()){
            return file;
        } else {
            return null;
        }
    }
}
