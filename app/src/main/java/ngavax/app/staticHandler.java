package ngavax.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class staticHandler {

    private String root_path;

    public staticHandler(String root) {
        this.root_path = root;
    }

    public byte[] testIndex(String path){
        String fullPath = this.root_path + path;
        File index = new File(fullPath + "/index.html");
        if(index.isFile()){
            byte[] data = null;
            try {
                data = Files.readAllBytes(index.toPath());
            } catch (IOException e) {
                LOG.error(e);
                e.printStackTrace();
            }
            return data;
        }else{
            return autoFileDir(path);
        }
    }

    public byte[] autoFileDir(String path){
        String fullPath = this.root_path + path;

        File test = new File(fullPath);
        if(test.isDirectory()){
            return indexPath(path);
        }else{
            return getFile(path);
        }
    }

    private byte[] indexPath(String path){
        String href = path;
        String fullPath = this.root_path + path;

        //System.out.println(path);
        File dir = new File(fullPath);
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

        return htmlIndex.getBytes();
    }

    private byte[] getFile(String path) {
        Path r = Paths.get(this.root_path + path);
        byte[] data = null;
        if(Files.exists(r)){
            try {
                data = Files.readAllBytes(r);
            } catch (IOException e) {
                LOG.error(e);
                e.printStackTrace();
            }
        }
        return data;
    }
}
