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

    public byte[] testFile(String path){
        final Path initialPath = Paths.get(path);
        if(Files.isRegularFile(initialPath)){
            LOG.debug(initialPath);
            return getFile(path);
        }else{
            final Path indexPath = Paths.get(path + "index.html");
            if(Files.isRegularFile(indexPath)){
                LOG.debug(indexPath);
                return getFile(path + "index.html");
            }
        }
        return null;
    }

    public byte[] autoFileDir(String path){
        final Path test = Paths.get(path);
        if(Files.isDirectory(test)){
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
        final Path testPath = Paths.get(path);
        byte[] data = null;
        if(Files.isRegularFile(testPath)){
            try(
                RandomAccessFile aFile = new RandomAccessFile(path, "r");
                FileChannel inChannel = aFile.getChannel();
                ) {

                long fileSize = inChannel.size();

                //Create buffer of the file size
                ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
                inChannel.read(buffer);
                buffer.flip();

                data = buffer.array();
            } catch (IOException e) {
                LOG.error("Error reading file: " + path);
                e.printStackTrace();
            }
        }else{
            LOG.warn("File not found: " + path);
        }
        return data;
    }
}
