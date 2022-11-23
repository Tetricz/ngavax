package ngavax.app;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class staticHandler {

    public byte[] testIndex(String path){
        File index = new File(path + "/index.html");
        byte[] data = null;
        if(index.isFile()){
            try {
                data = Files.readAllBytes(index.toPath());
            } catch (IOException e) {
                LOG.error(e);
                e.printStackTrace();
            }
            return data;
        }else{
            return getFile(path);
        }
    }

    public byte[] autoFileDir(String path){
        File test = new File(path);
        if(test.isDirectory()){
            return indexPath(path);
        }else{
            return getFile(path);
        }
    }

    private byte[] indexPath(String path){
        //System.out.println(path);
        File dir = new File(path);
        //String for html
        StringBuilder htmlIndex = new StringBuilder("<html>\n<head><title>Index</title></head>\n<body>\n<h1>Index</h1><hr><pre>\n<a href=\"../\">../</a>\n");

        //List of all files and directories
        String contents[] = dir.list();
        StringBuilder htmlFil = new StringBuilder();
        StringBuilder htmlDir = new StringBuilder();
        for(int i=0; i < contents.length; i++) {
            if(new File(path + contents[i]).isDirectory()){
                htmlDir.append("<a href=\"" + contents[i] + "/\">" + contents[i] + "</a>\n");
            } else {
                htmlFil.append("<a href=\"" + contents[i] + "\">" + contents[i] + "</a>\n");
            }
        }
        htmlIndex.append(htmlDir);
        htmlIndex.append(htmlFil);
        htmlIndex.append("</pre><hr></body>\n</html>");

        return htmlIndex.toString().getBytes();
    }

    public byte[] getFile(String path) {

        byte[] data = null;
        try(
            RandomAccessFile aFile = new RandomAccessFile(path, "r");
            FileChannel inChannel = aFile.getChannel();) {

            long fileSize = inChannel.size();

            //Create buffer of the file size
            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
            inChannel.read(buffer);
            buffer.flip();

            data = buffer.array();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
